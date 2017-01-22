package p455w0rdslib;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import p455w0rdslib.LibGlobals.ConfigOptions;

/**
 * @author p455w0rd
 *
 */
public class LibConfig {

	public static Configuration CONFIG;

	private static String DEF_CAT = "Options";

	@SubscribeEvent
	public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent e) {
		if (e.getModID().equals(LibGlobals.MODID)) {
			init();
		}
	}

	public static void init() {
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			if (CONFIG == null) {
				CONFIG = new Configuration(new File(LibGlobals.CONFIG_FILE));
				MinecraftForge.EVENT_BUS.register(new LibConfig());
			}

			ConfigOptions.ENABLE_CONTRIB_CAPE = CONFIG.getBoolean("EnableContributorCosmetics", DEF_CAT, true, "Enable the contributor cosmetics (Only useful if u are a contributor, but don't want the cosmetics to override other cosmetics)");

			if (CONFIG.hasChanged()) {
				CONFIG.save();
			}
		}
	}

}
