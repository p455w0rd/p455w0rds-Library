package p455w0rdslib.util;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
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

	@SideOnly(Side.CLIENT)
	public static World getWorld() {
		return mc().theWorld;
	}

	public File getDataDir() {
		return mc().mcDataDir;
	}

}
