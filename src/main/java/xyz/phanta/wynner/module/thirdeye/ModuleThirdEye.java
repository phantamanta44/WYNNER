package xyz.phanta.wynner.module.thirdeye;

import xyz.phanta.wynner.WynnModule;
import xyz.phanta.wynner.Wynner;

@WynnModule.Module(ModuleThirdEye.ID)
public class ModuleThirdEye extends WynnModule {

    public static final String ID = "third_eye";

    @Override
    public String getName() {
        return "Third Eye";
    }

    @Override
    public String getDesc() {
        return "Displays in-world indicators for important waypoints.";
    }

    @Override
    protected void init() {
        ThirdEyeRenderer teRenderer = new ThirdEyeRenderer();
        registerEvents(teRenderer);
        Wynner.INSTANCE.getHudHandler().registerRenderer(this, new ThirdEyeWaypointNameOverlay(teRenderer));
    }

    // TODO config: blacklist/whitelist waypoint providers
    // TODO config: waypoint render scale
    // TODO config: waypoint name display thresholds
    // TODO config: waypoint render min/max distance

}
