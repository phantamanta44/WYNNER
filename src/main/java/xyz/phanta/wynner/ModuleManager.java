package xyz.phanta.wynner;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModuleManager {

    private final Map<String, WynnModule> modules = new HashMap<>();
    @Nullable
    private String loadingModule = null;

    void loadModules(Set<ASMDataTable.ASMData> asmData) {
        Wynner.LOGGER.info("Beginning module loading...");
        asmData.forEach(asm -> {
            loadingModule = (String)asm.getAnnotationInfo().get("value");
            if (modules.containsKey(loadingModule)) {
                Wynner.LOGGER.warn("Ignoring duplicate module \"{}\"", loadingModule);
            } else {
                try {
                    Wynner.LOGGER.info("Loading module \"{}\"...", loadingModule);
                    modules.put(loadingModule, (WynnModule)Class.forName(asm.getClassName()).newInstance());
                } catch (Exception e) {
                    Wynner.LOGGER.error("Loading failed!", e);
                }
            }
        });
        loadingModule = null;
        String badDisabled = Arrays.stream(WynnConfig.DISABLED_MODULES)
                .filter(m -> !modules.containsKey(m))
                .collect(Collectors.joining(", "));
        if (!badDisabled.isEmpty()) {
            Wynner.LOGGER.warn("Ignoring unknown modules in disable list: {}", badDisabled);
        }
    }

    public String getLoadingModuleId() {
        if (loadingModule == null) {
            throw new IllegalStateException("No module is currently loading!");
        }
        return loadingModule;
    }

    public Collection<WynnModule> getModules() {
        return modules.values();
    }

    public Stream<WynnModule> getEnabledModules() {
        return modules.values().stream().filter(WynnModule::isEnabled);
    }

    // TODO in-game module enable/disable
    // TODO per-module configuration

    void initModules() {
        Wynner.LOGGER.info("Initializing modules...");
        modules.forEach((id, mod) -> {
            Wynner.LOGGER.info("Initializing module \"{}\" ({})...", mod.getName(), id);
            mod.init();
            if (mod.isEnabled()) {
                mod.onEnabled();
            }
        });
    }

    void onInit(FMLInitializationEvent event) {
        modules.values().forEach(m -> m.onGameInit(event));
    }

    void onPostInit(FMLPostInitializationEvent event) {
        modules.values().forEach(m -> m.onGamePostInit(event));
    }

}
