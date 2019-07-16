package xyz.phanta.wynner.module.barriersight;

import io.github.phantamanta44.libnine.util.world.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import xyz.phanta.wynner.util.WynnRenders;

public class BarrierRenderer {

    private static final int RADIUS = 5;
    private static final int RADIUS_SQ = RADIUS * RADIUS;

    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!mc.player.isSneaking()) {
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buf = tess.getBuffer();
            Vec3d plPos = WynnRenders.getInterpPos(mc.player, event.getPartialTicks());
            BlockPos plCoords = mc.player.getPosition();
            double offX = plPos.x - plCoords.getX(), offY = plPos.y - plCoords.getY(), offZ = plPos.z - plCoords.getZ();
            for (int x = -RADIUS; x <= RADIUS; x++) {
                for (int y = -RADIUS; y <= RADIUS + 1; y++) {
                    for (int z = -RADIUS; z <= RADIUS; z++) {
                        BlockPos blCoords = plCoords.add(x, y, z);
                        double distSq = WorldUtils.getBlockCenter(blCoords).squareDistanceTo(plPos.x, plPos.y, plPos.z);
                        if (distSq < RADIUS_SQ && mc.world.getBlockState(blCoords).getBlock() == Blocks.BARRIER) {
                            GlStateManager.color(1F, 1F, 1F, (1F - (float)Math.sqrt(distSq) / RADIUS) / 1.5F);
                            buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
                            double x0 = x - offX, x1 = x0 + 1;
                            double y0 = y - offY, y1 = y0 + 1;
                            double z0 = z - offZ, z1 = z0 + 1;
                            if (doesNotCull(mc.world.getBlockState(blCoords.add(-1, 0, 0)))) {
                                buf.pos(x0, y1, z0).endVertex();
                                buf.pos(x0, y0, z0).endVertex();
                                buf.pos(x0, y0, z1).endVertex();
                                buf.pos(x0, y1, z1).endVertex();
                            }
                            if (doesNotCull(mc.world.getBlockState(blCoords.add(0, -1, 0)))) {
                                buf.pos(x0, y0, z0).endVertex();
                                buf.pos(x1, y0, z0).endVertex();
                                buf.pos(x1, y0, z1).endVertex();
                                buf.pos(x0, y0, z1).endVertex();
                            }
                            if (doesNotCull(mc.world.getBlockState(blCoords.add(0, 0, -1)))) {
                                buf.pos(x1, y1, z0).endVertex();
                                buf.pos(x1, y0, z0).endVertex();
                                buf.pos(x0, y0, z0).endVertex();
                                buf.pos(x0, y1, z0).endVertex();
                            }
                            if (doesNotCull(mc.world.getBlockState(blCoords.add(1, 0, 0)))) {
                                buf.pos(x1, y1, z1).endVertex();
                                buf.pos(x1, y0, z1).endVertex();
                                buf.pos(x1, y0, z0).endVertex();
                                buf.pos(x1, y1, z0).endVertex();
                            }
                            if (doesNotCull(mc.world.getBlockState(blCoords.add(0, 1, 0)))) {
                                buf.pos(x0, y1, z1).endVertex();
                                buf.pos(x1, y1, z1).endVertex();
                                buf.pos(x1, y1, z0).endVertex();
                                buf.pos(x0, y1, z0).endVertex();
                            }
                            if (doesNotCull(mc.world.getBlockState(blCoords.add(0, 0, 1)))) {
                                buf.pos(x0, y1, z1).endVertex();
                                buf.pos(x0, y0, z1).endVertex();
                                buf.pos(x1, y0, z1).endVertex();
                                buf.pos(x1, y1, z1).endVertex();
                            }
                            tess.draw();
                        }
                    }
                }
            }
            GlStateManager.color(1F, 1F, 1F, 1F);
            GlStateManager.enableTexture2D();
        }
    }

    private static boolean doesNotCull(IBlockState state) {
        return !state.isOpaqueCube() && state.getBlock() != Blocks.BARRIER;
    }

}
