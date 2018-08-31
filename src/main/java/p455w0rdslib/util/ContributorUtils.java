package p455w0rdslib.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
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
	//private static final ResourceLocation CAPE_LOCATION = new ResourceLocation("p455w0rdsthings", "textures/capes/cape2016.png");
	//private static final ResourceLocation MMD_CAPE_LOCATION = new ResourceLocation("p455w0rdsthings", "textures/capes/p455cape7.png");
	private static List<String> PATRON_LIST = Lists.<String>newArrayList();
	public static Map<AbstractClientPlayer, LayerContributorWings.Type> REGISTRY = new LinkedHashMap<>();
	public static List<AbstractClientPlayer> SPECIAL_PLAYERS = Lists.newArrayList();
	public static LayerContributorWings layerWings;
	public static LayerContribDankNull layerDankNull;
	private static DLThread thread;

	public static void queuePlayerCosmetics(AbstractClientPlayer player) {
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
		/*
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
		*/
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

	public static LayerContributorWings.Type getWingType(String uuid) {
		for (EntityPlayer player : REGISTRY.keySet()) {
			if (uuid.contains(player.getUniqueID().toString())) {
				return REGISTRY.get(player);
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
			removeVanillaSpecialLayers(r);
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

	public static boolean isPlayerSpecial(AbstractClientPlayer player) {
		if (SPECIAL_PLAYERS.contains(player)) {
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

	public static boolean isPlayerSuperSpecial(AbstractClientPlayer player) {
		if (isPlayerSpecial(player)) {
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