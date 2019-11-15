package p455w0rdslib;

import java.util.concurrent.*;

import net.minecraftforge.fml.common.Loader;

public class LibGlobals {

	public static final String MODID = "p455w0rdslib";
	public static final String VERSION = "2.3.158";
	public static final String NAME = "p455w0rd's Library";
	public static final String SERVER_PROXY = "p455w0rdslib.proxy.CommonProxy";
	public static final String CLIENT_PROXY = "p455w0rdslib.proxy.ClientProxy";
	public static final String GUI_FACTORY = "p455w0rdslib.LibGuiFactory";
	public static final String DEPENDENCIES = "after:redstoneflux;after:mantle;after:tconstruct;after:enderio;after:projecte;after:tesla;after:thaumcraft;after:albedo";
	public static final String CONFIG_FILE = "config/p455w0rdsLib.cfg";
	public static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
	public static int ELAPSED_TICKS = 0;
	public static int RED = 255;
	public static int GREEN = 0;
	public static int BLUE = 0;
	public static int TURN = 0;
	public static float TIME = 0.0F;
	public static float TIME2 = 0.0F;
	public static boolean IS_CONTRIBUTOR = false;
	public static boolean CONTRIBUTOR_FILE_DOWNLOADED = false;
	public static final String REQUIRE_DEP = "required-after:" + MODID + "@[" + VERSION + ",);";

	public static enum Mods {
			DANKNULL("danknull", "/dank/null"), AE2WTLIB("ae2wtlib", "AE2 Wireless Terminal Library"),
			WCT("wct", "Wireless Crafting Terminal"),
			WFT("wft", "Wireless Fluid Terminal"), WPT("wpt", "Wireless Pattern Terminal"),
			WIT("wit", "Wireless Interface Terminal"), AE2("appliedenergistics2", "Applied Energistics 2"),
			ENDERMAN_EVOLUTION("endermanevo", "Enderman Evolution"),
			TOP("theoneprobe", "The One Probe"), WAILA("waila", "WAILA"), JEI("jei", "JEI"),
			ITEMSCROLLER("itemscroller", "Item Scroller"), NEI("nei", "Not Enough Items"), CHISEL("chisel", "Chisel"),
			THAUMCRAFT("thaumcraft", "Thaumcraft"), BAUBLES("baubles", "Baubles"),
			BAUBLESAPI("Baubles|API", "Baubles API"), ALBEDO("albedo", "Albedo");

		private final String modid, name;

		Mods(final String modidIn, final String nameIn) {
			modid = modidIn;
			name = nameIn;
		}

		public String getId() {
			return modid;
		}

		public String getName() {
			return name;
		}

		public boolean isLoaded() {
			return Loader.isModLoaded(getId());
		}
	}

	public static class ConfigOptions {

		public static boolean ENABLE_CONTRIB_CAPE = true;
		public static boolean ENABLE_CONTRIB_PARTICLES_SELF = true;
		public static boolean ENABLE_CONTRIB_PARTICLES_OTHERS = true;
		public static boolean ENABLE_SHADERS = true;
		public static boolean ENABLE_BIT_NIGHTMARE = false;
		public static double SHADERS_MAX_DIST = 64D;
		public static int MAX_LIGHTS = 8;
		public static int SHADER_NUM_FRAMES_TO_SKIP = 10;
		public static boolean ENABLE_VANILLA_LIGHT_EFFECTS = false;

	}
}
