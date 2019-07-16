package xyz.phanta.wynner.common.quest;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.phanta.wynner.WynnModule;
import xyz.phanta.wynner.common.waypoint.Waypoint;
import xyz.phanta.wynner.common.waypoint.WaypointProvider;
import xyz.phanta.wynner.util.ScaledIcon;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class QuestDataManager implements WaypointProvider {

    private static final Pattern COORD_MATCHER = Pattern.compile("\\[(-?\\d+),\\s*(-?\\d+),\\s*(-?\\d+)]");
    private static final ScaledIcon WAYPOINT_TEX = new ScaledIcon(Waypoint.DEF_SPRITES.getRegion(48, 0, 16, 16));

    private final Minecraft mc = Minecraft.getMinecraft();
    private final WynnModule module;
    @Nullable
    private BlockPos trackedPos;
    private final TrackedWaypoint trackedPosWaypoint = new TrackedWaypoint();
    private boolean expectingQuestTrackData = false;

    public QuestDataManager(WynnModule module) {
        this.module = module;
    }

    @Nullable
    public BlockPos getTrackedPosition() {
        return canQuestTrack() ? trackedPos : null;
    }

    @Override
    public WynnModule getProvidingModule() {
        return module;
    }

    @Override
    public Stream<? extends Waypoint> getProvidedWaypoints() {
        return canQuestTrack() ? Stream.of(trackedPosWaypoint) : Stream.empty();
    }

    private boolean canQuestTrack() {
        if (trackedPos == null) {
            return false;
        } else if (mc.player.getHeldItemMainhand().getItem() == Items.WRITTEN_BOOK) {
            return true;
        } else {
            trackedPos = null;
            return false;
        }
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        String msg = event.getMessage().getFormattedText();
        if (msg.startsWith("\u00a7")) {
            if (expectingQuestTrackData) {
                if (msg.startsWith("c[Click to Stop Tracking]", 1)) {
                    expectingQuestTrackData = false;
                } else if (msg.charAt(1) == '7') {
                    Matcher m = COORD_MATCHER.matcher(msg);
                    if (m.find()) {
                        try {
                            trackedPos = new BlockPos(
                                    Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
                        } catch (NumberFormatException ignored) {
                            // NO-OP
                        }
                    }
                }
            } else if (msg.charAt(1) == '6' && msg.endsWith("\u00a7e[Tracking]\u00a7r")) {
                expectingQuestTrackData = true;
            }
        }
    }

    private class TrackedWaypoint implements Waypoint {

        @Override
        public String getName() {
            return "Quest Tracker";
        }

        @Override
        public BlockPos getPosition() {
            return Objects.requireNonNull(trackedPos);
        }

        @Override
        public TextFormatting getColour() {
            return TextFormatting.GOLD;
        }

        @Nullable
        @Override
        public ScaledIcon getIcon() {
            return WAYPOINT_TEX;
        }

        @Override
        public boolean useTint() {
            return true;
        }

    }

}
