package xyz.phanta.wynner.common.waypoint;

import io.github.phantamanta44.libnine.util.render.TextureResource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import xyz.phanta.wynner.Wynner;
import xyz.phanta.wynner.util.ScaledIcon;

import javax.annotation.Nullable;

public interface Waypoint {

    TextureResource DEF_SPRITES = Wynner.INSTANCE.newTextureResource("textures/misc/map_sprites.png", 64, 32);
    TextureResource DEF_ICONS = Wynner.INSTANCE.newTextureResource("textures/misc/map_icons.png", 54, 72);

    String getName();

    BlockPos getPosition();

    TextFormatting getColour();

    @Nullable
    ScaledIcon getIcon();

    default boolean useTint() {
        return false;
    }

    class Impl implements Waypoint {

        private final String name;
        private final BlockPos position;
        private final TextFormatting colour;
        @Nullable
        private final ScaledIcon icon;

        public Impl(String name, BlockPos position, TextFormatting colour, @Nullable ScaledIcon icon) {
            this.name = name;
            this.position = position;
            this.colour = colour;
            this.icon = icon;
        }

        public Impl(String name, BlockPos position, TextFormatting colour) {
            this(name, position, colour, null);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public BlockPos getPosition() {
            return position;
        }

        @Override
        public TextFormatting getColour() {
            return colour;
        }

        @Nullable
        @Override
        public ScaledIcon getIcon() {
            return icon;
        }

    }

}
