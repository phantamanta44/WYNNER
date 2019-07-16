package xyz.phanta.wynner.common.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import xyz.phanta.wynner.util.WynnItems;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public enum ItemTier {

    NORMAL(TextFormatting.WHITE),
    UNIQUE(TextFormatting.YELLOW),
    RARE(TextFormatting.LIGHT_PURPLE),
    LEGENDARY(TextFormatting.AQUA),
    MYTHIC(TextFormatting.DARK_PURPLE),
    SET(TextFormatting.GREEN);

    public final TextFormatting colour;

    ItemTier(TextFormatting colour) {
        this.colour = colour;
    }

    public static Optional<ItemTier> getFor(ItemStack stack) {
        return WynnItems.getLore(stack).map(ItemTier::parseLoreLine).filter(Objects::nonNull).findAny();
    }

    @Nullable
    public static ItemTier parseLoreLine(String loreLine) {
        if (loreLine.startsWith("\u00a7fNormal Item")) {
            return NORMAL;
        } else if (loreLine.startsWith("\u00a7eUnique Item")) {
            return UNIQUE;
        } else if (loreLine.startsWith("\u00a7dRare Item")) {
            return RARE;
        } else if (loreLine.startsWith("\u00a7aSet Item")) {
            return SET;
        } else if (loreLine.startsWith("\u00a7bLegendary Item")) {
            return LEGENDARY;
        } else if (loreLine.startsWith("\u00a75Mythic Item")) {
            return MYTHIC;
        }
        return null;
    }

    @Nullable
    public static ItemTier parseColour(char colour) {
        switch (colour) {
            case 'f':
                return NORMAL;
            case 'e':
                return UNIQUE;
            case 'd':
                return RARE;
            case 'b':
                return LEGENDARY;
            case '5':
                return MYTHIC;
            case 'a':
                return SET;
        }
        return null;
    }

}
