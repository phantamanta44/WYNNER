package xyz.phanta.wynner.module.thirdeye;

import io.github.phantamanta44.libnine.util.world.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.phanta.wynner.Wynner;
import xyz.phanta.wynner.common.waypoint.Waypoint;
import xyz.phanta.wynner.util.ColourUtils;
import xyz.phanta.wynner.util.ScaledIcon;
import xyz.phanta.wynner.util.WynnRenders;

import javax.annotation.Nullable;
import java.util.Objects;

public class ThirdEyeRenderer {

    private static final double RADIUS = 64D;

    private final Minecraft mc = Minecraft.getMinecraft();
    @Nullable
    private String hoveredWaypoint;
    private double hoveredAngle;

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        Vec3d lookVec = mc.player.getLook(event.getPartialTicks());
        Vec3d playerPos = mc.player.getPositionEyes(event.getPartialTicks());
        hoveredWaypoint = null;
        hoveredAngle = Math.PI / 12D;
        GlStateManager.disableDepth();

        Wynner.INSTANCE.getWaypoints().getAll()
                .map(w -> WaypointRender.get(w, playerPos))
                .filter(Objects::nonNull)
                .sorted()
                .forEach(w -> {
                    Vec3d dPos = w.diffPos;
                    if (dPos.x * dPos.x + dPos.z * dPos.z <= RADIUS * RADIUS) {
                        double dist = dPos.lengthVector();
                        float sqrtAlpha = Math.min((float)dist / 8F, 1F);
                        if (w.useTint) {
                            ColourUtils.setGlColour(w.colour, sqrtAlpha * sqrtAlpha);
                        } else {
                            GlStateManager.color(1F, 1F, 1F, sqrtAlpha * sqrtAlpha);
                        }
                        WynnRenders.renderWorldOrtho(dPos.x, dPos.y + mc.player.getEyeHeight(), dPos.z, 0F, w.icon);
                        double angle = Math.acos(lookVec.dotProduct(dPos) / dist);
                        if (angle < hoveredAngle && Math.tan(angle) * dist < 1D) {
                            hoveredWaypoint = w.colour + w.name;
                            hoveredAngle = angle;
                        }
                    }
                });
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.enableDepth();
    }

    @Nullable
    String getHoveredWaypoint() {
        return hoveredWaypoint;
    }

    private static class WaypointRender implements Comparable<WaypointRender> {

        @Nullable
        static WaypointRender get(Waypoint waypoint, Vec3d playerPos) {
            ScaledIcon icon = waypoint.getIcon();
            if (icon != null) {
                return new WaypointRender(
                        waypoint.getName(), WorldUtils.getBlockCenter(waypoint.getPosition()).subtract(playerPos),
                        waypoint.getColour(), waypoint.useTint(), icon);
            }
            return null;
        }

        private final String name;
        private final TextFormatting colour;
        private final boolean useTint;
        private final ScaledIcon icon;
        private final Vec3d diffPos;
        private final double dist;

        private WaypointRender(String name, Vec3d diffPos, TextFormatting colour, boolean useTint, ScaledIcon icon) {
            this.name = name;
            this.diffPos = diffPos;
            this.colour = colour;
            this.useTint = useTint;
            this.icon = icon;
            this.dist = diffPos.lengthVector();
        }

        @Override
        public int compareTo(WaypointRender o) {
            return Double.compare(o.dist, dist);
        }

    }

}
