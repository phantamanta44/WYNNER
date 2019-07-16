package xyz.phanta.wynner;

import net.minecraftforge.common.config.Config;

import java.util.ArrayList;
import java.util.List;

@Config(modid = Wynner.MOD_ID)
public class WynnConfig {

    @Config.Name("disabled_modules")
    @Config.Comment("WYNNER!!! module IDs to be disabled.")
    public static String[] DISABLED_MODULES = new String[0];

}
