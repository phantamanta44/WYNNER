package xyz.phanta.wynner.module.invdata;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.phanta.wynner.common.item.ItemPrereqs;
import xyz.phanta.wynner.common.item.ItemTier;
import xyz.phanta.wynner.common.item.UnidentifiedItem;
import xyz.phanta.wynner.util.ColourUtils;

public class InventoryDataOverlayRenderer {

    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onDrawContainer(GuiContainerEvent.DrawForeground event) {
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.75F, 0.75F, 1F);
        event.getGuiContainer().inventorySlots.inventorySlots.forEach(slot -> {
            ItemStack stack = slot.getStack();
            if (!stack.isEmpty()) {
                ItemPrereqs reqs = ItemPrereqs.getFor(stack);
                if (reqs.hasLevelReq()) {
                    String text = Integer.toString(reqs.getLevelReq());
                    mc.fontRenderer.drawStringWithShadow(
                            text, (slot.xPos + 15 - mc.fontRenderer.getStringWidth(text) / 2F) / 0.75F, slot.yPos / 0.75F,
                            ColourUtils.getTextColour(ItemTier.getFor(stack).orElse(ItemTier.NORMAL).colour));
                } else {
                    UnidentifiedItem ui = UnidentifiedItem.getFor(stack);
                    if (ui != null) {
                        String text = ui.getBaseLevel() + "?";
                        mc.fontRenderer.drawStringWithShadow(text,
                                (slot.xPos + 15 - mc.fontRenderer.getStringWidth(text) / 2F) / 0.75F, slot.yPos / 0.75F,
                                ColourUtils.getTextColour(ColourUtils.getDarker(ui.getTier().colour)));
                    }
                }
            }
        });
        GlStateManager.popMatrix();
        GlStateManager.enableDepth();
    }

}
