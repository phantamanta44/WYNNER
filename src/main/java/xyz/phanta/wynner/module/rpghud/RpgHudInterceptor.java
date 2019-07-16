package xyz.phanta.wynner.module.rpghud;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import xyz.phanta.wynner.common.hud.HudRenderer;

public class RpgHudInterceptor implements HudRenderer {

    @Override
    public boolean renderIn(RenderGameOverlayEvent.ElementType type) {
        return type == RenderGameOverlayEvent.ElementType.FOOD || type == RenderGameOverlayEvent.ElementType.HEALTHMOUNT;
    }

    @Override
    public boolean doRender(RenderGameOverlayEvent.ElementType type, ScaledResolution res, float partialTicks) {
        return false;
    }

}
