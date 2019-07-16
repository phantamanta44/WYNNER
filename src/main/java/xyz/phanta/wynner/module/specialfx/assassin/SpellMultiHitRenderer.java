package xyz.phanta.wynner.module.specialfx.assassin;

import io.github.phantamanta44.libnine.client.sound.SingleSound;
import io.github.phantamanta44.libnine.util.math.MathUtils;
import io.github.phantamanta44.libnine.util.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import xyz.phanta.wynner.common.npc.NpcType;
import xyz.phanta.wynner.module.specialfx.SpellEffectRenderer;
import xyz.phanta.wynner.module.specialfx.common.ParticleSlash;
import xyz.phanta.wynner.module.specialfx.common.SfxResources;
import xyz.phanta.wynner.util.WynnRenders;

import java.util.HashSet;
import java.util.Set;

public class SpellMultiHitRenderer implements SpellEffectRenderer {

    private static final double RANGE = 1.5D;
    private static final int DURATION = 20;

    private static final int RING_SEGS = 8;

    private final Minecraft mc = Minecraft.getMinecraft();
    private final Vec3d center;
    private final Set<EntityEntry> tracking = new HashSet<>();
    private int aliveTime = 0;

    public SpellMultiHitRenderer() {
        this.center = mc.player.getPositionVector().add(mc.player.getLookVec().scale(RANGE));
    }

    @Override
    public void init() {
        for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(mc.player, new AxisAlignedBB(
                center.x - RANGE, center.y - RANGE, center.z - RANGE, center.x + RANGE, center.y + RANGE, center.z + RANGE))) {
            if (NpcType.canTarget(entity)) {
                tracking.add(new EntityEntry(entity));
            }
        }
        mc.getSoundHandler().playSound(new SingleSound(SfxResources.BLADE_SND));
    }

    @Override
    public boolean update() {
        if (++aliveTime < DURATION) {
            for (EntityEntry entry : tracking) {
                Entity entity = entry.entity;
                if (entity.isAddedToWorld() && entry.update()) {
                    mc.effectRenderer.addEffect(new ParticleSlash(
                            SfxResources.MINI_SLASH_TEX, entity.posX, entity.posY + entity.height / 2D, entity.posZ));
                    mc.getSoundHandler().playSound(
                            new SingleSound(SfxResources.MINI_SLASH_SND, 1F, 0.85F + 0.3F * mc.world.rand.nextFloat(),
                                    entity.getPosition(), SoundCategory.MASTER));
                }
            }
            return true;
        } else {
            destroy();
            return false;
        }
    }

    @Override
    public void destroy() {
        tracking.clear();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void render(float partialTicks) {
        float interpTime = aliveTime + partialTicks;
        float frac = Math.min(interpTime / DURATION, 1F), nFrac = 1 - frac;
        double radius = interpTime / 3F;
        Vec3d curPos = center.subtract(WynnRenders.getInterpPos(mc.player, partialTicks));

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        RenderUtils.enableFullBrightness();
        GlStateManager.disableDepth();
        GlStateManager.color(1F, 1F, 1F, nFrac * nFrac);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        SfxResources.AURA_XZ_TEX.bind();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buf.pos(curPos.x - radius, curPos.y, curPos.z - radius).tex(0, 0).endVertex();
        buf.pos(curPos.x - radius, curPos.y, curPos.z + radius).tex(0, 1).endVertex();
        buf.pos(curPos.x + radius, curPos.y, curPos.z + radius).tex(1, 1).endVertex();
        buf.pos(curPos.x + radius, curPos.y, curPos.z - radius).tex(1, 0).endVertex();
        tess.draw();

        SfxResources.AURA_XY_TEX.bind();
        buf.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_TEX);
        double angInc = MathUtils.PI_F * 2F / RING_SEGS;
        double y1 = curPos.y - 1D, y2 = y1 + 10D - 6D * frac;
        double iX = curPos.x + radius;
        buf.pos(iX, y2, curPos.z).tex(0, 0).endVertex();
        buf.pos(iX, y1, curPos.z).tex(0, 1).endVertex();
        for (int i = 1; i < RING_SEGS; i++) {
            double angle = angInc * i;
            double vX = curPos.x + radius * Math.cos(angle), vZ = curPos.z + radius * Math.sin(angle);
            buf.pos(vX, y2, vZ).tex(1, 0).endVertex();
            buf.pos(vX, y1, vZ).tex(1, 1).endVertex();
            buf.pos(vX, y2, vZ).tex(0, 0).endVertex();
            buf.pos(vX, y1, vZ).tex(0, 1).endVertex();
        }
        buf.pos(iX, y2, curPos.z).tex(1, 0).endVertex();
        buf.pos(iX, y1, curPos.z).tex(1, 1).endVertex();
        tess.draw();

        SfxResources.BLADE_TEX.bind();
        GlStateManager.color(1F, 1F, 1F, (float)Math.sqrt(nFrac));
        GlStateManager.enableDepth();
        GlStateManager.pushMatrix();
        GlStateManager.translate(curPos.x, curPos.y, curPos.z);
        GlStateManager.rotate(mc.player.rotationYaw + 180F, 0F, -1F, 0F);
        double bladeX = Math.min(interpTime, 2.5D);
        double bladeHeight = Math.max(24D - interpTime * 10D, -4D);
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buf.pos(-bladeX, bladeHeight + 10D, 0).tex(0, 0).endVertex();
        buf.pos(-bladeX, bladeHeight, 0).tex(0, 1).endVertex();
        buf.pos(bladeX, bladeHeight, 0).tex(1, 1).endVertex();
        buf.pos(bladeX, bladeHeight + 10D, 0).tex(1, 0).endVertex();
        tess.draw();
        GlStateManager.popMatrix();

        GlStateManager.color(1F, 1F, 1F, 1F);
        RenderUtils.restoreLightmap();
    }

    private static class EntityEntry {

        private final Entity entity;
        private int cooldown = 0;

        EntityEntry(Entity entity) {
            this.entity = entity;
        }

        boolean update() {
            if (cooldown == 0) {
                cooldown = 2;
                return true;
            } else {
                --cooldown;
                return false;
            }
        }

    }

}
