package xyz.phanta.wynner;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class WynnModule {

    private static boolean dummyCreated = false;

    private final String moduleId;
    private boolean enabled;
    private final Set<Object> eventHandlers = new HashSet<>();

    protected final Logger logger;

    public WynnModule() {
        ModuleManager modMan = Wynner.INSTANCE.getModuleManager();
        this.moduleId = modMan.getLoadingModuleId();
        this.enabled = Arrays.stream(WynnConfig.DISABLED_MODULES).noneMatch(moduleId::equals);
        this.logger = LogManager.getLogger(Wynner.LOGGER.getName() + "/" + moduleId);
    }

    WynnModule(String moduleId) {
        if (dummyCreated) {
            throw new IllegalStateException("Already created dummy module!");
        }
        dummyCreated = true;
        this.moduleId = moduleId;
        this.enabled = true;
        this.logger = Wynner.LOGGER;
    }

    // properties

    public String getModuleId() {
        return moduleId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public abstract String getName();

    public abstract String getDesc();

    // internal

    void setEnabled(boolean enabled) {
        if (enabled) {
            if (!this.enabled) {
                this.enabled = true;
                onEnabled();
            }
        } else if (this.enabled) {
            this.enabled = false;
            onDisabled();
        }
    }

    // listeners

    protected abstract void init();

    protected void onEnabled() {
        eventHandlers.forEach(MinecraftForge.EVENT_BUS::register);
    }

    protected void onDisabled() {
        eventHandlers.forEach(MinecraftForge.EVENT_BUS::unregister);
    }

    protected void onGameInit(FMLInitializationEvent event) {
        // NO-OP
    }

    protected void onGamePostInit(FMLPostInitializationEvent event) {
        // NO-OP
    }

    // api

    protected void registerEvents(Object handler, boolean ignoreDisable) {
        if (ignoreDisable) {
            MinecraftForge.EVENT_BUS.register(handler);
        } else {
            eventHandlers.add(handler);
        }
    }

    protected void registerEvents(Object handler) {
        registerEvents(handler, false);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Module {

        String value();

    }

}
