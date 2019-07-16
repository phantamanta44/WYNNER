package xyz.phanta.wynner.common.tick;

public interface ClientTickListener {

    default boolean onlyTicksInGame() {
        return true;
    }

    void onClientTick(long tick);

}
