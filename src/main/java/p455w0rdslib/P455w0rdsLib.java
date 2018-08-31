package p455w0rdslib;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import p455w0rdslib.LibGlobals.ModInfo;
import p455w0rdslib.proxy.CommonProxy;

/**
 * @author p455w0rd
 *
 */
@Mod(modid = LibGlobals.MODID, name = LibGlobals.NAME, useMetadata = false, version = LibGlobals.VERSION, dependencies = LibGlobals.DEPENDENCIES, acceptedMinecraftVersions = "1.12")
public class P455w0rdsLib {

	@SidedProxy(clientSide = LibGlobals.CLIENT_PROXY, serverSide = LibGlobals.SERVER_PROXY)
	public static CommonProxy PROXY;

	@Instance(LibGlobals.MODID)
	public static P455w0rdsLib INSTANCE;

	@Metadata(LibGlobals.MODID)
	public static ModMetadata meta;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		ModInfo.setData();
		INSTANCE = this;
		PROXY.preInit(e);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		PROXY.init(e);
	}

}
