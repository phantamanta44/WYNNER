package xyz.phanta.wynner.event;

import net.minecraftforge.fml.common.eventhandler.Event;
import xyz.phanta.wynner.common.player.SpellRune;

public class SpellCastEvent extends Event {

    private final String spellName;
    private final int manaCost;
    private final SpellRune spellA, spellB, spellC;

    public SpellCastEvent(String spellName, int manaCost, SpellRune spellA, SpellRune spellB, SpellRune spellC) {
        this.spellName = spellName;
        this.manaCost = manaCost;
        this.spellA = spellA;
        this.spellB = spellB;
        this.spellC = spellC;
    }

    public String getSpellName() {
        return spellName;
    }

    public int getManaCost() {
        return manaCost;
    }

    public SpellRune getSpellA() {
        return spellA;
    }

    public SpellRune getSpellB() {
        return spellB;
    }

    public SpellRune getSpellC() {
        return spellC;
    }

}
