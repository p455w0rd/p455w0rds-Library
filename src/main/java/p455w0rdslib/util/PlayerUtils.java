package p455w0rdslib.util;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rdslib.LibRegistry;
import p455w0rdslib.P455w0rdsLib;

/**
 * @author p455w0rd
 *
 */
public class PlayerUtils {

	public static EntityPlayer getPlayer() {
		return EasyMappings.player();
	}

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
		if (sender instanceof EntityPlayer) {
			if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
				return isOPClient((EntityPlayer) sender);
			}
			else if (FMLCommonHandler.instance().getSide() == Side.SERVER) {
				return isOPServer((EntityPlayer) sender);
			}
		}
		if (sender instanceof TileEntityCommandBlock) {
			return true;
		}
		return sender.getName().equalsIgnoreCase("@") || sender.getName().equals("Server");
	}

	@SideOnly(Side.CLIENT)
	private static boolean isOPClient(EntityPlayer sender) {
		if (sender instanceof EntityPlayerSP) {
			EntityPlayerSP player = (EntityPlayerSP) sender;
			return player.capabilities.isCreativeMode || player.isSpectator();
		}
		return false;
	}

	@SideOnly(Side.SERVER)
	private static boolean isOPServer(EntityPlayer sender) {
		if (sender instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) sender.getEntityWorld().getPlayerEntityByName(sender.getName());
			if (player != null && player.getGameProfile() != null) {
				UserListOpsEntry userentry = player.mcServer.getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
				return userentry != null && userentry.getPermissionLevel() >= 4;
			}
		}
		return false;
	}

	public static List<UUID> getFullPlayerList() {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		List<UUID> uuidList = Lists.newArrayList();
		if (server != null) {
			PlayerProfileCache playerCache = server.getPlayerProfileCache();
			String[] usernames = EasyMappings.getNames(server);
			for (String username : usernames) {
				uuidList.add(playerCache.getGameProfileForUsername(username).getId());
			}
		}
		return uuidList;
	}

	public static ItemStack getPlayerSkull(String playerName) {
		ItemStack head = null;
		Map<String, ItemStack> skullCache = LibRegistry.getSkullRegistry();
		if (!skullCache.containsKey(playerName)) {
			head = new ItemStack(Items.SKULL, 1, 3);
			NBTTagCompound nametag = new NBTTagCompound();
			nametag.setString("SkullOwner", playerName);
			head.setTagCompound(nametag);
			skullCache.put(playerName, head);
		}
		return skullCache.get(playerName);
	}

}
