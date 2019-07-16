package xyz.phanta.wynner.util;

import io.github.phantamanta44.libnine.util.render.TextureRegion;

public class ScaledIcon {

    private final TextureRegion icon;
    private final float xScale, yScale;

    public ScaledIcon(TextureRegion icon, float xScale, float yScale) {
        this.icon = icon;
        this.xScale = xScale;
        this.yScale = yScale;
    }

    public ScaledIcon(TextureRegion icon) {
        this(icon, 1F, 1F);
    }

    public TextureRegion getIcon() {
        return icon;
    }

    public float getHorizontalScale() {
        return xScale;
    }

    public float getVerticalScale() {
        return yScale;
    }

}
