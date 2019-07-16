package xyz.phanta.wynner.module.statusoverlay;

import io.github.phantamanta44.libnine.util.math.MathUtils;
import io.github.phantamanta44.libnine.util.render.TextureRegion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import xyz.phanta.wynner.Wynner;
import xyz.phanta.wynner.common.hud.HudRenderer;
import xyz.phanta.wynner.common.tick.ClientTickListener;
import xyz.phanta.wynner.util.RepeatingSound;
import xyz.phanta.wynner.util.WynnRenders;

import javax.annotation.Nullable;

public class InvisibilityStatusHandler implements HudRenderer, ClientTickListener {

    private static final ResourceLocation STEALTH_SOUND = Wynner.INSTANCE.newResourceLocation("wynner.status_overlay.stealth_loop");
    private static final TextureRegion OVERLAY_TEX = Wynner.INSTANCE
            .newTextureResource("textures/status_overlay/stealth.png", 512, 384).getRegion(0, 0, 512, 384);
    private static final int MAX_STRENGTH = 8;

    private final Minecraft mc = Minecraft.getMinecraft();
    @Nullable
    private RepeatingSound sound = null;
    private int strength = 0;
    private boolean deltaPlus = false;

    @Override
    public void onClientTick(long tick) {
        if (mc.player.getActivePotionEffect(MobEffects.INVISIBILITY) != null) {
            if (sound == null) {
                sound = new RepeatingSound(STEALTH_SOUND, 1F, 1F, SoundCategory.MASTER);
                mc.getSoundHandler().playSound(sound);
            }
            if (strength < MAX_STRENGTH) {
                ++strength;
            }
            deltaPlus = true;
        } else if (strength > 0) {
            --strength;
            deltaPlus = false;
        } else if (sound != null) {
            sound.kill();
            mc.getSoundHandler().stopSound(sound);
            sound = null;
            deltaPlus = false;
        }
    }

    @Override
    public boolean renderIn(RenderGameOverlayEvent.ElementType type) {
        return type == RenderGameOverlayEvent.ElementType.PORTAL && strength > 0;
    }

    @Override
    public boolean doRender(RenderGameOverlayEvent.ElementType type, ScaledResolution res, float partialTicks) {
        GlStateManager.color(1F, 1F, 1F,
                MathUtils.clamp((strength + (deltaPlus ? partialTicks : -partialTicks)) / MAX_STRENGTH, 0F, 1F));
        WynnRenders.renderScreenOverlay(OVERLAY_TEX, res);
        GlStateManager.color(1F, 1F, 1F, 1F);
        return true;
    }

}
