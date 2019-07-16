package xyz.phanta.wynner.module.servicefinder;

import xyz.phanta.wynner.WynnModule;
import xyz.phanta.wynner.Wynner;

@WynnModule.Module(ModuleServiceFinder.ID)
public class ModuleServiceFinder extends WynnModule {

    public static final String ID = "service_finder";

    @Override
    public String getName() {
        return "Service Finder";
    }

    @Override
    public String getDesc() {
        return "Provides waypoints for nearby shops and services.";
    }

    @Override
    protected void init() {
        ServiceWaypointProvider scanner = new ServiceWaypointProvider(this);
        Wynner.INSTANCE.getWaypoints().registerProvider(scanner);
    }

    // FIXME replace with world map module once map data is made available

}
