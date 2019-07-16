package xyz.phanta.wynner.module.statusoverlay;

import io.github.phantamanta44.libnine.util.math.MathUtils;
import io.github.phantamanta44.libnine.util.render.TextureRegion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import xyz.phanta.wynner.Wynner;
import xyz.phanta.wynner.common.hud.HudRenderer;
import xyz.phanta.wynner.util.WynnRenders;

public class FreezeStatusRenderer implements HudRenderer {

    private static final TextureRegion OVERLAY_TEX = Wynner.INSTANCE
            .newTextureResource("textures/status_overlay/freeze.png", 640, 384).getRegion(0, 0, 640, 384);
    private static final float MAX_STRENGTH = 10F;

    private final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public boolean renderIn(RenderGameOverlayEvent.ElementType type) {
        return type == RenderGameOverlayEvent.ElementType.PORTAL;
    }

    @Override
    public boolean doRender(RenderGameOverlayEvent.ElementType type, ScaledResolution res, float partialTicks) {
        PotionEffect effect = mc.player.getActivePotionEffect(MobEffects.SLOWNESS);
        if (effect != null) {
            GlStateManager.color(1F, 1F, 1F, MathUtils.clamp(
                    (effect.getDuration() - partialTicks) / MAX_STRENGTH, 0F, Math.min((1 + effect.getAmplifier()) * 0.3F, 1F)));
            WynnRenders.renderScreenOverlay(OVERLAY_TEX, res);
            GlStateManager.color(1F, 1F, 1F, 1F);
        }
        return false;
    }

}
