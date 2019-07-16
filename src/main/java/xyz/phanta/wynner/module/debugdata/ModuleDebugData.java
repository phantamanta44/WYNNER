package xyz.phanta.wynner.module.debugdata;

import xyz.phanta.wynner.WynnModule;

@WynnModule.Module(ModuleDebugData.ID)
public class ModuleDebugData extends WynnModule {

    public static final String ID = "debug_data";

    private final DebugChatListener chatListener;

    public ModuleDebugData() {
        this.chatListener = new DebugChatListener(logger, isEnabled());
    }

    @Override
    public String getName() {
        return "Debug Data";
    }

    @Override
    public String getDesc() {
        return "Displays debug data in-game for reverse engineering purposes.";
    }

    @Override
    protected void init() {
        registerEvents(new DebugDataHandler(chatListener));
    }

    @Override
    protected void onEnabled() {
        super.onEnabled();
        chatListener.setEnabled(true);
    }

    @Override
    protected void onDisabled() {
        super.onDisabled();
        chatListener.setEnabled(false);
    }

}
