package xyz.phanta.wynner.common.combat;

import net.minecraft.util.text.TextFormatting;

public enum Affinity {

    NEUTRAL(TextFormatting.GOLD),
    EARTH(TextFormatting.DARK_GREEN),
    THUNDER(TextFormatting.YELLOW),
    WATER(TextFormatting.AQUA),
    FIRE(TextFormatting.RED),
    AIR(TextFormatting.WHITE);

    public final TextFormatting colour;

    Affinity(TextFormatting colour) {
        this.colour = colour;
    }

}
