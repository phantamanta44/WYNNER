package xyz.phanta.wynner.common.hud;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public interface HudRenderer {

    boolean renderIn(RenderGameOverlayEvent.ElementType type);

    default boolean renderAfter() {
        return false;
    }

    boolean doRender(RenderGameOverlayEvent.ElementType type, ScaledResolution res, float partialTicks);

}
