package xyz.phanta.wynner.module.thirdeye;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import xyz.phanta.wynner.common.hud.HudRenderer;

public class ThirdEyeWaypointNameOverlay implements HudRenderer {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final ThirdEyeRenderer waypointProvider;

    ThirdEyeWaypointNameOverlay(ThirdEyeRenderer waypointProvider) {
        this.waypointProvider = waypointProvider;
    }

    @Override
    public boolean renderIn(RenderGameOverlayEvent.ElementType type) {
        return type == RenderGameOverlayEvent.ElementType.CROSSHAIRS;
    }

    @Override
    public boolean doRender(RenderGameOverlayEvent.ElementType type, ScaledResolution res, float partialTicks) {
        String text = waypointProvider.getHoveredWaypoint();
        if (text != null) {
            mc.fontRenderer.drawStringWithShadow(text,
                    res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(text) / 2, res.getScaledHeight() / 2 - 20, 0xFFFFFF);
            GlStateManager.color(1F, 1F, 1F, 1F);
        }
        return true;
    }

}
