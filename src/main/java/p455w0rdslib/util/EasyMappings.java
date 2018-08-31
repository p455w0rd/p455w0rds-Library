package p455w0rdslib.util;

import java.io.IOException;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author p455w0rd
 *
 */
public class EasyMappings {

	@SideOnly(Side.CLIENT)
	public static Minecraft mc() {
		return Minecraft.getMinecraft();
	}

	@SideOnly(Side.CLIENT)
	public static EntityPlayerSP player() {
		return mc().player;
	}

	@SideOnly(Side.CLIENT)
	public static World world() {
		return mc().world;
	}

	public static World world(Entity entity) {
		return entity.world;
	}

	public static int slotPosX(Slot slot) {
		return slot.xPos;
	}

	public static int slotPosY(Slot slot) {
		return slot.yPos;
	}

	public static boolean spawn(World world, Entity entity) {
		return world.spawnEntity(entity);
	}

	public static NBTTagCompound readNBT(PacketBuffer buf) {
		try {
			return buf.readCompoundTag();
		}
		catch (IOException e) {
		}
		return null;
	}

	public static PacketBuffer writeNBT(@Nullable NBTTagCompound nbt, PacketBuffer buf) {
		return buf.writeCompoundTag(nbt);
	}

	public static String[] getNames(MinecraftServer server) {
		return server.getOnlinePlayerNames();
	}

	public static void message(Entity entity, ITextComponent message) {
		entity.sendMessage(message);
	}

}
