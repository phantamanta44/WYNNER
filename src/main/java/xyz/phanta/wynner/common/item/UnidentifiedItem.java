package xyz.phanta.wynner.common.item;

import net.minecraft.item.ItemStack;
import xyz.phanta.wynner.util.WynnItems;

import javax.annotation.Nullable;

public class UnidentifiedItem {

    private final int baseLevel;
    private final ItemTier tier;

    public UnidentifiedItem(int baseLevel, ItemTier tier) {
        this.baseLevel = baseLevel;
        this.tier = tier;
    }

    public int getBaseLevel() {
        return baseLevel;
    }

    public ItemTier getTier() {
        return tier;
    }

    @Nullable
    public static UnidentifiedItem getFor(ItemStack stack) {
        String[] lore = WynnItems.getLore(stack).toArray(String[]::new);
        if (lore.length != 0 && lore[0].equals("\u00a77This item's powers have")) {
            try {
                int level = Integer.parseInt(lore[6].substring(19, lore[6].lastIndexOf('-')));
                ItemTier tier = ItemTier.parseColour(lore[7].charAt(13));
                if (tier != null) {
                    return new UnidentifiedItem(level, tier);
                }
            } catch (NumberFormatException ignored) {
                // NO-OP
            }
        }
        return null;
    }

}
