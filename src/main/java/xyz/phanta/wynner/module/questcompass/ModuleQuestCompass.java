package xyz.phanta.wynner.module.questcompass;

import xyz.phanta.wynner.WynnModule;
import xyz.phanta.wynner.Wynner;

@WynnModule.Module(ModuleQuestCompass.ID)
public class ModuleQuestCompass extends WynnModule {

    public static final String ID = "quest_compass";

    @Override
    public String getName() {
        return "Quest Compass";
    }

    @Override
    public String getDesc() {
        return "Provides a HUD for easier quest tracking.";
    }

    @Override
    protected void init() {
        Wynner.INSTANCE.getHudHandler().registerRenderer(this, new CompassOverlayRenderer());
    }

}
