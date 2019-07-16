package xyz.phanta.wynner.common.player;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.phanta.wynner.Wynner;
import xyz.phanta.wynner.common.tick.ClientTickListener;
import xyz.phanta.wynner.event.SpellCastEvent;

public class PlayerDataManager implements ClientTickListener {

    private final Minecraft mc = Minecraft.getMinecraft();
    private ItemStack lastHeldItem = ItemStack.EMPTY;

    // health data
    private boolean healthValid = false;
    private float health, healthMax;

    public float getHealth() {
        return healthValid ? health : mc.player.getHealth();
    }

    public float getMaxHealth() {
        return healthValid ? healthMax : mc.player.getMaxHealth();
    }

    // spell data
    private boolean spellValid = false;
    private SpellRune spellA = SpellRune.NONE, spellB = SpellRune.NONE, spellC = SpellRune.NONE;

    public SpellRune getSpellA() {
        return spellValid ? spellA : SpellRune.NONE;
    }

    public SpellRune getSpellB() {
        return spellValid ? spellB : SpellRune.NONE;
    }

    public SpellRune getSpellC() {
        return spellValid ? spellC : SpellRune.NONE;
    }

    // sprint data
    private boolean sprintValid = false;
    private int sprintCount;
    private long lastSprintUpdate = -1L;

    public int getSprintCount() {
        return sprintValid
                ? Math.min(sprintCount + (int)((Wynner.INSTANCE.getTicker().getAsLong() - lastSprintUpdate) / 48), 13) : 13;
    }

    private void invalidate() {
        healthValid = spellValid = sprintValid = false;
        spellA = spellB = spellC = SpellRune.NONE;
    }

    @SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT && mc.ingameGUI.overlayMessageTime != 0) {
            mc.ingameGUI.overlayMessageTime = 0;
            String msg = mc.ingameGUI.overlayMessage;
            if (msg.charAt(0) == '\u00a7' && msg.charAt(1) == 'c' && msg.charAt(3) == ' ') {
                // extract hp
                int iHp = msg.indexOf("\u00a70", 4);
                if (iHp != -1) {
                    String strHp = msg.substring(4, iHp);
                    iHp = strHp.indexOf('/');
                    try {
                        health = Integer.parseInt(strHp.substring(0, iHp));
                        healthMax = Integer.parseInt(strHp.substring(iHp + 1));
                        healthValid = true;
                    } catch (NumberFormatException e) {
                        healthValid = false;
                    }
                }

                int iSpaceLeft = msg.indexOf("    "), iSpaceRight = msg.lastIndexOf("    ");
                if (iSpaceLeft != -1 && iSpaceRight != -1 && iSpaceRight - iSpaceLeft > 3) {
                    String center = msg.substring(iSpaceLeft + 4, iSpaceRight);
                    // extract spell cast
                    if (center.startsWith("\u00a7a") && center.startsWith("\u00a77-", 3)) {
                        spellValid = true;
                        spellA = SpellRune.parse(center.charAt(2));
                        if (center.charAt(7) == 'a') {
                            spellB = SpellRune.parse(center.charAt(8));
                            if (center.charAt(15) == 'a') {
                                spellC = SpellRune.parse(center.charAt(16));
                            } else {
                                spellC = SpellRune.NONE;
                            }
                        } else {
                            spellB = spellC = SpellRune.NONE;
                        }
                    } else {
                        spellValid = false;
                        // extract sprint
                        if (center.charAt(2) == '[') {
                            lastSprintUpdate = Wynner.INSTANCE.getTicker().getAsLong();
                            sprintValid = true;
                            if (center.charAt(4) == 'c') {
                                sprintCount = 0;
                            } else {
                                int sprintIndex = center.indexOf("\u00a78");
                                if (sprintIndex == -1) {
                                    sprintCount = 13;
                                } else if (sprintIndex == 3) {
                                    sprintCount = 1;
                                } else {
                                    sprintCount = sprintIndex - 4;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClientTick(long tick) {
        ItemStack held = mc.player.getHeldItemMainhand();
        if (held != lastHeldItem) {
            lastHeldItem = held;
            if (spellB != SpellRune.NONE) {
                String name = held.getDisplayName();
                if (name.startsWith("\u00a77")) {
                    int iCenter = name.indexOf(" spell cast!\u00a73 [\u00a7b-");
                    if (iCenter != -1) {
                        try {
                            MinecraftForge.EVENT_BUS.post(new SpellCastEvent(name.substring(2, iCenter),
                                    Integer.parseInt(name.substring(iCenter + 19, name.length() - 5)),
                                    spellA, spellB, spellC));
                            spellA = spellB = spellC = SpellRune.NONE;
                        } catch (NumberFormatException ignored) {
                            // NO-OP
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldChange(EntityJoinWorldEvent event) {
        if (event.getEntity() == mc.player) {
            invalidate();
        }
    }

}
