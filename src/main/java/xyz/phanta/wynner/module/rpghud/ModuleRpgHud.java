package xyz.phanta.wynner.module.rpghud;

import net.minecraft.client.Minecraft;
import xyz.phanta.wynner.WynnModule;
import xyz.phanta.wynner.Wynner;

@WynnModule.Module(ModuleRpgHud.ID)
public class ModuleRpgHud extends WynnModule {

    public static final String ID = "rpg_hud";

    @Override
    public String getName() {
        return "RPG HUD";
    }

    @Override
    public String getDesc() {
        return "Moves HUD elements around and does fancy stuff with them.";
    }

    @Override
    protected void init() {
        Wynner.INSTANCE.getHudHandler().registerAll(
                this, new RpgResourceBarRenderer(), new RpgHudInterceptor(), new RpgHudInfoRenderer());
    }

    @Override
    protected void onDisabled() {
        super.onDisabled();
        Minecraft.getMinecraft().gameSettings.heldItemTooltips = true;
    }

}
