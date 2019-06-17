/*
 * The MIT License
 *
 * Copyright (c) 2017 Elucent, Una Thompson (unascribed), and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package p455w0rdslib;

import org.lwjgl.opengl.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rdslib.asm.Hooks;
import p455w0rdslib.util.ShaderUtils.Shader;
import p455w0rdslib.util.ShaderUtils.ShaderType;

@SideOnly(Side.CLIENT)
public class LibShaders {

	private static Shader activeShader = Shader.NONE;
	public static Shader coloredLightShader = Shader.NONE;

	private static boolean shaderCheck = false;
	private static boolean shadersEnabled = false;

	public static boolean areShadersEnabled() {
		if (!shaderCheck && Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
			shaderCheck = true;
			shadersEnabled = GLContext.getCapabilities().OpenGL20;
			if (Hooks.conflictDetected) {
				shadersEnabled = false;
			}
		}
		return shadersEnabled;
	}

	public static Shader getActiveShader() {
		return activeShader;
	}

	public static void setActiveShader(final Shader shader) {
		activeShader = shader;
	}

	public static void registerReloadListener() {
		if (!areShadersEnabled()) {
			return;
		}
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener((irm) -> {
			reload();
		});
	}

	public static void reload() {
		if (coloredLightShader != null) {
			coloredLightShader.delete();
			coloredLightShader = null;
		}
		coloredLightShader = new Shader(createProgram(getLightOverlaySrc(ShaderType.VERTEX), getLightOverlaySrc(ShaderType.FRAGMENT)));
		coloredLightShader.use();
		coloredLightShader.refreshUniforms();
		Shader.NONE.use();
	}

	public static int createProgram(final String vsh, final String fsh) {
		final int p = OpenGlHelper.glCreateProgram();
		OpenGlHelper.glAttachShader(p, genShader(vsh, ShaderType.VERTEX));
		OpenGlHelper.glAttachShader(p, genShader(fsh, ShaderType.FRAGMENT));
		OpenGlHelper.glLinkProgram(p);
		if (GL20.glGetProgrami(p, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			throw new RuntimeException("Error linking program: " + GL20.glGetProgramInfoLog(p, 65536));
		}
		return p;
	}

	public static int genShader(final String source, final ShaderType type) {
		final int id = OpenGlHelper.glCreateShader(type.getId());
		if (id == 0) {
			return 0;
		}
		ARBShaderObjects.glShaderSourceARB(id, source);
		OpenGlHelper.glCompileShader(id);
		if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			throw new RuntimeException("Error creating shader: " + ARBShaderObjects.glGetInfoLogARB(id, ARBShaderObjects.glGetObjectParameteriARB(id, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB)));
		}
		return id;
	}

	public static String getLightOverlaySrc(final ShaderType type) {
		switch (type) {
		case FRAGMENT:
			return new StringBuilder()
			//@formatter:off
			.append("#version 120\n")
			.append("varying vec3 position;\n")
			.append("varying float magnitude;\n")
			.append("varying vec4 lightColor;\n")
			.append("uniform int chunkX;\n")
			.append("uniform int chunkZ;\n")
			.append("uniform sampler2D base;\n")
			.append("uniform sampler2D lightmap;\n")
			.append("vec3 normlize(vec3 vec) {\n")
			.append("	float length = sqrt((vec.x*vec.x) + (vec.y*vec.y) + (vec.z*vec.z));\n")
			.append("	if (length==0) return vec;\n")
			.append("	return vec3(vec.x/length, vec.y/length, vec.z/length)+vec3(0.5,0.5,0.5);\n")
			.append("}\n")
			.append("void main() {\n")
			.append("   gl_FragColor = clamp((gl_Color * texture2D(base, gl_TexCoord[0].st)) * vec4(vec3(mix(clamp(texture2D(lightmap, gl_TexCoord[1].st).xyz, 0.0f, 1.0f).xyz, normlize(lightColor.xyz), magnitude)),1), 0.0f, 1.0f);\n")
			.append("}\n")
			.toString();
			//@formatter:on
		case VERTEX:
			return new StringBuilder()
			//@formatter:off
			.append("#version 120\n")
			.append("varying vec3 position;\n")
			.append("varying vec4 lightColor;\n")
			.append("varying float magnitude;\n")
			.append("struct Light {\n")
			.append("vec4 color;\n")
			.append("vec3 position;\n")
			.append("vec3 rad;\n")
			.append("float intensity;\n")
			.append("};\n")
			.append("uniform int chunkX;\n")
			.append("uniform int chunkY;\n")
			.append("uniform int chunkZ;\n")
			.append("uniform sampler2D base;\n")
			.append("uniform sampler2D lightmap;\n")
			.append("uniform Light lights[100];\n")
			.append("uniform int lightCount;\n")
			.append("uniform int maxLights;\n")
			.append("void main() {\n")
			.append("vec4 pos = gl_ModelViewProjectionMatrix * gl_Vertex;\n")
			.append("position = gl_Vertex.xyz+vec3(chunkX,chunkY,chunkZ);\n")
			.append("float offset = 0;\n")
			.append("gl_TexCoord[0] = gl_TextureMatrix[0] * gl_MultiTexCoord0;\n")
			.append("gl_TexCoord[1] = gl_TextureMatrix[1] * gl_MultiTexCoord1;\n")
			.append("gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n")//vec4(gl_Vertex.xyz, 1.0);\n")//(gl_Vertex + vec4(0,offset,0,0));\n")
			.append("gl_FrontColor = gl_Color;\n")
			.append("float finalR = 0;\n")
			.append("float finalG = 0;\n")
			.append("float finalB = 0;\n")
			.append("float finalIntensity = 0;\n")
			.append("for (int i = 0; i < lightCount; i ++) {\n")
			.append("float dist = distance(lights[i].position, position);\n")
			.append("float radius = length(lights[i].rad);\n")
			.append("if (dist <= radius) {\n")
			.append("float falloff = 1-clamp(dist/radius, 0.0f, 1.0f);\n")
			.append("float intensity = falloff * lights[i].color.w * lights[i].intensity;\n")
			.append("finalIntensity += intensity;\n")
			.append("vec3 normalLight = lights[i].color.xyz;\n")
			.append("finalR += normalLight.x;\n")
			.append("finalG += normalLight.y;\n")
			.append("finalB += normalLight.z;\n")
			.append("}\n")
			.append("}\n")
			.append("magnitude = finalIntensity;\n")
			.append("lightColor = vec4(finalR, finalG, finalB, 1);\n")
			.append("}\n")
			.toString();
			//@formatter:on
		default:
			return "";
		}
	}

	public static final String LIGHTOVERLAY_SRC_FRAG = getLightOverlaySrc(ShaderType.FRAGMENT);

	public static final String LIGHTOVERLAY_SRC_VERT = getLightOverlaySrc(ShaderType.VERTEX);
}