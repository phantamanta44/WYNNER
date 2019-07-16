package xyz.phanta.wynner.module.statusoverlay;

import xyz.phanta.wynner.WynnModule;
import xyz.phanta.wynner.Wynner;

@WynnModule.Module(ModuleStatusOverlay.ID)
public class ModuleStatusOverlay extends WynnModule {

    public static final String ID = "status_overlay";

    @Override
    public String getName() {
        return "Status Overlay";
    }

    @Override
    public String getDesc() {
        return "Dynamically applies screen overlays to reflect player status.";
    }

    @Override
    protected void init() {
        LowHealthRenderer health = new LowHealthRenderer();
        Wynner.INSTANCE.getHudHandler().registerRenderer(this, health);
        Wynner.INSTANCE.getTicker().registerListener(health);

        Wynner.INSTANCE.getHudHandler().registerRenderer(this, new FreezeStatusRenderer());

        InvisibilityStatusHandler invis = new InvisibilityStatusHandler();
        registerEvents(invis);
        Wynner.INSTANCE.getHudHandler().registerRenderer(this, invis);
        Wynner.INSTANCE.getTicker().registerListener(invis);
    }

    // TODO config: enable/disable individual status handlers

}
