package xyz.phanta.wynner.module.barriersight;

import xyz.phanta.wynner.WynnModule;

@WynnModule.Module(ModuleBarrierSight.ID)
public class ModuleBarrierSight extends WynnModule {

    public static final String ID = "barrier_sight";

    @Override
    public String getName() {
        return "Barrier Sight";
    }

    @Override
    public String getDesc() {
        return "Causes barriers to become visible upon approach.";
    }

    @Override
    protected void init() {
        registerEvents(new BarrierRenderer());
    }

    // TODO config: sneaking toggles barrier renderer

}
