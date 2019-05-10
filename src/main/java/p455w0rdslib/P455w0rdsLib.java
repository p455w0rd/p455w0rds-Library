package p455w0rdslib;

import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import p455w0rdslib.proxy.CommonProxy;

/**
 * @author p455w0rd
 *
 */
@Mod(modid = LibGlobals.MODID, name = LibGlobals.NAME, version = LibGlobals.VERSION, dependencies = LibGlobals.DEPENDENCIES, acceptedMinecraftVersions = "1.12", certificateFingerprint = "@FINGERPRINT@")
public class P455w0rdsLib {

	@SidedProxy(clientSide = LibGlobals.CLIENT_PROXY, serverSide = LibGlobals.SERVER_PROXY)
	public static CommonProxy PROXY;

	@Instance(LibGlobals.MODID)
	public static P455w0rdsLib INSTANCE;

	@Metadata(LibGlobals.MODID)
	public static ModMetadata meta;

	@EventHandler
	public void preInit(final FMLPreInitializationEvent e) {
		INSTANCE = this;
		PROXY.preInit(e);
	}

	@EventHandler
	public void init(final FMLInitializationEvent e) {
		PROXY.init(e);
	}

}
