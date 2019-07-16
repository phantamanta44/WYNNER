package xyz.phanta.wynner.module.questcompass;

import io.github.phantamanta44.libnine.util.math.MathUtils;
import io.github.phantamanta44.libnine.util.render.TextureRegion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import xyz.phanta.wynner.Wynner;
import xyz.phanta.wynner.common.hud.HudRenderer;
import xyz.phanta.wynner.util.WynnRenders;

public class CompassOverlayRenderer implements HudRenderer {

    private static final TextureRegion POINTER_TEX = Wynner.INSTANCE
            .newTextureResource("textures/quest_compass/pointer.png", 64, 64).getRegion(0, 0, 64, 64);

    private final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public boolean renderIn(RenderGameOverlayEvent.ElementType type) {
        return type == RenderGameOverlayEvent.ElementType.CROSSHAIRS;
    }

    @Override
    public boolean doRender(RenderGameOverlayEvent.ElementType type, ScaledResolution res, float partialTicks) {
        BlockPos pos = Wynner.INSTANCE.getQuestData().getTrackedPosition();
        if (pos != null) {
            Vec3d playerPos = WynnRenders.getInterpPos(mc.player, partialTicks);
            float angGl = mc.player.rotationYaw
                    - (float)Math.atan2(pos.getZ() - playerPos.z, pos.getX() - playerPos.x) * MathUtils.R2D_F + 90F;
            float angGeo = (-90F - angGl) * MathUtils.D2R_F;
            float drawRadius = res.getScaledHeight() * 0.2778F;
            float halfWidth = res.getScaledWidth() / 2F, halfHeight = res.getScaledHeight() / 2F;
            GlStateManager.enableBlend();
            GlStateManager.pushMatrix();
            GlStateManager.translate(halfWidth, halfHeight, 0F);
            GlStateManager.rotate(angGl, 0F, 0F, -1F);
            POINTER_TEX.draw(-28, -drawRadius - 28, 56, 56);
            GlStateManager.popMatrix();
            double dist = Math.hypot(pos.getX() + 0.5D - playerPos.x, pos.getZ() + 0.5D - playerPos.z);
            String text = dist < 100D ? String.format("%.1f m", dist)
                    : dist < 1000D ? String.format(".%d km", (int)Math.floor(dist / 10D))
                    : String.format("%.1f km", dist / 1000D);
            mc.fontRenderer.drawStringWithShadow(text,
                    halfWidth + drawRadius * (float)Math.cos(angGeo) - mc.fontRenderer.getStringWidth(text) / 2F,
                    halfHeight + drawRadius * (float)Math.sin(angGeo) - mc.fontRenderer.FONT_HEIGHT / 2F, 0xFFAA00);
        }
        return true;
    }

}
