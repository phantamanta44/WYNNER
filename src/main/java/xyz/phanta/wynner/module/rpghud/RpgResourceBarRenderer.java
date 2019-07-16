package xyz.phanta.wynner.module.rpghud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.FoodStats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import xyz.phanta.wynner.Wynner;
import xyz.phanta.wynner.common.hud.HudRenderer;
import xyz.phanta.wynner.common.player.PlayerDataManager;

public class RpgResourceBarRenderer implements HudRenderer {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final RpgResourceBar hpRender = new RpgResourceBar(mc, 0, 0.298F, 0.588F, 0.22F);
    private final RpgResourceBar manaRender = new RpgResourceBar(mc, 14, 0.008F, 0.475F, 0.918F);

    @Override
    public boolean renderIn(RenderGameOverlayEvent.ElementType type) {
        return type == RenderGameOverlayEvent.ElementType.HEALTH;
    }

    @Override
    public boolean doRender(RenderGameOverlayEvent.ElementType type, ScaledResolution res, float partialTicks) {
        PlayerDataManager playerData = Wynner.INSTANCE.getPlayerData();
        hpRender.renderResourceBar(res, playerData.getHealth(), playerData.getMaxHealth());
        FoodStats food = mc.player.getFoodStats();
        manaRender.renderResourceBar(res, food.getFoodLevel(), 20F);
        return false;
    }

}
