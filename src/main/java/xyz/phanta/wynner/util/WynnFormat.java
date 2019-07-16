package xyz.phanta.wynner.util;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class WynnFormat {

    public static void prettyPrintNbt(TabulatedStringBuffer buf, NBTBase tag) {
        switch (tag.getId()) {
            case Constants.NBT.TAG_BYTE:
                buf.append(String.format("%2xb", ((NBTTagByte)tag).getByte()));
                break;
            case Constants.NBT.TAG_SHORT:
                buf.append(((NBTTagShort)tag).getShort() + "s");
                break;
            case Constants.NBT.TAG_INT:
                buf.append(((NBTTagInt)tag).getInt() + "i");
                break;
            case Constants.NBT.TAG_LONG:
                buf.append(((NBTTagLong)tag).getLong() + "j");
                break;
            case Constants.NBT.TAG_FLOAT:
                buf.append(String.format("%.4ff", ((NBTTagFloat)tag).getFloat()));
                break;
            case Constants.NBT.TAG_DOUBLE:
                buf.append(String.format("%.4fd", ((NBTTagDouble)tag).getDouble()));
                break;
            case Constants.NBT.TAG_BYTE_ARRAY:
                buf.append("[");
                for (byte n : ((NBTTagByteArray)tag).getByteArray()) {
                    buf.append(String.format("%2xb", n));
                }
                buf.append("]");
                break;
            case Constants.NBT.TAG_INT_ARRAY:
                buf.append("[");
                for (int n : ((NBTTagIntArray)tag).getIntArray()) {
                    buf.append(n + "i");
                }
                buf.append("]");
                break;
            case Constants.NBT.TAG_LONG_ARRAY:
                buf.append(tag.toString()); // lol wtf
                break;
            case Constants.NBT.TAG_STRING:
                buf.append(((NBTTagString)tag).getString());
                break;
            case Constants.NBT.TAG_LIST:
                buf.append("[").indent();
                ((NBTTagList)tag).forEach(subTag -> {
                    buf.newLine();
                    prettyPrintNbt(buf, subTag);
                });
                buf.outdent().appendLine("]");
                break;
            case Constants.NBT.TAG_COMPOUND:
                buf.append("{").indent();
                NBTTagCompound compound = (NBTTagCompound)tag;
                compound.getKeySet().forEach(key -> {
                    buf.appendLine(key).append(": ");
                    prettyPrintNbt(buf, compound.getTag(key));
                });
                buf.outdent().appendLine("}");
                break;
        }
    }

}
