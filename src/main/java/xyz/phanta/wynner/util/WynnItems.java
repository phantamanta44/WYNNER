package xyz.phanta.wynner.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class WynnItems {

    public static Stream<String> getLore(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound tag = Objects.requireNonNull(stack.getTagCompound());
            if (tag.hasKey("display", Constants.NBT.TAG_COMPOUND)) {
                tag = tag.getCompoundTag("display");
                if (tag.hasKey("Lore", Constants.NBT.TAG_LIST)) {
                    return StreamSupport.stream(tag.getTagList("Lore", Constants.NBT.TAG_STRING).spliterator(), false)
                            .map(t -> ((NBTTagString)t).getString());
                }
            }
        }
        return Stream.empty();
    }

}
