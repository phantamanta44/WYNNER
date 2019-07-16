package xyz.phanta.wynner.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

import static net.minecraft.util.text.TextFormatting.*;

public class ColourUtils {

    public static int getTextColour(TextFormatting format) {
        int index = format.getColorIndex();
        return index == -1 ? 0xFFFFFF : Minecraft.getMinecraft().fontRenderer.colorCode[index];
    }

    public static float getComponent(int colour, int compIndex) {
        return ((colour >> (compIndex * 8)) & 0xFF) / 255F;
    }

    public static void setGlColour(TextFormatting colour, float alpha) {
        int n = ColourUtils.getTextColour(colour);
        GlStateManager.color(getComponent(n, 2), getComponent(n, 1), getComponent(n, 0), alpha);
    }

    public static void setGlColour(TextFormatting colour) {
        setGlColour(colour, 1F);
    }

    public static TextFormatting getDarker(TextFormatting colour) {
        switch (colour) {
            case GRAY:
                return DARK_GRAY;
            case BLUE:
                return DARK_BLUE;
            case GREEN:
                return DARK_GREEN;
            case AQUA:
                return DARK_AQUA;
            case RED:
                return DARK_RED;
            case LIGHT_PURPLE:
                return DARK_PURPLE;
            case YELLOW:
                return GOLD;
            case WHITE:
                return GRAY;
        }
        return colour;
    }

    public static String stripFormat(String str) {
        int index;
        while ((index = str.indexOf('\u00a7')) != -1) {
            str = str.substring(0, index) + str.substring(index + 2);
        }
        return str;
    }

}
