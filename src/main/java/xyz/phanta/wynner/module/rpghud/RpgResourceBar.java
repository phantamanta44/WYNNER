package xyz.phanta.wynner.module.rpghud;

import io.github.phantamanta44.libnine.util.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import xyz.phanta.wynner.Wynner;

public class RpgResourceBar {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 11;
    private static final int Z_LEVEL = -45;
    private static final ResourceLocation TEX_SMOKE = Wynner.INSTANCE.newResourceLocation("textures/rpg_hud/smoke.png");
    private static final int SEG_W = WIDTH / 3;

    private final Minecraft mc;
    private final int yOffset;
    private final float r;
    private final float g;
    private final float b;
    private float inter = 0;

    public RpgResourceBar(Minecraft mc, int yOffset, float r, float g, float b) {
        this.mc = mc;
        this.yOffset = yOffset;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void renderResourceBar(ScaledResolution res, float current, float max) {
        long time = System.currentTimeMillis();
        current = MathUtils.clamp(current, 0, max);
        if (inter < current) {
            inter = current;
        } else if (inter > max) {
            inter = max;
        } else {
            inter -= Math.min(inter - current, max / 64F);
        }

        int x1 = res.getScaledWidth() / 2 - WIDTH / 2, y1 = res.getScaledHeight() - 64 + yOffset;
        int x2 = x1 + WIDTH, y2 = y1 + HEIGHT;
        int x3 = (int)Math.floor(x1 + WIDTH * current / max);
        int xOff = (int)((time / (16 + yOffset / 2)) % (SEG_W * 2));

        GlStateManager.enableBlend();
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();

        GlStateManager.disableTexture2D();
        GlStateManager.color(0.082F, 0.082F, 0.082F, 1F);
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        buf.pos(x1 - 1, y1 - 1, Z_LEVEL).endVertex();
        buf.pos(x1 - 1, y2 + 1, Z_LEVEL).endVertex();
        buf.pos(x2 + 1, y2 + 1, Z_LEVEL).endVertex();
        buf.pos(x2 + 1, y1 - 1, Z_LEVEL).endVertex();
        tess.draw();

        GlStateManager.color(r, g, b, 1F);
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        buf.pos(x1, y1, Z_LEVEL).endVertex();
        buf.pos(x1, y2, Z_LEVEL).endVertex();
        buf.pos(x3, y2, Z_LEVEL).endVertex();
        buf.pos(x3, y1, Z_LEVEL).endVertex();
        tess.draw();

        if (inter > current) {
            int x4 = (int)Math.floor(x3 + WIDTH * (inter - current) / max);
            GlStateManager.color(0.925F, 0.529F, 0.004F, 1F);
            buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            buf.pos(x3, y1, Z_LEVEL).endVertex();
            buf.pos(x3, y2, Z_LEVEL).endVertex();
            buf.pos(x4, y2, Z_LEVEL).endVertex();
            buf.pos(x4, y1, Z_LEVEL).endVertex();
            tess.draw();
        }
        GlStateManager.enableTexture2D();

        mc.getTextureManager().bindTexture(TEX_SMOKE);
        GlStateManager.color(1F, 1F, 1F, 0.36F);
        buf.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_TEX);
        boolean invertUv = xOff >= SEG_W;
        xOff %= SEG_W;
        int xCur = Math.min(x1 + SEG_W - xOff, x3);
        float uDiffMin = (float)xOff / SEG_W, uDiffMax = (float)(xCur - x1) / SEG_W + uDiffMin;
        if (invertUv) {
            buf.pos(x1, y1, Z_LEVEL).tex(uDiffMin, 0).endVertex();
            buf.pos(x1, y2, Z_LEVEL).tex(uDiffMin, 1).endVertex();
            buf.pos(xCur, y1, Z_LEVEL).tex(uDiffMax, 0).endVertex();
            buf.pos(xCur, y2, Z_LEVEL).tex(uDiffMax, 1).endVertex();
        } else {
            buf.pos(x1, y1, Z_LEVEL).tex(1 - uDiffMin, 0).endVertex();
            buf.pos(x1, y2, Z_LEVEL).tex(1 - uDiffMin, 1).endVertex();
            buf.pos(xCur, y1, Z_LEVEL).tex(1 - uDiffMax, 0).endVertex();
            buf.pos(xCur, y2, Z_LEVEL).tex(1 - uDiffMax, 1).endVertex();
        }
        int remaining = x3 - xCur;
        while (remaining >= SEG_W) {
            xCur += SEG_W;
            int u = invertUv ? 0 : 1;
            buf.pos(xCur, y1, Z_LEVEL).tex(u, 0).endVertex();
            buf.pos(xCur, y2, Z_LEVEL).tex(u, 1).endVertex();
            invertUv = !invertUv;
            remaining = x3 - xCur;
        }
        if (remaining > 0) {
            xCur += remaining;
            float remainderU = invertUv ? (1F - (float)remaining / SEG_W) : ((float)remaining / SEG_W);
            buf.pos(xCur, y1, Z_LEVEL).tex(remainderU, 0).endVertex();
            buf.pos(xCur, y2, Z_LEVEL).tex(remainderU, 1).endVertex();
        }
        tess.draw();

        String label = String.format("%d / %d", (int)Math.floor(current), (int)Math.floor(max));
        int labelWidth = mc.fontRenderer.getStringWidth(label);
        mc.fontRenderer.drawStringWithShadow(
                label, x1 + WIDTH / 2 - labelWidth / 2, y1 - (mc.fontRenderer.FONT_HEIGHT - 1 - HEIGHT) / 2F, 0xFFFFFF);
        GlStateManager.color(1F, 1F, 1F, 1F);
    }

}
