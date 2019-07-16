package xyz.phanta.wynner.common.npc;

import xyz.phanta.wynner.common.waypoint.Waypoint;
import xyz.phanta.wynner.util.ScaledIcon;

import javax.annotation.Nullable;

public enum NpcServiceType {

    SHOP(18, 18),
    TRADE_MARKET(36, 18),
    POWDER_MASTER(0, 36),
    IDENTIFIER(18, 36),
    BLACKSMITH(36, 36),
    PROCESSING(0, 54);

    private final ScaledIcon mapIcon;

    NpcServiceType(int u, int v) {
        this.mapIcon = new ScaledIcon(Waypoint.DEF_ICONS.getRegion(u, v, 18, 18), 0.64F, 0.64F);
    }

    public ScaledIcon getMapIcon() {
        return mapIcon;
    }

    @Nullable
    public static NpcServiceType readName(String name) {
        if (name.endsWith("\u00a7r")) {
            if (name.startsWith("\u00a7d")) {
                String inner = name.substring(2, name.length() - 2);
                switch (inner) {
                    case "Powder Master":
                        return POWDER_MASTER;
                    case "Item Identifier":
                        return IDENTIFIER;
                    case "Blacksmith":
                        return BLACKSMITH;
                    default:
                        return SHOP;
                }
            } else if (name.startsWith("\u00a7a\u00a7l") && name.startsWith("Refinery", name.length() - 10)) {
                return PROCESSING;
            } else if (name.equals("\u00a7cTrade Market\u00a7r")) {
                return TRADE_MARKET;
            }
        }
        return null;
    }

}
