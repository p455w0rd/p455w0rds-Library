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
package p455w0rdslib.api.client.shader;

import java.nio.FloatBuffer;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Light {

	public static final float DOT_15 = 0.966f;
	public static final float DOT_30 = 0.866f;
	public static final float DOT_45 = 0.707f;
	public static final float DOT_60 = 0.500f;
	public static final float DOT_90 = 0.000f;

	public float x;
	public float y;
	public float z;

	public float r;
	public float g;
	public float b;
	public float a;
	public float l;

	//cone vector and falloff
	public float mag = 1.0f; //Used to prevent precision loss when repeatedly reorienting.
	public float sx;
	public float sy;
	public float sz;

	public float sf;

	@Deprecated
	public Light(final float x, final float y, final float z, final float r, final float g, final float b, final float a, final float radius) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		l = 1.0f;
		sx = radius;
		sy = 0;
		sz = 0;
	}

	public void writeTo(final float[] buf, final int ofs) {
		buf[ofs + 0] = r;
		buf[ofs + 1] = g;
		buf[ofs + 2] = b;
		buf[ofs + 3] = a;
		buf[ofs + 4] = x;
		buf[ofs + 5] = y;
		buf[ofs + 6] = z;
		buf[ofs + 7] = sx;
		buf[ofs + 8] = sy;
		buf[ofs + 9] = sz;
		buf[ofs + 10] = l;
	}

	public void writeTo(final FloatBuffer buf) {
		buf.put(r);
		buf.put(g);
		buf.put(b);
		buf.put(a);
		buf.put(x);
		buf.put(y);
		buf.put(z);
		buf.put(sx);
		buf.put(sy);
		buf.put(sz);
		buf.put(l);
	}

	public void moveTo(final BlockPos pos) {
		x = pos.getX() + 0.5f;
		y = pos.getY() + 0.5f;
		z = pos.getZ() + 0.5f;
	}

	public void moveTo(final Entity entity) {
		x = (float) entity.posX;
		y = (float) entity.posY + entity.getEyeHeight();
		z = (float) entity.posZ;
	}

	public void moveAndOrientTo(final Entity entity) {
		moveTo(entity);
		final Vec3d vec = entity.getLookVec();
		sx = (float) vec.x * mag;
		sy = (float) vec.y * mag;
		sz = (float) vec.z * mag;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {

		private float x = 0;
		private float y = 0;
		private float z = 0;

		private float r = 1;
		private float g = 1;
		private float b = 1;
		private float a = 1;
		private float l = 1.0f;

		private float radius = Float.NaN;

		private final float sx = 1;
		private final float sy = 0;
		private final float sz = 0;

		public Builder pos(final BlockPos pos) {
			return pos(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
		}

		public Builder pos(final Vec3d pos) {
			return pos(pos.x, pos.y, pos.z);
		}

		public Builder pos(final Entity e) {
			return pos(e.posX, e.posY, e.posZ);
		}

		public Builder pos(final double x, final double y, final double z) {
			return pos((float) x, (float) y, (float) z);
		}

		public Builder pos(final float x, final float y, final float z) {
			this.x = x;
			this.y = y;
			this.z = z;
			return this;
		}

		public Builder color(final int c, final boolean hasAlpha) {
			return color(extract(c, 2), extract(c, 1), extract(c, 0), hasAlpha ? extract(c, 3) : 1);
		}

		private float extract(final int i, final int idx) {
			return (i >> idx * 8 & 0xFF) / 255f;
		}

		public Builder color(final float r, final float g, final float b) {
			return color(r, g, b, 1);
		}

		public Builder color(final float r, final float g, final float b, final float a) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
			return this;
		}

		public Builder radius(final float radius) {
			this.radius = radius;
			return this;
		}

		public Builder intensity(final float l) {
			this.l = l;
			return this;
		}

		public Light build() {
			if (Float.isFinite(radius)) {
				final Light l = new Light(x, y, z, r, g, b, a, 1.0f);
				l.sx = sx * radius;
				l.sy = sy * radius;
				l.sz = sz * radius;
				l.mag = radius;
				//l.sf = sf;
				l.l = this.l;
				return l;
			}
			else {
				throw new IllegalArgumentException("Radius must be set, and cannot be infinite");
			}
		}
	}

}
