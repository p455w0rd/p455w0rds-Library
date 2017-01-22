package p455w0rdslib.util;

import java.util.Map;

import net.minecraft.launchwrapper.Launch;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author p455w0rd
 *
 */
public class MCUtils {

	public static boolean isSSP(MinecraftServer server) {
		return server.getServer().isSinglePlayer();
	}

	public static boolean isSMP(MinecraftServer server) {
		return !isSSP(server);
	}

	public static boolean isClient() {
		return FMLCommonHandler.instance().getSide() == Side.CLIENT;
	}

	public static boolean isServer() {
		return !isClient();
	}

	public static boolean isDeobf() {
		Map<String, Object> bb = Launch.blackboard;
		return (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
	}

}
