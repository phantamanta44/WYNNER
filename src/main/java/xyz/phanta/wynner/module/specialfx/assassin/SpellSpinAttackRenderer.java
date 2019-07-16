package xyz.phanta.wynner.module.specialfx.assassin;

import io.github.phantamanta44.libnine.client.sound.SingleSound;
import io.github.phantamanta44.libnine.util.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import xyz.phanta.wynner.common.npc.NpcType;
import xyz.phanta.wynner.module.specialfx.SpellEffectRenderer;
import xyz.phanta.wynner.module.specialfx.common.ParticleSlash;
import xyz.phanta.wynner.module.specialfx.common.SfxResources;
import xyz.phanta.wynner.util.WynnRenders;

public class SpellSpinAttackRenderer implements SpellEffectRenderer {

    private static final double RANGE = 8D;
    private static final double RANGE_SQ = RANGE * RANGE;

    private static final int DURATION = 20;
    private static final int RING_SEGS = 18;

    private final Minecraft mc = Minecraft.getMinecraft();
    private final Vec3d pos = new Vec3d(mc.player.posX, mc.player.posY + 0.25D, mc.player.posZ);
    private int aliveTime = 0;

    @Override
    public void init() {
        boolean hit = false;
        double x = mc.player.posX, y = mc.player.posY, z = mc.player.posZ;
        for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(mc.player,
                new AxisAlignedBB(x - RANGE, y - 1.75D, z - RANGE, x + RANGE, y + 2.75D, z + RANGE))) {
            if (entity.getDisplayName().getFormattedText().endsWith("\u00a7r") && entity.getDistanceSq(x, y, z) <= RANGE_SQ) {
                if (NpcType.canTarget(entity)) {
                    mc.effectRenderer.addEffect(new ParticleSlash(
                            SfxResources.SLASH_TEX, entity.posX, entity.posY + entity.height / 2D, entity.posZ));
                    hit = true;
                }
            }
        }
        SoundHandler soundHandler = mc.getSoundHandler();
        if (hit) {
            soundHandler.playSound(new SingleSound(SfxResources.SLASH_SND));
        }
        soundHandler.playSound(new SingleSound(SfxResources.THUMP_SND));
    }

    @Override
    public boolean update() {
        return ++aliveTime < DURATION;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void render(float partialTicks) {
        double radius = aliveTime + partialTicks, iAngle = radius / 6F;
        Vec3d curPos = pos.subtract(WynnRenders.getInterpPos(mc.player, partialTicks));

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        float frac = (float)Math.sqrt(1F - radius / DURATION);
        GlStateManager.color(frac, frac, frac, frac);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        SfxResources.RING_XZ_TEX.bind();
        drawXzRing(tess, buf, curPos.x, curPos.y, curPos.z, radius * 1.25F);
        if (radius > 4) {
            drawXzRing(tess, buf, curPos.x, curPos.y, curPos.z, (radius - 4) * 1.25F * DURATION / (DURATION - 4));
            if (radius > 8) {
                drawXzRing(tess, buf, curPos.x, curPos.y, curPos.z, (radius - 8) * 1.25F * DURATION / (DURATION - 8));
            }
        }

        SfxResources.RING_XY_TEX.bind();
        buf.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_TEX);
        double angInc = MathUtils.PI_F * 2F / RING_SEGS;
        double y1 = curPos.y - 3D, y2 = y1 + 6D;
        double iX = curPos.x + radius * Math.cos(iAngle), iZ = curPos.z + radius * Math.sin(iAngle);
        buf.pos(iX, y2, iZ).tex(0, 0).endVertex();
        buf.pos(iX, y1, iZ).tex(0, 1).endVertex();
        for (int i = 1; i < RING_SEGS; i++) {
            double angle = iAngle + angInc * i;
            double vX = curPos.x + radius * Math.cos(angle), vZ = curPos.z + radius * Math.sin(angle);
            buf.pos(vX, y2, vZ).tex(1, 0).endVertex();
            buf.pos(vX, y1, vZ).tex(1, 1).endVertex();
            buf.pos(vX, y2, vZ).tex(0, 0).endVertex();
            buf.pos(vX, y1, vZ).tex(0, 1).endVertex();
        }
        buf.pos(iX, y2, iZ).tex(1, 0).endVertex();
        buf.pos(iX, y1, iZ).tex(1, 1).endVertex();
        tess.draw();

        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    private static void drawXzRing(Tessellator tess, BufferBuilder buf, double x, double y, double z, double radius) {
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buf.pos(x - radius, y, z - radius).tex(0, 0).endVertex();
        buf.pos(x - radius, y, z + radius).tex(0, 1).endVertex();
        buf.pos(x + radius, y, z + radius).tex(1, 1).endVertex();
        buf.pos(x + radius, y, z - radius).tex(1, 0).endVertex();
        tess.draw();
    }

}
