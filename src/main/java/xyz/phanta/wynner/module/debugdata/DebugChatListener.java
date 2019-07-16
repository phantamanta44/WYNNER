package xyz.phanta.wynner.module.debugdata;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.IChatListener;
import net.minecraft.client.gui.chat.NormalChatListener;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class DebugChatListener implements IChatListener {

    private final Logger logger;
    private final Minecraft mc = Minecraft.getMinecraft();
    private final IChatListener fallbackListener = new NormalChatListener(mc);
    private boolean hooked = false;
    private boolean enabled;

    DebugChatListener(Logger logger, boolean enabled) {
        this.logger = logger;
        this.enabled = enabled;
    }

    @Override
    public void say(ChatType type, ITextComponent message) {
        if (enabled) {
            mc.ingameGUI.getChatGUI().setChatLine(message, 0, this.mc.ingameGUI.getUpdateCounter(), false);
            logger.info("CHAT: {}", message.getFormattedText().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n"));
        } else {
            fallbackListener.say(type, message);
        }
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    void register() {
        if (!hooked) {
            logger.info("Injecting debug chat hook...");
            Map<ChatType, List<IChatListener>> listenerMap;
            listenerMap = mc.ingameGUI.chatListeners;
            listenerMap.forEach((type, listeners) -> {
                if (listeners.removeIf(l -> l instanceof NormalChatListener)) {
                    listeners.add(this);
                }
            });
            hooked = true;
        }
    }

}
