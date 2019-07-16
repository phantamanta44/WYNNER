package xyz.phanta.wynner.module.servicefinder;

import io.github.phantamanta44.libnine.util.render.TextureRegion;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import xyz.phanta.wynner.WynnModule;
import xyz.phanta.wynner.common.npc.NpcServiceType;
import xyz.phanta.wynner.common.waypoint.Waypoint;
import xyz.phanta.wynner.common.waypoint.WaypointProvider;
import xyz.phanta.wynner.util.ScaledIcon;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class ServiceWaypointProvider implements WaypointProvider {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final ModuleServiceFinder module;
    private final Map<UUID, ServiceWaypoint> tracking = new HashMap<>();

    ServiceWaypointProvider(ModuleServiceFinder module) {
        this.module = module;
    }

    @Override
    public Stream<? extends Waypoint> getProvidedWaypoints() {
        if (mc.player.inventory.getStackInSlot(8).isEmpty()) {
            return Stream.empty();
        }
        tracking.values().removeIf(e -> !e.entity.isAddedToWorld());
        return mc.world.getLoadedEntityList().stream().map(this::getWaypoint).filter(Objects::nonNull);
    }

    @Nullable
    private ServiceWaypoint getWaypoint(Entity entity) {
        String name = entity.getDisplayName().getFormattedText();
        if (name.endsWith("\u00a7r")) {
            UUID key = entity.getUniqueID();
            ServiceWaypoint result = tracking.get(key);
            if (result != null) {
                return result;
            }
            NpcServiceType service = NpcServiceType.readName(name);
            if (service != null) {
                result = new ServiceWaypoint(entity, service);
                tracking.put(key, result);
                return result;
            }
        }
        return null;
    }

    @Override
    public WynnModule getProvidingModule() {
        return module;
    }

    private static class ServiceWaypoint implements Waypoint {

        private final Entity entity;
        private final NpcServiceType service;

        ServiceWaypoint(Entity entity, NpcServiceType service) {
            this.entity = entity;
            this.service = service;
        }

        @Override
        public String getName() {
            return entity.getDisplayName().getUnformattedComponentText();
        }

        @Override
        public BlockPos getPosition() {
            return entity.getPosition();
        }

        @Override
        public TextFormatting getColour() {
            return TextFormatting.LIGHT_PURPLE;
        }

        @Nullable
        @Override
        public ScaledIcon getIcon() {
            return service.getMapIcon();
        }

    }

}
