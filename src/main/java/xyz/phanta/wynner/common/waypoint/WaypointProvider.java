package xyz.phanta.wynner.common.waypoint;

import xyz.phanta.wynner.common.ModuleProvided;

import java.util.stream.Stream;

public interface WaypointProvider extends ModuleProvided {

    Stream<? extends Waypoint> getProvidedWaypoints();

}
