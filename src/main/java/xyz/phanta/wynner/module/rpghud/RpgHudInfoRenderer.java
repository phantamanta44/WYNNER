package xyz.phanta.wynner.module.rpghud;

import io.github.phantamanta44.libnine.util.render.TextureRegion;
import io.github.phantamanta44.libnine.util.render.TextureResource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import xyz.phanta.wynner.Wynner;
import xyz.phanta.wynner.common.hud.HudRenderer;
import xyz.phanta.wynner.common.player.PlayerDataManager;
import xyz.phanta.wynner.common.player.SpellRune;

public class RpgHudInfoRenderer implements HudRenderer {

    private static final TextureResource RUNE_TEX = Wynner.INSTANCE.newTextureResource("textures/rpg_hud/runes.png", 64, 32);
    private static final TextureRegion RUNE_LEFT = RUNE_TEX.getRegion(0, 0, 32, 32);
    private static final TextureRegion RUNE_RIGHT = RUNE_TEX.getRegion(32, 0, 32, 32);

    private static final TextureResource SPRINT_TEX = Wynner.INSTANCE.newTextureResource("textures/rpg_hud/sprint.png", 78, 30);
    private static final TextureRegion SPRINT_BG = SPRINT_TEX.getRegion(0, 0, 78, 10);
    private static final TextureRegion SPRINT_FG = SPRINT_TEX.getRegion(0, 10, 78, 10);
    private static final TextureRegion SPRINT_EMPTY = SPRINT_TEX.getRegion(0, 20, 78, 10);

    private static TextureRegion getRuneTexture(SpellRune rune) {
        switch (rune) {
            case LEFT:
                return RUNE_LEFT;
            case RIGHT:
                return RUNE_RIGHT;
            default:
                throw new UnsupportedOperationException(rune.name());
        }
    }

    private final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public boolean renderIn(RenderGameOverlayEvent.ElementType type) {
        return type == RenderGameOverlayEvent.ElementType.TEXT;
    }

    @Override
    public boolean renderAfter() {
        return true;
    }

    @Override
    public boolean doRender(RenderGameOverlayEvent.ElementType type, ScaledResolution res, float partialTicks) {
        int halfWidth = res.getScaledWidth() / 2, halfHeight = res.getScaledHeight() / 2;

        // held item tooltip
        mc.gameSettings.heldItemTooltips = false;
        ItemStack held = mc.player.getHeldItemMainhand();
        if (!held.isEmpty()) {
            String name = held.getDisplayName();
            mc.fontRenderer.drawStringWithShadow(name,
                    halfWidth - mc.fontRenderer.getStringWidth(name) / 2,
                    res.getScaledHeight() - 82, 0xFFFFFF);
            GlStateManager.color(1F, 1F, 1F, 1F);
        }

        // spell runes / sprint bar
        GlStateManager.enableBlend();
        PlayerDataManager playerData = Wynner.INSTANCE.getPlayerData();
        if (playerData.getSpellA() != SpellRune.NONE) {
            RUNE_RIGHT.draw(halfWidth - 27, halfHeight + 10, 16, 16);
            if (playerData.getSpellB() != SpellRune.NONE) {
                getRuneTexture(playerData.getSpellB()).draw(halfWidth - 8, halfHeight + 10, 16, 16);
                if (playerData.getSpellC() != SpellRune.NONE) {
                    getRuneTexture(playerData.getSpellC()).draw(halfWidth + 11, halfHeight + 10, 16, 16);
                }
            }
        } else {
            int sprint = playerData.getSprintCount();
            if (sprint < 13) {
                int sprintX = halfWidth - 39, sprintY = halfHeight + 13;
                SPRINT_BG.draw(sprintX, sprintY);
                if (sprint == 0) {
                    GlStateManager.color(1F, 1F, 1F, (float)Math.sin(System.currentTimeMillis() * 0.0083776));
                    SPRINT_EMPTY.draw(sprintX, sprintY);
                    GlStateManager.color(1F, 1F, 1F, 1F);
                } else {
                    SPRINT_FG.drawPartial(sprintX, sprintY, 0F, 0F, sprint / 13F, 1F);
                }
            }
        }
        return true;
    }

}
