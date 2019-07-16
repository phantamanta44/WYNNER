package xyz.phanta.wynner.common.tick;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongSupplier;

public class ClientTickTracker implements LongSupplier {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final List<ClientTickListener> listeners = new ArrayList<>();
    private final List<ClientTickListener> globalListeners = new ArrayList<>();
    private long tick;

    @Override
    public long getAsLong() {
        return tick;
    }

    public void registerListener(ClientTickListener listener) {
        listeners.add(listener);
        if (!listener.onlyTicksInGame()) {
            globalListeners.add(listener);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ++tick;
            if (mc.player == null) {
                globalListeners.forEach(l -> l.onClientTick(tick));
            } else {
                listeners.forEach(l -> l.onClientTick(tick));
            }
        }
    }

}
