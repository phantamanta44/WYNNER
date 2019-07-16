package xyz.phanta.wynner.common.player;

import javax.annotation.Nullable;

public enum PlayerClass {

    MAGE,
    ARCHER,
    WARRIOR,
    ASSASSIN;

    @Nullable
    public static PlayerClass parse(String name) {
        switch (name) {
            case "Mage/Dark Wizard":
                return MAGE;
            case "Archer/Hunter":
                return ARCHER;
            case "Warrior/Knight":
                return WARRIOR;
            case "Assassin/Ninja":
                return ASSASSIN;
        }
        return null;
    }

}
