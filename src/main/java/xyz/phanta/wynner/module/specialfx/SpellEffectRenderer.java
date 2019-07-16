package xyz.phanta.wynner.module.specialfx;

public interface SpellEffectRenderer {

    default void init() {
        // NO-OP
    }

    boolean update();

    default void destroy() {
        // NO-OP
    }

    default void render(float partialTicks) {
        // NO-OP
    }

}
