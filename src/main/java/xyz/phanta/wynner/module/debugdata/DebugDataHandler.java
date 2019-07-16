package xyz.phanta.wynner.module.debugdata;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import xyz.phanta.wynner.util.TabulatedStringBuffer;
import xyz.phanta.wynner.util.WynnFormat;

import java.util.Objects;

public class DebugDataHandler {

    private final DebugChatListener chatListener;
    private final KeyBinding keySneak = Minecraft.getMinecraft().gameSettings.keyBindSneak;

    DebugDataHandler(DebugChatListener chatListener) {
        this.chatListener = chatListener;
    }

    @SubscribeEvent
    public void onGui(GuiScreenEvent.InitGuiEvent event) {
        if (event.getGui() instanceof GuiMainMenu) {
            chatListener.register();
        }
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().hasTagCompound() && Keyboard.isKeyDown(keySneak.getKeyCode())) {
            event.getToolTip().clear();
            TabulatedStringBuffer buf = new TabulatedStringBuffer();
            WynnFormat.prettyPrintNbt(buf, Objects.requireNonNull(event.getItemStack().getTagCompound()));
            buf.getLines().map(l -> TextFormatting.WHITE + l.replace('\u00a7', '&')).forEach(event.getToolTip()::add);
        }
    }

}
