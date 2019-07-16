package xyz.phanta.wynner.util;

import io.github.phantamanta44.libnine.util.render.TextureRegion;
import io.github.phantamanta44.libnine.util.render.TextureResource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class SpriteUtils implements ISelectiveResourceReloadListener {

    private static final int ATLAS_DIM = 4096;
    private static final SpriteUtils INSTANCE = new SpriteUtils();

    private static TextureRegion getRegion(ResourceLocation atlas, TextureAtlasSprite sprite) {
        int u = (int)(sprite.getMinU() * ATLAS_DIM), v = (int)(sprite.getMinV() * ATLAS_DIM);
        return INSTANCE.getResource(atlas).getRegion(
                u, v, (int)(sprite.getMaxU() * ATLAS_DIM - u), (int)(sprite.getMaxV() * ATLAS_DIM - v));
    }

    public static TextureRegion getTerrainRegion(TextureAtlasSprite sprite) {
        return getRegion(TextureMap.LOCATION_BLOCKS_TEXTURE, sprite);
    }

    private final Map<ResourceLocation, TextureResource> atlasCache = new HashMap<>();

    private SpriteUtils() {
        // NO-OP
    }

    private TextureResource getResource(ResourceLocation atlas) {
        return atlasCache.computeIfAbsent(atlas, k -> new TextureResource(k, ATLAS_DIM, ATLAS_DIM));
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        if (resourcePredicate.test(VanillaResourceType.TEXTURES)) {
            atlasCache.clear();
        }
    }

}
