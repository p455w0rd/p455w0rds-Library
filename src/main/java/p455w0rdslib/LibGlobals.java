package p455w0rdslib;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LibGlobals {
	public static final String MODID = "p455w0rdslib";
	public static final String VERSION = "1.0.35";
	public static final String NAME = "p455w0rd's Library";
	public static final String SERVER_PROXY = "p455w0rdslib.proxy.CommonProxy";
	public static final String CLIENT_PROXY = "p455w0rdslib.proxy.ClientProxy";
	public static final String DEPENDENCIES = "after:mantle;after:tconstruct;after:enderio;after:ProjectE;after:tesla";
	public static final String CONFIG_FILE = "config/p455w0rdsLib.cfg";
	public static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
	public static int ELAPSED_TICKS = 0;
	public static boolean IS_CONTRIBUTOR = false;
	public static boolean CONTRIBUTOR_FILE_DOWNLOADED = false;

	public static class ConfigOptions {
		public static boolean ENABLE_CONTRIB_CAPE = true;
	}
}
