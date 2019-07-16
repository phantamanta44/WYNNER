package xyz.phanta.wynner.common.npc;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;

public enum NpcType {

    GENERIC(TextFormatting.WHITE, false),
    QUEST(TextFormatting.DARK_GREEN, false),
    MERCHANT(TextFormatting.LIGHT_PURPLE, false),
    GUARD(TextFormatting.AQUA, true),
    PASSIVE(TextFormatting.GREEN, true),
    NEUTRAL(TextFormatting.YELLOW, true),
    HOSTILE(TextFormatting.RED, true);

    public final TextFormatting colour;
    public final boolean canHarm;

    NpcType(TextFormatting colour, boolean canHarm) {
        this.colour = colour;
        this.canHarm = canHarm;
    }

    @Nullable
    public static NpcType readName(String name) {
        if (!name.endsWith("\u00a7r")) {
            return null;
        }
        if (name.startsWith("\u00a72\u00a7f")) {
            return GENERIC;
        }
        char nameCol = name.charAt(1);
        switch (nameCol) {
            case 'd':
                return MERCHANT;
            case '2':
                return QUEST;
        }
        if (!name.contains("\u00a76 [Lv.")) {
            return null;
        }
        switch (nameCol) {
            case 'b':
                return GUARD;
            case 'a':
                return PASSIVE;
            case 'e':
                return NEUTRAL;
            case 'c':
                return HOSTILE;
            default:
                return null;
        }
    }

    public static boolean canTarget(Entity entity) {
        String name = entity.getDisplayName().getFormattedText();
        NpcType type = NpcType.readName(name);
        if (type != null && type.canHarm) {
            return true;
        }
        return NpcHealth.readName(name) != null;
    }

}
