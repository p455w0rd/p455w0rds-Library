package p455w0rdslib.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rdslib.LibGlobals;
import p455w0rdslib.client.render.LayerContributorWings;
import p455w0rdslib.client.render.LayerContributorWings.Type;

/**
 * @author p455w0rd
 *
 */
@SideOnly(Side.CLIENT)
public class ContributorUtils {
	//private static final ResourceLocation CAPE_LOCATION = new ResourceLocation("p455w0rdsthings", "textures/capes/cape2016.png");
	//private static final ResourceLocation MMD_CAPE_LOCATION = new ResourceLocation("p455w0rdsthings", "textures/capes/p455cape7.png");
	private static List<String> PATRON_LIST = Lists.<String>newArrayList();
	public static Map<AbstractClientPlayer, LayerContributorWings.Type> REGISTRY = new LinkedHashMap<>();
	public static List<AbstractClientPlayer> SPECIAL_PLAYERS = Lists.newArrayList();
	public static LayerContributorWings layerWings;

	public static void queuePlayerCosmetics(AbstractClientPlayer player) {
		LibGlobals.THREAD_POOL.submit(() -> {
			try {
				PATRON_LIST = ContributorUtils.getPatronList();
				if (PATRON_LIST.size() <= 0) {
					return;
				}
				Thread.sleep(100);
			}
			catch (InterruptedException e) {
				return;
			}
			Minecraft.getMinecraft().addScheduledTask(() -> {
				ContributorUtils.addCosmetic(player);
			});
		});
	}

	private static void addCosmetic(AbstractClientPlayer player) {

		/*
		if (ContributorUtils.doesPlayerHaveCape(player)) {
			PlayerTextureUtils.setCape(player, CAPE_LOCATION);
			PlayerTextureUtils.setElytra(player, CAPE_LOCATION);
			return;
		}
		else if (ContributorUtils.doesPlayerHaveMMDCape(player)) {
			PlayerTextureUtils.setCape(player, MMD_CAPE_LOCATION);
			PlayerTextureUtils.setElytra(player, MMD_CAPE_LOCATION);
			return;
		}

		if (doesPlayerHaveEmeraldWings(player)) {
			addWings(LayerContributorWings.Type.EMERALD);
			registerContributor(player, LayerContributorWings.Type.EMERALD);
			LibGlobals.IS_CONTRIBUTOR = true;
			return;
		}
		else if (doesPlayerHaveBloodWings(player)) {
			addWings(LayerContributorWings.Type.BLOOD);
			registerContributor(player, LayerContributorWings.Type.BLOOD);
			LibGlobals.IS_CONTRIBUTOR = true;
			return;
		}
		else if (doesPlayerHaveBlueWings(player)) {
			addWings(LayerContributorWings.Type.BLUE);
			registerContributor(player, LayerContributorWings.Type.BLUE);
			LibGlobals.IS_CONTRIBUTOR = true;
			return;
		}
		*/
		if (doesPlayerHaveWings(player)) {
			Type type = getWingTypeForPlayer(player);
			addWings(type);
			registerContributor(player, type);
			LibGlobals.IS_CONTRIBUTOR = true;
			return;
		}
		else if (DateUtils.isXmas() || DateUtils.isXmasEve()) {
			addWings(LayerContributorWings.Type.XMAS);
			registerContributor(player, LayerContributorWings.Type.XMAS);
			LibGlobals.IS_CONTRIBUTOR = true;
			return;
		}
	}

	public static void registerContributor(AbstractClientPlayer player, LayerContributorWings.Type type) {
		if (player != null && type != null) {
			if (!REGISTRY.containsKey(player)) {
				REGISTRY.put(player, type);
				registerSpecialContributor(player);
			}
		}
	}

	public static void registerSpecialContributor(AbstractClientPlayer player) {
		if (player != null && isPlayerSpecial(player)) {
			if (!SPECIAL_PLAYERS.contains(player)) {
				SPECIAL_PLAYERS.add(player);
			}
		}
	}

	public static boolean isContributor(AbstractClientPlayer player) {
		return player != null && REGISTRY.containsKey(player);
	}

	public static LayerContributorWings.Type getWingType(AbstractClientPlayer player) {
		if (player != null) {
			if (REGISTRY.containsKey(player)) {
				return REGISTRY.get(player);
			}
		}
		return null;
	}
	/*
		public static void addWings(LayerContributorWings.Type type) {
			for (RenderPlayer renderPlayer : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
				List<LayerRenderer<AbstractClientPlayer>> r = MCPrivateUtils.getLayerRenderers(renderPlayer);
				for (int i = 0; i < r.size(); ++i) {
					if (r.get(i) instanceof LayerElytra || r.get(i) instanceof LayerCape) {
						renderPlayer.removeLayer(r.get(i));
					}
				}
				renderPlayer.addLayer(layerWings = new LayerContributorWings());
			}
		}
	*/

	public static void addWings(LayerContributorWings.Type type) {
		for (RenderLivingBase<? extends EntityLivingBase> renderPlayer : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
			List<LayerRenderer<?>> r = MCPrivateUtils.getLayerRenderers(renderPlayer);
			for (int i = 0; i < r.size(); ++i) {
				if (r.get(i) instanceof LayerElytra || r.get(i) instanceof LayerCape) {
					r.remove(i);
				}
			}
			renderPlayer.addLayer(layerWings = new LayerContributorWings());
		}
	}

	static List<String> getPatronList() {
		if (MCUtils.isDeobf()) {
			try {
				List<String> entries = new ArrayList<String>();
				HttpURLConnection con;
				con = (HttpURLConnection) new URL("http://p455w0rd.net/mc/patrons.txt").openConnection();
				con.setConnectTimeout(1000);
				InputStream in2 = con.getInputStream();
				entries = IOUtils.readLines(in2);
				if (!entries.isEmpty()) {
					con.disconnect();
					return entries;
				}
			}
			catch (IOException e) {
			}
		}
		else {
			try {
				List<String> entries = new ArrayList<String>();
				HttpURLConnection con;
				con = (HttpURLConnection) new URL("http://p455w0rd.net/mc/patrons.txt").openConnection();
				con.setConnectTimeout(1000);
				InputStream in2 = con.getInputStream();
				entries = IOUtils.readLines(in2);
				if (!entries.isEmpty()) {
					con.disconnect();
					return entries;
				}
			}
			catch (IOException e) {
			}
		}
		return null;
	}

	/*
		public static boolean doesPlayerHaveCape(AbstractClientPlayer player) {
			for (int i = 0; i < PATRON_LIST.size(); ++i) {
				String uuid = player.getUniqueID().toString();
				if (!uuid.equals(PATRON_LIST.get(i))) {
					continue;
				}
				return true;
			}
			return false;
		}

		public static boolean doesPlayerHaveMMDCape(AbstractClientPlayer player) {
			for (int i = 0; i < PATRON_LIST.size(); ++i) {
				String uuid = player.getUniqueID().toString() + "_MMD";
				if (!uuid.equals(PATRON_LIST.get(i))) {
					continue;
				}
				return true;
			}
			return false;
		}

	public static boolean doesPlayerHaveEmeraldWings(AbstractClientPlayer player) {
		for (int i = 0; i < PATRON_LIST.size(); ++i) {
			String uuid = player.getUniqueID().toString() + "_EWINGS";
			if (!uuid.equals(PATRON_LIST.get(i))) {
				continue;
			}
			return true;
		}
		return false;
	}

	public static boolean doesPlayerHaveBloodWings(AbstractClientPlayer player) {
		for (int i = 0; i < PATRON_LIST.size(); ++i) {
			String uuid = player.getUniqueID().toString() + "_RWINGS";
			if (!uuid.equals(PATRON_LIST.get(i))) {
				continue;
			}
			return true;
		}
		return false;
	}

	public static boolean doesPlayerHaveBlueWings(AbstractClientPlayer player) {
		for (int i = 0; i < PATRON_LIST.size(); ++i) {
			String uuid = player.getUniqueID().toString() + "_BWINGS";
			if (!uuid.equals(PATRON_LIST.get(i))) {
				continue;
			}
			return true;
		}
		return false;
	}
	*/
	public static boolean doesPlayerHaveWings(AbstractClientPlayer player) {
		if (PATRON_LIST != null) {
			for (int i = 0; i < PATRON_LIST.size(); ++i) {
				for (Type type : LayerContributorWings.Type.values()) {
					String uuid = player.getUniqueID().toString() + "" + type.getIdentifier();
					if (!uuid.equals(PATRON_LIST.get(i)) && !isPlayerSpecial(uuid, PATRON_LIST.get(i))) {
						continue;
					}
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isPlayerSpecial(AbstractClientPlayer player) {
		if (SPECIAL_PLAYERS.contains(player)) {
			return true;
		}
		for (int i = 0; i < PATRON_LIST.size(); ++i) {
			for (Type type : LayerContributorWings.Type.values()) {
				String uuid = player.getUniqueID().toString() + "" + type.getIdentifier();
				if (!PATRON_LIST.get(i).equals(uuid + "#")) {
					continue;
				}
				return true;
			}
		}
		return false;
	}

	public static boolean isPlayerSpecial(String uuid, String comparison) {
		return comparison.equals(uuid + "#");
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
}