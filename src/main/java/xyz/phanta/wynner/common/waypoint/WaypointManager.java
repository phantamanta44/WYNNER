package xyz.phanta.wynner.common.waypoint;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Stream;

public class WaypointManager {

    private final Collection<WaypointProvider> providers = new HashSet<>();

    public void registerProvider(WaypointProvider provider) {
        providers.add(provider);
    }

    public Collection<WaypointProvider> getProviders() {
        return providers;
    }

    public Stream<Waypoint> getAll() {
        return providers.stream().flatMap(WaypointProvider::getProvidedWaypoints);
    }

}
