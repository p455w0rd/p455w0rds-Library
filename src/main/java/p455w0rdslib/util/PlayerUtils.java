package p455w0rdslib.util;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.mojang.authlib.GameProfile;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import p455w0rdslib.P455w0rdsLib;

/**
 * @author p455w0rd
 *
 */
public class PlayerUtils {

	public static EntityPlayer getPlayerByContext(MessageContext ctx) {
		return P455w0rdsLib.PROXY.getPlayer(ctx);
	}

	public static void writeProfileToNBT(GameProfile profile, NBTTagCompound tag) {
		tag.setString("Name", profile.getName());
		UUID id = profile.getId();
		if (id != null) {
			tag.setLong("UUIDL", id.getLeastSignificantBits());
			tag.setLong("UUIDU", id.getMostSignificantBits());
		}
	}

	public static GameProfile profileFromNBT(NBTTagCompound tag) {
		String name = tag.getString("Name");
		UUID uuid = null;
		if (tag.hasKey("UUIDL")) {
			uuid = new UUID(tag.getLong("UUIDU"), tag.getLong("UUIDL"));
		}
		else if (StringUtils.isBlank(name)) {
			return null;
		}
		return new GameProfile(uuid, name);
	}

	public static NBTTagCompound proifleToNBT(GameProfile profile) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("Name", profile.getName());
		UUID id = profile.getId();
		if (id != null) {
			tag.setLong("UUIDL", id.getLeastSignificantBits());
			tag.setLong("UUIDU", id.getMostSignificantBits());
		}
		return tag;
	}

	public static EntityPlayer getPlayerFromWorld(World world, UUID player) {
		if (player == null) {
			return null;
		}
		List<EntityPlayer> players = world.playerEntities;
		for (EntityPlayer entityPlayer : players) {
			if (entityPlayer.getUniqueID().compareTo(player) != 0) {
				continue;
			}
			return entityPlayer;
		}
		return null;
	}

	public static UUID getPlayerID(GameProfile profile) {
		return profile.getId();
	}

	public static GameProfile getPlayerProfile(GameProfile profile) {
		return profile;
	}

	public static boolean isOp(ICommandSender sender) {
		if (!ProxiedUtils.isSMP()) {
			return true;
		}
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			if (sender instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) sender;
				return player.capabilities.isCreativeMode || player.isSpectator();
			}
		}
		else if (FMLCommonHandler.instance().getSide() == Side.SERVER) {
			if (sender instanceof EntityPlayerMP) {
				EntityPlayer player = sender.getEntityWorld().getPlayerEntityByName(sender.getName());
				if (player != null && player.getGameProfile() != null) {
					UserListOpsEntry userentry = ((EntityPlayerMP) player).mcServer.getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
					return userentry != null && userentry.getPermissionLevel() >= 4;
				}
			}
		}
		if (sender instanceof TileEntityCommandBlock) {
			return true;
		}
		return sender.getName().equalsIgnoreCase("@") || sender.getName().equals("Server");
	}

}
