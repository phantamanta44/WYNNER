package xyz.phanta.wynner.common.npc;

import xyz.phanta.wynner.util.ColourUtils;

import javax.annotation.Nullable;

public class NpcHealth {

    private final float health;

    public NpcHealth(float health) {
        this.health = health;
    }

    public float getHealth() {
        return health;
    }

    @Nullable
    public static NpcHealth readName(String name) {
        if (name.length() > 9 && name.charAt(0) == '\u00a7' && name.charAt(2) == '[') {
            String unf = ColourUtils.stripFormat(name);
            int lInd = unf.indexOf("|||||");
            if (lInd != -1) {
                try {
                    return new NpcHealth(Integer.parseInt(unf.substring(lInd + 5, unf.lastIndexOf("|||||"))));
                } catch (NumberFormatException ignored) {
                    // NO-OP
                }
            }
        }
        return null;
    }

}
