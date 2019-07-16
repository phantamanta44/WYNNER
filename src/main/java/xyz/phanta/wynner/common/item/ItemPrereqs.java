package xyz.phanta.wynner.common.item;

import net.minecraft.item.ItemStack;
import xyz.phanta.wynner.common.player.PlayerClass;
import xyz.phanta.wynner.common.player.PlayerStat;
import xyz.phanta.wynner.util.WynnItems;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Objects;

public class ItemPrereqs {

    @Nullable
    private final PlayerClass classReq;
    private final EnumMap<PlayerStat, Integer> statReqs;
    private final int levelReq;

    public ItemPrereqs(@Nullable PlayerClass classReq, EnumMap<PlayerStat, Integer> statReqs, int levelReq) {
        this.classReq = classReq;
        this.statReqs = statReqs;
        this.levelReq = levelReq;
    }

    public boolean hasClassReq() {
        return classReq != null;
    }

    public PlayerClass getClassReq() {
        return Objects.requireNonNull(classReq);
    }

    public boolean hasStatReq(PlayerStat stat) {
        return statReqs.containsKey(stat);
    }

    public int getStatReqs(PlayerStat stat) {
        return statReqs.get(stat);
    }

    public boolean hasLevelReq() {
        return levelReq > 0;
    }

    public int getLevelReq() {
        return levelReq;
    }

    public static class Builder {

        @Nullable
        private PlayerClass classReq = null;
        private final EnumMap<PlayerStat, Integer> statReqs = new EnumMap<>(PlayerStat.class);
        private int levelReq = 0;

        public Builder withClassReq(PlayerClass classReq) {
            this.classReq = classReq;
            return this;
        }

        public Builder withStatReq(PlayerStat stat, int statReq) {
            this.statReqs.put(stat, statReq);
            return this;
        }

        public Builder withLevelReq(int levelReq) {
            this.levelReq = levelReq;
            return this;
        }

        public ItemPrereqs build() {
            return new ItemPrereqs(classReq, statReqs, levelReq);
        }

    }

    public static ItemPrereqs getFor(ItemStack stack) {
        Builder builder = new Builder();
        WynnItems.getLore(stack).forEach(line -> {
            try {
                if (line.startsWith("\u00a77 ", 3)) {
                    if (line.startsWith("Class Req: ", 6)) {
                        PlayerClass classReq = PlayerClass.parse(line.substring(17));
                        if (classReq != null) {
                            builder.withClassReq(classReq);
                        }
                    } else if (line.startsWith("Combat Lv. Min: ", 6)) {
                        builder.withLevelReq(Integer.parseInt(line.substring(22)));
                    } else {
                        for (PlayerStat stat : PlayerStat.VALUES) {
                            if (line.startsWith(stat.displayName + " Min: ", 6)) {
                                builder.withStatReq(stat, Integer.parseInt(line.substring(stat.displayName.length() + 6)));
                            }
                        }
                    }
                }
            } catch (NumberFormatException ignored) {
                // NO-OP
            }
        });
        return builder.build();
    }

}
