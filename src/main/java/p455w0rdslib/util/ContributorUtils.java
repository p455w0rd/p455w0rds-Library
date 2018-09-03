package p455w0rdslib.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rdslib.LibGlobals;
import p455w0rdslib.api.IProcess;
import p455w0rdslib.client.render.LayerContribDankNull;
import p455w0rdslib.client.render.LayerContributorWings;
import p455w0rdslib.client.render.LayerContributorWings.Type;
import p455w0rdslib.handlers.ProcessHandler;
import p455w0rdslib.handlers.ProcessHandlerClient;

/**
 * @author p455w0rd
 *
 */
@SideOnly(Side.CLIENT)
public class ContributorUtils {

	private static List<String> PATRON_LIST = Lists.<String>newArrayList();
	public static Map<UUID, LayerContributorWings.Type> REGISTRY = new LinkedHashMap<>();
	public static List<UUID> SPECIAL_PLAYERS = Lists.newArrayList();
	public static LayerContributorWings layerWings;
	public static LayerContribDankNull layerDankNull;
	private static DLThread thread;

	public static void queuePlayerCosmetics(AbstractClientPlayer player) {
		if (LibGlobals.CONTRIBUTOR_FILE_DOWNLOADED) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				ContributorUtils.addCosmetic(player);
			});
			return;
		}
		if (!isOnlineMode(player)) {
			return;
		}
		thread = new DLThread();
		thread.setDaemon(true);
		thread.start();

		IProcess process = new IProcess() {
			@Override
			public void updateProcess() {
				if (thread.isFinished()) {
					thread = null;
					Minecraft.getMinecraft().addScheduledTask(() -> {
						ContributorUtils.addCosmetic(player);
					});
				}
				else if (thread.isFailed()) {
					thread = null;
				}
			}

			@Override
			public boolean isDead() {
				return thread == null;
			}
		};

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			ProcessHandlerClient.addProcess(process);
		}
		else {
			ProcessHandler.addProcess(process);
		}

	}

	private static boolean isOnlineMode(EntityPlayer player) {
		if (player == null) {
			return false;
		}
		return !UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(Charsets.UTF_8)).equals(player.getUniqueID());
	}

	private static void addCosmetic(AbstractClientPlayer player) {
		if (doesPlayerHaveDankNull(player)) {
			addDankNull();
		}
		if (doesPlayerHaveWings(player)) {
			Type type = getWingTypeForPlayer(player);
			addWings(type);
			registerContributor(player, type);
			LibGlobals.IS_CONTRIBUTOR = true;
		}
		if ((DateUtils.isXmas() || DateUtils.isXmasEve()) && !doesPlayerHaveWings(player)) {
			addWings(LayerContributorWings.Type.XMAS);
			registerContributor(player, LayerContributorWings.Type.XMAS);
			LibGlobals.IS_CONTRIBUTOR = true;
			return;
		}
	}

	public static void registerContributor(AbstractClientPlayer player, LayerContributorWings.Type type) {
		if (player != null && type != null) {
			if (!REGISTRY.containsKey(player.getUniqueID())) {
				REGISTRY.put(player.getUniqueID(), type);
				registerSpecialContributor(player);
			}
		}
	}

	public static void registerSpecialContributor(AbstractClientPlayer player) {
		if (player != null && isPlayerSpecial(player.getUniqueID())) {
			if (!SPECIAL_PLAYERS.contains(player.getUniqueID())) {
				SPECIAL_PLAYERS.add(player.getUniqueID());
			}
		}
	}

	public static boolean isContributor(AbstractClientPlayer player) {
		return player != null && REGISTRY.containsKey(player.getUniqueID());
	}

	public static LayerContributorWings.Type getWingType(AbstractClientPlayer player) {
		if (player != null) {
			if (REGISTRY.containsKey(player.getUniqueID())) {
				return REGISTRY.get(player.getUniqueID());
			}
		}
		return null;
	}

	public static LayerContributorWings.Type getWingType(String uuid) {
		for (UUID playerUUID : REGISTRY.keySet()) {
			if (uuid.contains(playerUUID.toString())) {
				return REGISTRY.get(playerUUID);
			}
		}
		return null;
	}

	private static void removeVanillaSpecialLayers(List<LayerRenderer<?>> r) {
		for (int i = 0; i < r.size(); ++i) {
			if (r.get(i) instanceof LayerElytra || r.get(i) instanceof LayerCape) {
				r.remove(i);
			}
		}
	}

	public static void addWings(LayerContributorWings.Type type) {
		for (RenderLivingBase<? extends EntityLivingBase> renderPlayer : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
			List<LayerRenderer<?>> r = MCPrivateUtils.getLayerRenderers(renderPlayer);
			//removeVanillaSpecialLayers(r);
			renderPlayer.addLayer(layerWings = new LayerContributorWings());
		}
	}

	public static void addDankNull() {
		for (RenderLivingBase<? extends EntityLivingBase> renderPlayer : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
			List<LayerRenderer<?>> r = MCPrivateUtils.getLayerRenderers(renderPlayer);
			removeVanillaSpecialLayers(r);
			renderPlayer.addLayer(layerDankNull = new LayerContribDankNull());
		}
	}

	public static boolean doesPlayerHaveWings(AbstractClientPlayer player) {
		if (PATRON_LIST != null) {
			for (int i = 0; i < PATRON_LIST.size(); ++i) {
				for (Type type : LayerContributorWings.Type.values()) {
					String uuid = player.getUniqueID().toString() + "" + type.getIdentifier();
					if (!uuid.contains(PATRON_LIST.get(i)) && !isPlayerSpecial(uuid, PATRON_LIST.get(i))) {
						continue;
					}
					return true;
				}
			}
		}
		return false;
	}

	public static boolean doesPlayerHaveDankNull(AbstractClientPlayer player) {
		return false;
	}

	public static boolean isPlayerSpecial(UUID playerUUID) {
		if (SPECIAL_PLAYERS.contains(playerUUID)) {
			return true;
		}
		for (int i = 0; i < PATRON_LIST.size(); ++i) {
			if (!PATRON_LIST.get(i).split("_")[1].contains("#")) {
				continue;
			}
			return true;
		}
		return false;
	}

	public static boolean isPlayerSuperSpecial(UUID playerUUID) {
		if (isPlayerSpecial(playerUUID)) {
			for (int i = 0; i < PATRON_LIST.size(); ++i) {
				if (!PATRON_LIST.get(i).split("_")[1].contains("!")) {
					continue;
				}
				return true;
			}
		}
		return false;
	}

	public static boolean isPlayerSpecial(String uuid, String comparison) {
		return comparison.contains(uuid) && comparison.contains("#");
	}

	public static boolean isPlayerSpecial(String uuid) {
		if (!PATRON_LIST.isEmpty()) {
			for (int i = 0; i < PATRON_LIST.size(); ++i) {
				if (!PATRON_LIST.get(i).split("_")[1].contains("#")) {
					continue;
				}
				return true;
			}
		}
		return false;
	}

	public static boolean isPlayerSuperSpecial(String uuid) {
		if (!PATRON_LIST.isEmpty() && isPlayerSpecial(uuid)) {
			for (int i = 0; i < PATRON_LIST.size(); ++i) {
				if (!PATRON_LIST.get(i).split("_")[1].contains("!")) {
					continue;
				}
				return true;
			}
		}
		return false;
	}

	public static Type getWingTypeForPlayer(AbstractClientPlayer player) {
		for (int i = 0; i < PATRON_LIST.size(); ++i) {
			for (Type type : LayerContributorWings.Type.values()) {
				String uuid = player.getUniqueID().toString() + "" + type.getIdentifier();
				if (!uuid.equals(PATRON_LIST.get(i)) && !isPlayerSpecial(uuid, PATRON_LIST.get(i))) {
					continue;
				}
				return type;
			}
		}
		return null;
	}

	public static class DLThread extends Thread {

		private boolean finished = false;
		private boolean failed = false;

		public DLThread() {
			super("TheRealp455w0rd Contributors DL Thread");
		}

		@Override
		public void run() {
			super.run();

			try {
				List<String> entries = new ArrayList<String>();
				HttpURLConnection con;
				con = (HttpURLConnection) new URL("https://s3.us-east-2.amazonaws.com/p455w0rd/patrons.txt").openConnection();
				con.setConnectTimeout(1000);
				InputStream in2 = con.getInputStream();
				entries = IOUtils.readLines(in2, Charset.defaultCharset());
				if (!entries.isEmpty()) {
					PATRON_LIST = entries;
					LibGlobals.CONTRIBUTOR_FILE_DOWNLOADED = true;
				}
				in2.close();
				con.disconnect();
				finished = true;
				failed = PATRON_LIST.isEmpty();
			}
			catch (Exception e) {
				failed = true;
				e.printStackTrace();
			}

		}

		public boolean isFinished() {
			return finished;
		}

		public boolean isFailed() {
			return failed;
		}
	}

}