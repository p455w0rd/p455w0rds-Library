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

	public static String CLIENT_CAT = "Misc Client Options";
	public static String SHADER_CAT = "Colored Light Shader Options";

	@SubscribeEvent
	public void onConfigChange(final ConfigChangedEvent.OnConfigChangedEvent e) {
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
			ConfigOptions.ENABLE_CONTRIB_CAPE = CONFIG.getBoolean("EnableContributorCosmetics", CLIENT_CAT, true, "Enable the contributor cosmetics (Only useful if u are a contributor, but don't want the cosmetics to override other cosmetics)");
			ConfigOptions.ENABLE_CONTRIB_PARTICLES_SELF = CONFIG.getBoolean("EnableContributorParticleEffectsSelf", CLIENT_CAT, true, "Enable (true0 or disable (false) particle effects that result from contriutor cosmetics for current player");
			ConfigOptions.ENABLE_CONTRIB_PARTICLES_OTHERS = CONFIG.getBoolean("EnableContributorParticleEffectsOthers", CLIENT_CAT, true, "Enable (true0 or disable (false) particle effects that result from contriutor cosmetics for other players");
			ConfigOptions.ENABLE_SHADERS = CONFIG.getBoolean("EnableShaders", SHADER_CAT, true, "Enables shader support in dependant mods");
			ConfigOptions.SHADER_NUM_FRAMES_TO_SKIP = CONFIG.getInt("NumFramesToSkipWhenRenderingShaders", SHADER_CAT, 10, 0, 160, "Skips sending light updates to the card some frames. This can speed up fps greatly when bandwidth is a problem. 0 always sends data.");
			ConfigOptions.SHADERS_MAX_DIST = CONFIG.getFloat("MaxRenderDist", SHADER_CAT, 128.0F, 4.0F, 128.0F, "Maximum Distance to render a colored light");
			ConfigOptions.MAX_LIGHTS = CONFIG.getInt("MaxLights", SHADER_CAT, 64, 4, 256, "Maximum Number of lights to be rendered on the screen at once");
			ConfigOptions.ENABLE_VANILLA_LIGHT_EFFECTS = CONFIG.getBoolean("EnableVanillaLightingEffects", SHADER_CAT, false, "If true, then colored lighting effects will be added to vanilla items, blocks, and tile entities.");
			if (CONFIG.hasChanged()) {
				CONFIG.save();
			}
		}
	}

}
