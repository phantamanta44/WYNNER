package xyz.phanta.wynner.module.specialfx.common;

import io.github.phantamanta44.libnine.util.render.RenderUtils;
import io.github.phantamanta44.libnine.util.render.TextureResource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import xyz.phanta.wynner.util.WynnRenders;

public class ParticleSlash extends Particle {

    private final TextureResource texture;

    public ParticleSlash(TextureResource texture, double x, double y, double z) {
        super(Minecraft.getMinecraft().world, x, y, z);
        this.texture = texture;
        this.particleAngle = world.rand.nextFloat() * 360;
        this.particleMaxAge = 14;
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        if (particleAge++ >= particleMaxAge) {
            setExpired();
        }
    }

    @Override
    public void renderParticle(BufferBuilder unused, Entity player, float partialTicks,
                               float lookX, float lookXZ, float lookZ, float lookYZ, float lookXY) {
        float interpAge = Math.min(particleAge + partialTicks, particleMaxAge);
        float frac = Math.min(interpAge / 1.5F, 1F);
        texture.bind();
        RenderUtils.enableFullBrightness();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        if (interpAge > 1.5F) {
            GlStateManager.color(1F, 1F, 1F, 1F - (interpAge - 1.5F) / (particleMaxAge - 1.5F));
        }
        WynnRenders.renderWorldOrtho(prevPosX + (posX - prevPosX) * partialTicks - interpPosX,
                prevPosY + (posY - prevPosY) * partialTicks - interpPosY,
                prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ,
                1.3F * (1.2F - frac), 2.5F * frac, particleAngle, lookX, lookXZ, lookZ, lookYZ, lookXY);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        RenderUtils.restoreLightmap();
    }

}
