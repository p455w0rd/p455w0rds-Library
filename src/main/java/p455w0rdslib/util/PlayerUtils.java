package p455w0rdslib.util;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
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

	public static EntityPlayer getPlayerByContext(final MessageContext ctx) {
		return P455w0rdsLib.PROXY.getPlayer(ctx);
	}

	public static void writeProfileToNBT(final GameProfile profile, final NBTTagCompound tag) {
		tag.setString("Name", profile.getName());
		final UUID id = profile.getId();
		if (id != null) {
			tag.setLong("UUIDL", id.getLeastSignificantBits());
			tag.setLong("UUIDU", id.getMostSignificantBits());
		}
	}

	public static GameProfile profileFromNBT(final NBTTagCompound tag) {
		final String name = tag.getString("Name");
		UUID uuid = null;
		if (tag.hasKey("UUIDL")) {
			uuid = new UUID(tag.getLong("UUIDU"), tag.getLong("UUIDL"));
		}
		else if (StringUtils.isBlank(name)) {
			return null;
		}
		return new GameProfile(uuid, name);
	}

	public static NBTTagCompound proifleToNBT(final GameProfile profile) {
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setString("Name", profile.getName());
		final UUID id = profile.getId();
		if (id != null) {
			tag.setLong("UUIDL", id.getLeastSignificantBits());
			tag.setLong("UUIDU", id.getMostSignificantBits());
		}
		return tag;
	}

	public static EntityPlayer getPlayerFromWorld(final World world, final UUID player) {
		if (player == null) {
			return null;
		}
		final List<EntityPlayer> players = world.playerEntities;
		for (final EntityPlayer entityPlayer : players) {
			if (entityPlayer.getUniqueID().compareTo(player) != 0) {
				continue;
			}
			return entityPlayer;
		}
		return null;
	}

	public static UUID getPlayerID(final GameProfile profile) {
		return profile.getId();
	}

	public static GameProfile getPlayerProfile(final GameProfile profile) {
		return profile;
	}

	public static boolean isOp(final ICommandSender sender) {
		if (!ProxiedUtils.isSMP()) {
			return true;
		}
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			if (sender instanceof EntityPlayer) {
				final EntityPlayer player = (EntityPlayer) sender;
				return player.capabilities.isCreativeMode || player.isSpectator();
			}
		}
		else if (FMLCommonHandler.instance().getSide() == Side.SERVER) {
			if (sender instanceof EntityPlayerMP) {
				final EntityPlayer player = sender.getEntityWorld().getPlayerEntityByName(sender.getName());
				if (player != null && player.getGameProfile() != null) {
					final UserListOpsEntry userentry = ((EntityPlayerMP) player).mcServer.getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
					return userentry != null && userentry.getPermissionLevel() >= 4;
				}
			}
		}
		if (sender instanceof TileEntityCommandBlock) {
			return true;
		}
		return sender.getName().equalsIgnoreCase("@") || sender.getName().equals("Server");
	}

	public static List<UUID> getFullPlayerList() {
		final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		final List<UUID> uuidList = Lists.newArrayList();
		if (server != null) {
			final PlayerProfileCache playerCache = server.getPlayerProfileCache();
			final String[] usernames = EasyMappings.getNames(server);
			for (final String username : usernames) {
				uuidList.add(playerCache.getGameProfileForUsername(username).getId());
			}
		}
		return uuidList;
	}

	public static ItemStack getPlayerSkull(final String playerName) {
		ItemStack head = null;
		final Map<String, ItemStack> skullCache = LibRegistry.getSkullRegistry();
		if (!skullCache.containsKey(playerName)) {
			head = new ItemStack(Items.SKULL, 1, 3);
			final NBTTagCompound nametag = new NBTTagCompound();
			nametag.setString("SkullOwner", playerName);
			head.setTagCompound(nametag);
			skullCache.put(playerName, head);
		}
		return skullCache.get(playerName);
	}

	public static double getDistanceToPos(final double x, final double y, final double z) {
		final EntityPlayer p = Minecraft.getMinecraft().player;
		return MathUtils.getDistanceSq(x, y, z, p.posX, p.posY, p.posZ);
	}

}
