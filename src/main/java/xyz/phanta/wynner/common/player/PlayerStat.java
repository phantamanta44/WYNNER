package xyz.phanta.wynner.common.player;

import xyz.phanta.wynner.common.combat.Affinity;

public enum PlayerStat {

    STRENGTH(Affinity.EARTH, "Strength"),
    DEXTERITY(Affinity.THUNDER, "Dexterity"),
    INTELLIGENCE(Affinity.WATER, "Intelligence"),
    DEFENSE(Affinity.FIRE, "Defence"),
    AGILITY(Affinity.AIR, "Agility");

    public static final PlayerStat[] VALUES = values();

    public final Affinity element;
    public final String displayName;

    PlayerStat(Affinity element, String displayName) {
        this.element = element;
        this.displayName = displayName;
    }

}
