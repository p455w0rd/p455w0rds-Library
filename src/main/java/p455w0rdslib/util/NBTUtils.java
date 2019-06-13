package p455w0rdslib.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

/**
 * @author p455w0rd
 *
 */
public class NBTUtils {

	public static boolean hasInt(final NBTTagCompound compound, final String key) {
		return compound.hasKey(key, Constants.NBT.TAG_INT);
	}

	public static int getInt(final NBTTagCompound compound, final String key) {
		return compound.getInteger(key);
	}

	public static boolean hasNBTTagList(final NBTTagCompound compound, final String key) {
		return compound.hasKey(key, Constants.NBT.TAG_LIST);
	}

	public static NBTTagList getTagList(final NBTTagCompound compound, final String key, final int type) {
		return compound.getTagList(key, type);
	}

	public static NBTTagList getNBTTagList(final NBTTagCompound compound, final String key) {
		return getTagList(compound, key, Constants.NBT.TAG_COMPOUND);
	}

}
