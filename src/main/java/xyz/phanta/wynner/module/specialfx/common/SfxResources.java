package xyz.phanta.wynner.module.specialfx.common;

import io.github.phantamanta44.libnine.util.render.TextureResource;
import net.minecraft.util.ResourceLocation;
import xyz.phanta.wynner.Wynner;

public class SfxResources {

    public static final TextureResource RING_XY_TEX = Wynner.INSTANCE
            .newTextureResource("textures/special_fx/ring_xy.png", 128, 128);
    public static final TextureResource RING_XZ_TEX = Wynner.INSTANCE
            .newTextureResource("textures/special_fx/ring_xz.png", 256, 256);
    public static final TextureResource SLASH_TEX = Wynner.INSTANCE
            .newTextureResource("textures/special_fx/slash.png", 256, 128);
    public static final TextureResource MINI_SLASH_TEX = Wynner.INSTANCE
            .newTextureResource("textures/special_fx/myriad.png", 256, 128);
    public static final TextureResource AURA_XY_TEX = Wynner.INSTANCE
            .newTextureResource("textures/special_fx/aura_xy.png", 256, 256);
    public static final TextureResource AURA_XZ_TEX = Wynner.INSTANCE
            .newTextureResource("textures/special_fx/aura_xz.png", 256, 256);
    public static final TextureResource BLADE_TEX = Wynner.INSTANCE
            .newTextureResource("textures/special_fx/blade.png", 128, 256);

    public static final ResourceLocation SLASH_SND = Wynner.INSTANCE.newResourceLocation("wynner.special_fx.slash");
    public static final ResourceLocation THUMP_SND = Wynner.INSTANCE.newResourceLocation("wynner.special_fx.thump");
    public static final ResourceLocation MINI_SLASH_SND = Wynner.INSTANCE.newResourceLocation("wynner.special_fx.slash");
    public static final ResourceLocation BLADE_SND = Wynner.INSTANCE.newResourceLocation("wynner.special_fx.tempest");

}
