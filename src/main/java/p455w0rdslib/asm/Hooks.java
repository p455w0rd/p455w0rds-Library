package p455w0rdslib.asm;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.math.BlockPos;
import p455w0rdslib.LibGlobals;
import p455w0rdslib.LibShaders;
import p455w0rdslib.api.client.shader.LightHandler;
import p455w0rdslib.util.ShaderUtils.Shader;

/**
 * @author p455w0rd
 *
 */
public class Hooks {

	public static boolean albedoDetected = false;

	public static void enableColoredLighting() {
		if (LibGlobals.areShadersEnabled()) {
			LibShaders.coloredLightShader.use();
			LibShaders.coloredLightShader.getUniform("base").setInt(0);
			LibShaders.coloredLightShader.getUniform("lightmap").setInt(1);
			//LightHandler.clear();
			LightHandler.update(Minecraft.getMinecraft().world);
			LightHandler.uploadLights();
		}
	}

	public static void disableColoredLighting() {
		if (LibGlobals.areShadersEnabled()) {
			Shader.NONE.use();
		}
	}

	public static void preRenderChunk(final RenderChunk c) {
		if (LibGlobals.areShadersEnabled()) {
			if (LibShaders.getActiveShader() == LibShaders.coloredLightShader) {
				final BlockPos pos = c.getPosition();
				LibShaders.getActiveShader().getUniform("chunkX").setInt(pos.getX());
				LibShaders.getActiveShader().getUniform("chunkY").setInt(pos.getY());
				LibShaders.getActiveShader().getUniform("chunkZ").setInt(pos.getZ());
			}
		}
	}

	public static boolean isOptifineDetected() {
		try {
			return Class.forName("optifine.OptiFineClassTransformer") != null;
		}
		catch (final ClassNotFoundException e) {
			return false;
		}
	}

}
