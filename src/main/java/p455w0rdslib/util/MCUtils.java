package p455w0rdslib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		return FMLCommonHandler.instance().getSide().isClient();
	}

	public static boolean isServer() {
		return !isClient();
	}

	public static boolean isDeobf() {
		return (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
	}

	@SideOnly(Side.CLIENT)
	public static Minecraft mc() {
		return Minecraft.getMinecraft();
	}

}
