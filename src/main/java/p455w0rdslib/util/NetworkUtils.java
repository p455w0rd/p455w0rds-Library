package p455w0rdslib.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * @author p455w0rd
 *
 */
public class NetworkUtils {

	public static ItemStack readItemStack(ByteBuf dataIn) {
		PacketBuffer buf = new PacketBuffer(dataIn);
		try {
			NBTTagCompound nbt = buf.readNBTTagCompoundFromBuffer();
			ItemStack stack = ItemStack.loadItemStackFromNBT(nbt);
			stack.stackSize = buf.readInt();
			return stack;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void writeItemStack(ByteBuf dataOut, ItemStack itemStack) {
		PacketBuffer buf = new PacketBuffer(dataOut);
		NBTTagCompound nbt = new NBTTagCompound();
		itemStack.writeToNBT(nbt);
		try {
			buf.writeNBTTagCompoundToBuffer(nbt);
			buf.writeInt(itemStack.stackSize);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String readString(ByteBuf dataIn) {
		int s = dataIn.readInt();
		if (s == -1) {
			return null;
		}
		if (s == 0) {
			return "";
		}
		byte[] dst = new byte[s];
		dataIn.readBytes(dst);
		return new String(dst);
	}

	public static void writeString(ByteBuf dataOut, String str) {
		if (str == null) {
			dataOut.writeInt(-1);
			return;
		}
		byte[] bytes = str.getBytes();
		dataOut.writeInt(bytes.length);
		if (bytes.length > 0) {
			dataOut.writeBytes(bytes);
		}
	}

	public static BlockPos readPos(ByteBuf dataIn) {
		return new BlockPos(dataIn.readInt(), dataIn.readInt(), dataIn.readInt());
	}

	public static void writePos(ByteBuf dataOut, BlockPos pos) {
		dataOut.writeInt(pos.getX());
		dataOut.writeInt(pos.getY());
		dataOut.writeInt(pos.getZ());
	}

	public static <T extends Enum<?>> void writeEnum(ByteBuf buf, T value, T nullValue) {
		if (value == null) {
			buf.writeInt(nullValue.ordinal());
		}
		else {
			buf.writeInt(value.ordinal());
		}
	}

	public static <T extends Enum<?>> T readEnum(ByteBuf buf, T[] values) {
		return values[buf.readInt()];
	}

	public static void writeEnumCollection(ByteBuf buf, Collection<? extends Enum<?>> collection) {
		buf.writeInt(collection.size());
		for (Enum<?> type : collection) {
			buf.writeInt(type.ordinal());
		}
	}

	public static <T extends Enum<?>> void readEnumCollection(ByteBuf buf, Collection<T> collection, T[] values) {
		collection.clear();
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
			collection.add(values[buf.readInt()]);
		}
	}

	public static void writeFloat(ByteBuf buf, Float f) {
		if (f != null) {
			buf.writeBoolean(true);
			buf.writeFloat(f.floatValue());
		}
		else {
			buf.writeBoolean(false);
		}
	}

	public static Float readFloat(ByteBuf buf) {
		if (buf.readBoolean()) {
			return Float.valueOf(buf.readFloat());
		}
		return null;
	}

	public static void writeBlockPosList(ArrayList<BlockPos> posList, ByteBuf buf) {
		if (posList == null) {
			buf.writeShort(-1);
		}
		else {
			buf.writeShort(posList.size());
			for (int i = 0; i < posList.size(); ++i) {
				buf.writeLong(posList.get(i).toLong());
			}
		}
	}

	public static ArrayList<BlockPos> readBlockPosList(ByteBuf buf) {
		ArrayList<BlockPos> posList = new ArrayList<BlockPos>();
		int size = buf.readShort();
		for (int i = 0; i < size; ++i) {
			posList.add(new BlockPos(BlockPos.fromLong(buf.readLong())));
		}
		return posList;
	}

	public static MinecraftServer getServer() {
		return FMLCommonHandler.instance().getMinecraftServerInstance();
	}

}
