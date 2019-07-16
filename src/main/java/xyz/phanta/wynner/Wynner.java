package xyz.phanta.wynner;

import io.github.phantamanta44.libnine.Virtue;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import xyz.phanta.wynner.common.hud.IngameHudHandler;
import xyz.phanta.wynner.common.player.PlayerDataManager;
import xyz.phanta.wynner.common.quest.QuestDataManager;
import xyz.phanta.wynner.common.tick.ClientTickTracker;
import xyz.phanta.wynner.common.waypoint.Waypoint;
import xyz.phanta.wynner.common.waypoint.WaypointManager;
import xyz.phanta.wynner.common.waypoint.WaypointProvider;

import java.util.stream.Stream;

@SuppressWarnings("NullableProblems")
@Mod(modid = Wynner.MOD_ID, version = Wynner.VERSION, useMetadata = true)
public class Wynner extends Virtue {

    public static final String MOD_ID = "wynner";
    public static final String VERSION = "1.0.0";

    @SuppressWarnings("NullableProblems")
    @Mod.Instance(MOD_ID)
    public static Wynner INSTANCE;

    @SuppressWarnings("NullableProblems")
    public static Logger LOGGER;

    private final WynnModule dummyModule = new DummyModule();

    private ModuleManager moduleManager;
    private IngameHudHandler hudHandler;
    private WaypointManager waypoints;
    private PlayerDataManager playerData;
    private QuestDataManager questData;
    private ClientTickTracker clientTicks;

    public Wynner() {
        super(MOD_ID);
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        LOGGER.info("WYNNER!!!");
        moduleManager = new ModuleManager();
        MinecraftForge.EVENT_BUS.register(hudHandler = new IngameHudHandler(moduleManager));
        waypoints = new WaypointManager();
        MinecraftForge.EVENT_BUS.register(playerData = new PlayerDataManager());
        MinecraftForge.EVENT_BUS.register(questData = new QuestDataManager(dummyModule));
        MinecraftForge.EVENT_BUS.register(clientTicks = new ClientTickTracker());
        clientTicks.registerListener(playerData);
        waypoints.registerProvider(questData);
        moduleManager.loadModules(event.getAsmData().getAll("xyz.phanta.wynner.WynnModule$Module"));
        moduleManager.initModules();
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public IngameHudHandler getHudHandler() {
        return hudHandler;
    }

    public WaypointManager getWaypoints() {
        return waypoints;
    }

    public PlayerDataManager getPlayerData() {
        return playerData;
    }

    public QuestDataManager getQuestData() {
        return questData;
    }

    public ClientTickTracker getTicker() {
        return clientTicks;
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        moduleManager.onInit(event);
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        moduleManager.onPostInit(event);
    }

    private static class DummyModule extends WynnModule {

        DummyModule() {
            super("wynner");
        }

        @Override
        public String getName() {
            return "WYNNER!!!";
        }

        @Override
        public String getDesc() {
            return "The core module of Wynner.";
        }

        @Override
        protected void init() {
            // NO-OP
        }

    }

}
