package p455w0rdslib.asm;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.math.BlockPos;
import p455w0rdslib.LibGlobals.ConfigOptions;
import p455w0rdslib.LibShaders;
import p455w0rdslib.api.client.shader.LightHandler;
import p455w0rdslib.util.ShaderUtils.Shader;

/**
 * @author p455w0rd
 *
 */
public class Hooks {

	public static void enableColoredLighting() {
		if (LibShaders.areShadersEnabled() && ConfigOptions.ENABLE_SHADERS) {
			if (LibShaders.coloredLightShader == Shader.NONE) {
				LibShaders.reload();
			}
			LibShaders.coloredLightShader.use();
			LibShaders.coloredLightShader.getUniform("base").setInt(0);
			LibShaders.coloredLightShader.getUniform("lightmap").setInt(1);
			//LightHandler.clear();
			LightHandler.update(Minecraft.getMinecraft().world);
			LightHandler.uploadLights();
		}
		else if (LibShaders.coloredLightShader != Shader.NONE) {
			LibShaders.coloredLightShader = Shader.NONE;
		}
	}

	public static void disableColoredLighting() {
		if (LibShaders.areShadersEnabled()) {
			Shader.NONE.use();
		}
	}

	public static void preRenderChunk(final RenderChunk c) {
		if (LibShaders.areShadersEnabled() && ConfigOptions.ENABLE_SHADERS) {
			if (LibShaders.getActiveShader() == LibShaders.coloredLightShader) {
				if (LibShaders.coloredLightShader == Shader.NONE) {
					LibShaders.reload();
				}
				final BlockPos pos = c.getPosition();
				LibShaders.getActiveShader().getUniform("chunkX").setInt(pos.getX());
				LibShaders.getActiveShader().getUniform("chunkY").setInt(pos.getY());
				LibShaders.getActiveShader().getUniform("chunkZ").setInt(pos.getZ());
			}
		}
	}

}
