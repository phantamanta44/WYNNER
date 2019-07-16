package xyz.phanta.wynner.util;

import io.github.phantamanta44.libnine.util.render.TextureRegion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class WynnRenders {

    public static Vec3d getInterpPos(Entity entity, float partialTicks) {
        return new Vec3d(entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks,
                entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks,
                entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks);
    }

    public static void renderWorldOrtho(double x, double y, double z,
                                        float scaleX, float scaleY, float angle, float u1, float v1, float u2, float v2,
                                        float lookX, float lookXZ, float lookZ, float lookYZ, float lookXY) {
        Vec3d[] vertices = new Vec3d[] {
                new Vec3d(-lookX * scaleX - lookYZ * scaleY, -lookXZ * scaleY, -lookZ * scaleX - lookXY * scaleY),
                new Vec3d(-lookX * scaleX + lookYZ * scaleY, lookXZ * scaleY, -lookZ * scaleX + lookXY * scaleY),
                new Vec3d(lookX * scaleX + lookYZ * scaleY, lookXZ * scaleY, lookZ * scaleX + lookXY * scaleY),
                new Vec3d(lookX * scaleX - lookYZ * scaleY, -lookXZ * scaleY, lookZ * scaleX - lookXY * scaleY)
        };
        float angX = MathHelper.cos(angle * 0.5F);
        Vec3d axial = Minecraft.getMinecraft().player.getLookVec().scale(MathHelper.sin(angle * 0.5F));
        for (int i = 0; i < 4; ++i) {
            vertices[i] = axial.scale(2D * vertices[i].dotProduct(axial))
                    .add(vertices[i].scale(angX * angX - axial.dotProduct(axial)))
                    .add(axial.crossProduct(vertices[i]).scale(2F * angX));
        }
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buf.pos(x + vertices[0].x, y + vertices[0].y, z + vertices[0].z).tex(u2, v2).endVertex();
        buf.pos(x + vertices[1].x, y + vertices[1].y, z + vertices[1].z).tex(u2, v1).endVertex();
        buf.pos(x + vertices[2].x, y + vertices[2].y, z + vertices[2].z).tex(u1, v1).endVertex();
        buf.pos(x + vertices[3].x, y + vertices[3].y, z + vertices[3].z).tex(u1, v2).endVertex();
        tess.draw();
    }

    public static void renderWorldOrtho(double x, double y, double z, float scaleX, float scaleY, float angle,
                                        float lookX, float lookXZ, float lookZ, float lookYZ, float lookXY) {
        renderWorldOrtho(x, y, z, scaleX, scaleY, angle, 0F, 0F, 1F, 1F, lookX, lookXZ, lookZ, lookYZ, lookXY);
    }

    public static void renderWorldOrtho(double x, double y, double z,
                                        float scaleX, float scaleY, float angle, float u1, float v1, float u2, float v2) {
        renderWorldOrtho(x, y, z, scaleX, scaleY, angle, u1, v1, u2, v2,
                ActiveRenderInfo.getRotationX(), ActiveRenderInfo.getRotationXZ(), ActiveRenderInfo.getRotationZ(),
                ActiveRenderInfo.getRotationYZ(), ActiveRenderInfo.getRotationXY());
    }

    public static void renderWorldOrtho(double x, double y, double z,
                                        float scaleX, float scaleY, float angle, TextureRegion icon) {
        icon.getTexture().bind();
        renderWorldOrtho(x, y, z, scaleX, scaleY, angle, icon.getU1(), icon.getV1(), icon.getU2(), icon.getV2());
    }

    public static void renderWorldOrtho(double x, double y, double z, float angle, ScaledIcon icon) {
        renderWorldOrtho(x, y, z, icon.getHorizontalScale(), icon.getVerticalScale(), angle, icon.getIcon());
    }

    public static void renderWorldOrtho(double x, double y, double z, float scaleX, float scaleY, float angle) {
        renderWorldOrtho(x, y, z, scaleX, scaleY, angle, 0F, 0F, 1F, 1F);
    }

    public static void renderScreenOverlay(TextureRegion texture, ScaledResolution res) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        texture.draw(0, 0, res.getScaledWidth(), res.getScaledHeight());
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
    }

}
