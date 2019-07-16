package xyz.phanta.wynner.common.hud;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.phanta.wynner.ModuleManager;
import xyz.phanta.wynner.WynnModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class IngameHudHandler {

    private final ModuleManager moduleManager;
    private final Multimap<String, HudRenderer> renderers = Multimaps.newListMultimap(new HashMap<>(), ArrayList::new);

    public IngameHudHandler(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    public void registerRenderer(WynnModule module, HudRenderer renderer) {
        renderers.put(module.getModuleId(), renderer);
    }

    public void registerAll(WynnModule module, HudRenderer... renderers) {
        for (HudRenderer renderer : renderers) {
            registerRenderer(module, renderer);
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onPreHudRender(RenderGameOverlayEvent.Pre event) {
        delegateRender(event, false);
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onPostHudRender(RenderGameOverlayEvent.Post event) {
        delegateRender(event, true);
    }

    private void delegateRender(RenderGameOverlayEvent event, boolean post) {
        RenderGameOverlayEvent.ElementType type = event.getType();
        AtomicBoolean shouldCancel = new AtomicBoolean(false);
        moduleManager.getEnabledModules()
                .flatMap(m -> renderers.get(m.getModuleId()).stream())
                .filter(r -> r.renderIn(type) && r.renderAfter() == post)
                .forEach(renderer -> {
                    if (!renderer.doRender(type, event.getResolution(), event.getPartialTicks())) {
                        shouldCancel.set(true);
                    }
                });
        if (shouldCancel.get()) {
            event.setCanceled(true);
        }
    }

}
