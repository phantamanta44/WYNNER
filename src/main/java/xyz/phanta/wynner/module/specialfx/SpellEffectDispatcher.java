package xyz.phanta.wynner.module.specialfx;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.phanta.wynner.common.tick.ClientTickListener;
import xyz.phanta.wynner.event.SpellCastEvent;
import xyz.phanta.wynner.module.specialfx.assassin.SpellMultiHitRenderer;
import xyz.phanta.wynner.module.specialfx.assassin.SpellSpinAttackRenderer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Supplier;

public class SpellEffectDispatcher implements ClientTickListener {

    private final Map<String, Supplier<SpellEffectRenderer>> providers = new HashMap<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final Collection<SpellEffectRenderer> activeRenderers = new HashSet<>();

    SpellEffectDispatcher() {
        // TODO finish
        // mage
//        providers.put("Heal", null);
//        providers.put("Teleport", null);
//        providers.put("Meteor", null);
//        providers.put("Ice Snake", null);

        // archer
//        providers.put("Arrow Storm", null);
//        providers.put("Escape", null);
//        providers.put("Bomb Arrow", null);
//        providers.put("Arrow Shield", null);

        // warrior
//        providers.put("Bash", null);
//        providers.put("Charge", null);
//        providers.put("Uppercut", null);
//        providers.put("War Scream", null);

        // assassin
        providers.put("Spin Attack", SpellSpinAttackRenderer::new);
//        providers.put("Vanish", null);
        providers.put("Multi Hit", SpellMultiHitRenderer::new);
//        providers.put("Smoke Bomb", null);
    }

    public void cleanUp() {
        activeRenderers.forEach(SpellEffectRenderer::destroy);
        activeRenderers.clear();
    }

    @Override
    public void onClientTick(long tick) {
        activeRenderers.removeIf(renderer -> !renderer.update());
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        float partialTicks = event.getPartialTicks();
        activeRenderers.forEach(renderer -> renderer.render(partialTicks));
    }

    @SubscribeEvent
    public void onSpellCast(SpellCastEvent event) {
        Supplier<SpellEffectRenderer> provider = providers.get(event.getSpellName());
        if (provider != null) {
            SpellEffectRenderer renderer = provider.get();
            activeRenderers.add(renderer);
            renderer.init();
        }
    }

}
