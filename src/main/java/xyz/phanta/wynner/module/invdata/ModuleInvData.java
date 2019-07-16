package xyz.phanta.wynner.module.invdata;

import xyz.phanta.wynner.WynnModule;

@WynnModule.Module(ModuleInvData.ID)
public class ModuleInvData extends WynnModule {

    public static final String ID = "inv_data";

    @Override
    public String getName() {
        return "Inventory Data";
    }

    @Override
    public String getDesc() {
        return "Displays useful information in inventories.";
    }

    @Override
    protected void init() {
        registerEvents(new InventoryDataOverlayRenderer());
    }

}
