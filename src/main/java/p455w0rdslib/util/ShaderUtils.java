package p455w0rdslib.util;

import java.nio.*;
import java.util.Arrays;
import java.util.Map;

import org.lwjgl.opengl.*;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

import net.minecraft.client.renderer.OpenGlHelper;
import p455w0rdslib.LibShaders;

/**
 * @author p455w0rd
 *
 */
public class ShaderUtils {

	public static class Shader {

		public static final Shader NONE = new Shader(0);

		private final int program;
		private final Map<String, Uniform> uniforms = Maps.newConcurrentMap();

		public Shader(final int program) {
			this.program = program;
		}

		public void refreshUniforms() {
			if (GLContext.getCapabilities().OpenGL31) {
				final int numUniforms = GL20.glGetProgrami(program, GL20.GL_ACTIVE_UNIFORMS);
				for (int i = 0; i < numUniforms; i++) {
					final String name = GL31.glGetActiveUniformName(program, i, 32);
					final int idx = GL20.glGetUniformLocation(program, name);
					uniforms.put(name, new Uniform(idx, null));
				}
			}
		}

		public Uniform getUniform(final String name) {
			if (!uniforms.containsKey(name)) {
				final int loc = GL20.glGetUniformLocation(program, name);
				if (loc == -1) {
					//LogManager.getLogger(LibGlobals.MODID).warn("Uniform \"" + name + "\" not found");
				}
				else {
					uniforms.put(name, new Uniform(loc, null));
				}
			}
			return uniforms.get(name);
		}

		public void use() {
			LibShaders.setActiveShader(this);
			OpenGlHelper.glUseProgram(program);
		}

		public void delete() {
			OpenGlHelper.glDeleteProgram(program);
		}

		public int getId() {
			return program;
		}

		public boolean isActive() {
			return LibShaders.getActiveShader() == this;
		}

	}

	public static class Uniform {

		private final int location;
		private Object lastValue;

		public Uniform(final int location, final Object lastValue) {
			this.location = location;
			this.lastValue = lastValue;
		}

		private boolean equal(final Object a, final Object b) {
			if (a instanceof int[] && b instanceof int[]) {
				return Arrays.equals((int[]) a, (int[]) b);
			}
			if (a instanceof float[] && b instanceof float[]) {
				return Arrays.equals((float[]) a, (float[]) b);
			}
			return Objects.equal(a, b);
		}

		public void setFloat(final float val) {
			if (equal(val, lastValue)) {
				return;
			}
			GL20.glUniform1f(location, val);
		}

		public void setInt(final int val) {
			if (equal(val, lastValue)) {
				return;
			}
			GL20.glUniform1i(location, val);
		}

		public void setFloat(final float val1, final float val2) {
			final float[] val = new float[] {
					val1, val2
			};
			if (equal(val, lastValue)) {
				return;
			}
			GL20.glUniform2f(location, val1, val2);
			lastValue = val;
		}

		public void setInt(final int val1, final int val2) {
			final int[] val = new int[] {
					val1, val2
			};
			if (equal(val, lastValue)) {
				return;
			}
			GL20.glUniform2i(location, val1, val2);
			lastValue = val;
		}

		public void setFloat(final float val1, final float val2, final float val3) {
			final float[] val = new float[] {
					val1, val2, val3
			};
			if (equal(val, lastValue)) {
				return;
			}
			GL20.glUniform3f(location, val1, val2, val3);
			lastValue = val;
		}

		public void setInt(final int val1, final int val2, final int val3) {
			final int[] val = new int[] {
					val1, val2, val3
			};
			if (equal(val, lastValue)) {
				return;
			}
			GL20.glUniform3i(location, val1, val2, val3);
			lastValue = val;
		}

		public void setFloat(final float val1, final float val2, final float val3, final float val4) {
			final float[] val = new float[] {
					val1, val2, val3, val4
			};
			if (equal(val, lastValue)) {
				return;
			}
			GL20.glUniform4f(location, val1, val2, val3, val4);
			lastValue = val;
		}

		public void setInt(final int val1, final int val2, final int val3, final int val4) {
			final int[] val = new int[] {
					val1, val2, val3, val4
			};
			if (equal(val, lastValue)) {
				return;
			}
			GL20.glUniform4i(location, val1, val2, val3, val4);
			lastValue = val;
		}

		public void setFloats(final float[] floats) {
			final FloatBuffer values = ByteBuffer.allocateDirect(floats.length * Float.BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
			values.put(floats);
			GL20.glUniform1(location, values);
		}
	}

	public static enum ShaderType {

			VERTEX(35633), FRAGMENT(35632);

		private final int type;

		ShaderType(final int type) {
			this.type = type;
		}

		public int getId() {
			return type;
		}

	}
}
