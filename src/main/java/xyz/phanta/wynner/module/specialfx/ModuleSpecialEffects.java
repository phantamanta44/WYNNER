package xyz.phanta.wynner.module.specialfx;

import xyz.phanta.wynner.WynnModule;
import xyz.phanta.wynner.Wynner;

@WynnModule.Module(ModuleSpecialEffects.ID)
public class ModuleSpecialEffects extends WynnModule {

    public static final String ID = "special_fx";

    private final SpellEffectDispatcher effectDispatcher = new SpellEffectDispatcher();

    @Override
    public String getName() {
        return "Special Effects";
    }

    @Override
    public String getDesc() {
        return "Makes in-world effects look better.";
    }

    @Override
    protected void init() {
        registerEvents(effectDispatcher);
        Wynner.INSTANCE.getTicker().registerListener(effectDispatcher);
    }

    @Override
    protected void onDisabled() {
        super.onDisabled();
        effectDispatcher.cleanUp();
    }

    // TODO config: enable/disable individual effect renderers

}
