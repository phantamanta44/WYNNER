package xyz.phanta.wynner.common.player;

public enum SpellRune {

    NONE, LEFT, RIGHT;

    public static SpellRune parse(char rune) {
        switch (rune) {
            case 'L':
                return LEFT;
            case 'R':
                return RIGHT;
            default:
                return NONE;
        }
    }

}
