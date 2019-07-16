package xyz.phanta.wynner.module.statusoverlay;

import io.github.phantamanta44.libnine.util.math.MathUtils;
import io.github.phantamanta44.libnine.util.render.TextureRegion;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import xyz.phanta.wynner.Wynner;
import xyz.phanta.wynner.common.hud.HudRenderer;
import xyz.phanta.wynner.common.player.PlayerDataManager;
import xyz.phanta.wynner.common.tick.ClientTickListener;
import xyz.phanta.wynner.util.WynnRenders;

public class LowHealthRenderer implements HudRenderer, ClientTickListener {

    private static final TextureRegion OVERLAY_TEX = Wynner.INSTANCE
            .newTextureResource("textures/status_overlay/health.png", 512, 512).getRegion(0, 0, 512, 512);
    private static final float MAX_STRENGTH = 5F;
    private static final int DURATION = 8;
    private static final float DENOM = MAX_STRENGTH * DURATION;

    private float lastKnownHealth = 0F;
    private float strength = 0;
    private int time = 0;

    @Override
    public void onClientTick(long tick) {
        PlayerDataManager playerData = Wynner.INSTANCE.getPlayerData();
        float frac = playerData.getHealth() / playerData.getMaxHealth();
        if (frac != lastKnownHealth) {
            if (frac < lastKnownHealth) {
                strength = (1 - frac) * (1 - frac) * MAX_STRENGTH;
                time = DURATION;
            }
            lastKnownHealth = frac;
        } else if (time > 0) {
            --time;
        }
    }

    @Override
    public boolean renderIn(RenderGameOverlayEvent.ElementType type) {
        return type == RenderGameOverlayEvent.ElementType.PORTAL && time > 0;
    }

    @Override
    public boolean doRender(RenderGameOverlayEvent.ElementType type, ScaledResolution res, float partialTicks) {
        GlStateManager.color(1F, 1F, 1F, MathUtils.clamp(strength * (time - partialTicks) / DENOM, 0F, 1F));
        WynnRenders.renderScreenOverlay(OVERLAY_TEX, res);
        GlStateManager.color(1F, 1F, 1F, 1F);
        return true;
    }

}
