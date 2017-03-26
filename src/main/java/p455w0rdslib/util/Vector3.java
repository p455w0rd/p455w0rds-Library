package p455w0rdslib.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import javax.vecmath.Vector4f;

import org.lwjgl.util.vector.Vector3f;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rdslib.api.ICopyable;

/**
 * @author p455w0rd
 *
 */
public class Vector3 implements ICopyable<Vector3> {

	public static Vector3 zero = new Vector3();
	public static Vector3 one = new Vector3(1, 1, 1);
	public static Vector3 center = new Vector3(0.5, 0.5, 0.5);

	public double x;
	public double y;
	public double z;

	public Vector3() {
	}

	public Vector3(double d, double d1, double d2) {
		x = d;
		y = d1;
		z = d2;
	}

	public Vector3(Vector3 vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
	}

	public Vector3(double[] da) {
		this(da[0], da[1], da[2]);
	}

	public Vector3(float[] fa) {
		this(fa[0], fa[1], fa[2]);
	}

	public Vector3(Vec3d vec) {
		x = vec.xCoord;
		y = vec.yCoord;
		z = vec.zCoord;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public Vector3 add(Vec3i vec) {
		x += vec.getX();
		y += vec.getY();
		z += vec.getZ();
		return this;
	}

	public static Vector3 fromBlockPos(BlockPos pos) {
		return fromVec3i(pos);
	}

	public static Vector3 fromVec3i(Vec3i pos) {
		return new Vector3(pos.getX(), pos.getY(), pos.getZ());
	}

	public static Vector3 fromBlockPosCenter(BlockPos pos) {
		return fromBlockPos(pos).add(0.5);
	}

	public static Vector3 fromEntity(Entity e) {
		return new Vector3(e.posX, e.posY, e.posZ);
	}

	public static Vector3 fromEntityCenter(Entity e) {
		return new Vector3(e.posX, e.posY - e.getYOffset() + e.height / 2, e.posZ);
	}

	public static Vector3 fromTile(TileEntity tile) {
		return fromBlockPos(tile.getPos());
	}

	public static Vector3 fromTileCenter(TileEntity tile) {
		return fromTile(tile).add(0.5);
	}

	public static Vector3 fromAxes(double[] da) {
		return new Vector3(da[2], da[0], da[1]);
	}

	public static Vector3 fromAxes(float[] fa) {
		return new Vector3(fa[2], fa[0], fa[1]);
	}

	public static Vector3 fromArray(double[] da) {
		return new Vector3(da[0], da[1], da[2]);
	}

	public static Vector3 fromArray(float[] fa) {
		return new Vector3(fa[0], fa[1], fa[2]);
	}

	public Vec3d vec3() {
		return new Vec3d(x, y, z);
	}

	public BlockPos pos() {
		return new BlockPos(x, y, z);
	}

	@SideOnly(Side.CLIENT)
	public Vector3f vector3f() {
		return new Vector3f((float) x, (float) y, (float) z);
	}

	@SideOnly(Side.CLIENT)
	public Vector4f vector4f() {
		return new Vector4f((float) x, (float) y, (float) z, 1);
	}

	@SideOnly(Side.CLIENT)
	public void glVertex() {
		GlStateManager.glVertex3f((float) x, (float) y, (float) z);
	}

	public Vector3 set(double x1, double y1, double z1) {
		x = x1;
		y = y1;
		z = z1;
		return this;
	}

	public Vector3 set(double d) {
		return set(d, d, d);
	}

	public Vector3 set(Vector3 vec) {
		return set(vec.x, vec.y, vec.z);
	}

	public Vector3 set(Vec3i vec) {
		return set(vec.getX(), vec.getY(), vec.getZ());
	}

	public Vector3 set(double[] da) {
		return set(da[0], da[1], da[2]);
	}

	public Vector3 set(float[] fa) {
		return set(fa[0], fa[1], fa[2]);
	}

	public Vector3 add(double dx, double dy, double dz) {
		x += dx;
		y += dy;
		z += dz;
		return this;
	}

	public Vector3 add(double d) {
		return add(d, d, d);
	}

	public Vector3 add(Vector3 vec) {
		return add(vec.x, vec.y, vec.z);
	}

	public Vector3 add(BlockPos pos) {
		return add(pos.getX(), pos.getY(), pos.getZ());
	}

	public Vector3 subtract(double dx, double dy, double dz) {
		x -= dx;
		y -= dy;
		z -= dz;
		return this;
	}

	public Vector3 subtract(double d) {
		return subtract(d, d, d);
	}

	public Vector3 subtract(Vector3 vec) {
		return subtract(vec.x, vec.y, vec.z);
	}

	public Vector3 subtract(BlockPos pos) {
		return subtract(pos.getX(), pos.getY(), pos.getZ());
	}

	public Vector3 multiply(double fx, double fy, double fz) {
		x *= fx;
		y *= fy;
		z *= fz;
		return this;
	}

	public Vector3 multiply(double f) {
		return multiply(f, f, f);
	}

	public Vector3 multiply(Vector3 f) {
		return multiply(f.x, f.y, f.z);
	}

	public Vector3 divide(double fx, double fy, double fz) {
		x /= fx;
		y /= fy;
		z /= fz;
		return this;
	}

	public Vector3 divide(double f) {
		return divide(f, f, f);
	}

	public Vector3 divide(Vector3 vec) {
		return divide(vec.x, vec.y, vec.z);
	}

	public Vector3 divide(BlockPos pos) {
		return divide(pos.getX(), pos.getY(), pos.getZ());
	}

	public Vector3 floor() {
		x = MathUtils.floor(x);
		y = MathUtils.floor(y);
		z = MathUtils.floor(z);
		return this;
	}

	public Vector3 celi() {
		x = MathUtils.ceil(x);
		y = MathUtils.ceil(y);
		z = MathUtils.ceil(z);
		return this;
	}

	public double mag() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public double magSquared() {
		return x * x + y * y + z * z;
	}

	public Vector3 negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}

	public Vector3 normalize() {
		double d = mag();
		if (d != 0) {
			multiply(1 / d);
		}
		return this;
	}

	public double dotProduct(double x1, double y1, double z1) {
		return x1 * x + y1 * y + z1 * z;
	}

	public double dotProduct(Vector3 vec) {
		double d = vec.x * x + vec.y * y + vec.z * z;

		if (d > 1 && d < 1.00001) {
			d = 1;
		}
		else if (d < -1 && d > -1.00001) {
			d = -1;
		}
		return d;
	}

	public Vector3 crossProduct(Vector3 vec) {
		double d = y * vec.z - z * vec.y;
		double d1 = z * vec.x - x * vec.z;
		double d2 = x * vec.y - y * vec.x;
		x = d;
		y = d1;
		z = d2;
		return this;
	}

	public Vector3 perpendicular() {
		if (z == 0) {
			return zCrossProduct();
		}
		return xCrossProduct();
	}

	public Vector3 xCrossProduct() {
		double d = z;
		double d1 = -y;
		x = 0;
		y = d;
		z = d1;
		return this;
	}

	public Vector3 zCrossProduct() {
		double d = y;
		double d1 = -x;
		x = d;
		y = d1;
		z = 0;
		return this;
	}

	public Vector3 yCrossProduct() {
		double d = -z;
		double d1 = x;
		x = d;
		y = 0;
		z = d1;
		return this;
	}

	public double scalarProject(Vector3 b) {
		double l = b.mag();
		return l == 0 ? 0 : dotProduct(b) / l;
	}

	public Vector3 project(Vector3 b) {
		double l = b.magSquared();
		if (l == 0) {
			set(0, 0, 0);
			return this;
		}
		double m = dotProduct(b) / l;
		set(b).multiply(m);
		return this;
	}

	public Vector3 rotate(double angle, Vector3 axis) {
		Quat.aroundAxis(axis.copy().normalize(), angle).rotate(this);
		return this;
	}

	public Vector3 rotate(Quat rotator) {
		rotator.rotate(this);
		return this;
	}

	public double angle(Vector3 vec) {
		return Math.acos(copy().normalize().dotProduct(vec.copy().normalize()));
	}

	public Vector3 YZintercept(Vector3 end, double px) {
		double dx = end.x - x;
		double dy = end.y - y;
		double dz = end.z - z;

		if (dx == 0) {
			return null;
		}

		double d = (px - x) / dx;
		if (MathUtils.between(-1E-5, d, 1E-5)) {
			return this;
		}

		if (!MathUtils.between(0, d, 1)) {
			return null;
		}

		x = px;
		y += d * dy;
		z += d * dz;
		return this;
	}

	public Vector3 XZintercept(Vector3 end, double py) {
		double dx = end.x - x;
		double dy = end.y - y;
		double dz = end.z - z;

		if (dy == 0) {
			return null;
		}

		double d = (py - y) / dy;
		if (MathUtils.between(-1E-5, d, 1E-5)) {
			return this;
		}

		if (!MathUtils.between(0, d, 1)) {
			return null;
		}

		x += d * dx;
		y = py;
		z += d * dz;
		return this;
	}

	public Vector3 XYintercept(Vector3 end, double pz) {
		double dx = end.x - x;
		double dy = end.y - y;
		double dz = end.z - z;

		if (dz == 0) {
			return null;
		}

		double d = (pz - z) / dz;
		if (MathUtils.between(-1E-5, d, 1E-5)) {
			return this;
		}

		if (!MathUtils.between(0, d, 1)) {
			return null;
		}

		x += d * dx;
		y += d * dy;
		z = pz;
		return this;
	}

	public boolean isZero() {
		return x == 0 && y == 0 && z == 0;
	}

	public boolean isAxial() {
		return x == 0 ? (y == 0 || z == 0) : (y == 0 && z == 0);
	}

	public double getSide(int side) {
		switch (side) {
		case 0:
		case 1:
			return y;
		case 2:
		case 3:
			return z;
		case 4:
		case 5:
			return x;
		}
		throw new IndexOutOfBoundsException("Switch Falloff");
	}

	public Vector3 setSide(int s, double v) {
		switch (s) {
		case 0:
		case 1:
			y = v;
			break;
		case 2:
		case 3:
			z = v;
			break;
		case 4:
		case 5:
			x = v;
			break;
		default:
			throw new IndexOutOfBoundsException("Switch Falloff");
		}
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Vector3)) {
			return false;
		}
		Vector3 v = (Vector3) o;
		return x == v.x && y == v.y && z == v.z;
	}

	/**
	 * Equals method with tolerance
	 *
	 * @return true if this is equal to v within +-1E-5
	 */
	public boolean equalsT(Vector3 v) {
		return MathUtils.between(x - 1E-5, v.x, x + 1E-5) && MathUtils.between(y - 1E-5, v.y, y + 1E-5) && MathUtils.between(z - 1E-5, v.z, z + 1E-5);
	}

	@Override
	public Vector3 copy() {
		return new Vector3(this);
	}

	@Override
	public String toString() {
		MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
		return "Vector3(" + new BigDecimal(x, cont) + ", " + new BigDecimal(y, cont) + ", " + new BigDecimal(z, cont) + ")";
	}

	/*
		    public Translation translation() {
		        return new Translation(this);
		    }
	
		    public Vector3 apply(Transformation t) {
		        t.apply(this);
		        return this;
		    }
	*/
	public Vector3 $tilde() {
		return normalize();
	}

	public Vector3 unary_$tilde() {
		return normalize();
	}

	public Vector3 $plus(Vector3 v) {
		return add(v);
	}

	public Vector3 $minus(Vector3 v) {
		return subtract(v);
	}

	public Vector3 $times(double d) {
		return multiply(d);
	}

	public Vector3 $div(double d) {
		return multiply(1 / d);
	}

	public Vector3 $times(Vector3 v) {
		return crossProduct(v);
	}

	public double $dot$times(Vector3 v) {
		return dotProduct(v);
	}

	public static class Quat {

		public double i;
		public double j;
		public double k;
		public double s;

		public Quat() {
			this(1D);
		}

		public Quat(double zeroMag) {
			s = zeroMag;
			i = 0D;
			j = 0D;
			k = 0D;
		}

		public Quat(Quat quat) {
			i = quat.i;
			j = quat.j;
			k = quat.k;
			s = quat.s;
		}

		public Quat(double w, double i, double j, double k) {
			this.i = i;
			this.j = j;
			this.k = k;
			s = w;
		}

		public void set(Quat quat) {
			i = quat.i;
			j = quat.j;
			k = quat.k;
			s = quat.s;
		}

		public Quat set(double d, double d1, double d2, double d3) {
			i = d1;
			j = d2;
			k = d3;
			s = d;

			return this;
		}

		public void rotate(Vector3 vec) {
			double d = -i * vec.x - j * vec.y - k * vec.z;
			double d1 = s * vec.x + j * vec.z - k * vec.y;
			double d2 = s * vec.y - i * vec.z + k * vec.x;
			double d3 = s * vec.z + i * vec.y - j * vec.x;
			vec.x = d1 * s - d * i - d2 * k + d3 * j;
			vec.y = d2 * s - d * j + d1 * k - d3 * i;
			vec.z = d3 * s - d * k - d1 * j + d2 * i;
		}

		public static Quat buildQuatWithAngle(double ax, double ay, double az, double angle) {
			angle *= 0.5D;
			double d4 = Math.sin(angle);
			return new Quat(Math.cos(angle), ax * d4, ay * d4, az * d4);
		}

		public void leftMultiply(Quat quat) {
			double d = s * quat.s - i * quat.i - j * quat.j - k * quat.k;
			double d1 = s * quat.i + i * quat.s - j * quat.k + k * quat.j;
			double d2 = s * quat.j + i * quat.k + j * quat.s - k * quat.i;
			double d3 = s * quat.k - i * quat.j + j * quat.i + k * quat.s;
			s = d;
			i = d1;
			j = d2;
			k = d3;
		}

		public void rightMultiply(Quat quat) {
			double d = s * quat.s - i * quat.i - j * quat.j - k * quat.k;
			double d1 = s * quat.i + i * quat.s + j * quat.k - k * quat.j;
			double d2 = s * quat.j - i * quat.k + j * quat.s + k * quat.i;
			double d3 = s * quat.k + i * quat.j - j * quat.i + k * quat.s;
			s = d;
			i = d1;
			j = d2;
			k = d3;
		}

		public double mag() {
			return Math.sqrt(i * i + j * j + k * k + s * s);
		}

		public void normalize() {
			double d = mag();
			if (d == 0.0D) {
				return;
			}
			d = 1.0D / d;
			i *= d;
			j *= d;
			k *= d;
			s *= d;
		}

		public void rotateWithMagnitude(Vector3 vec) {
			double d = -i * vec.x - j * vec.y - k * vec.z;
			double d1 = s * vec.x + j * vec.z - k * vec.y;
			double d2 = s * vec.y - i * vec.z + k * vec.x;
			double d3 = s * vec.z + i * vec.y - j * vec.x;
			vec.x = (d1 * s - d * i - d2 * k + d3 * j);
			vec.y = (d2 * s - d * j + d1 * k - d3 * i);
			vec.z = (d3 * s - d * k - d1 * j + d2 * i);
		}

		@Override
		public String toString() {
			return String.format("Quaternion: { s=%f, i=%f, j=%f, k=%f }", s, i, j, k);
		}

		public static Quat buildQuatFrom3DVector(Vector3 axis, double angle) {
			return buildQuatWithAngle(axis.x, axis.y, axis.z, angle);
		}

		public static Quat aroundAxis(double ax, double ay, double az, double angle) {
			return new Quat().setAroundAxis(ax, ay, az, angle);
		}

		public static Quat aroundAxis(Vector3 axis, double angle) {
			return aroundAxis(axis.x, axis.y, axis.z, angle);
		}

		public Quat setAroundAxis(double ax, double ay, double az, double angle) {
			angle *= 0.5;
			double d4 = MathUtils.sin((float) angle);
			return set(MathUtils.cos((float) angle), ax * d4, ay * d4, az * d4);
		}

		public Quat setAroundAxis(Vector3 axis, double angle) {
			return setAroundAxis(axis.x, axis.y, axis.z, angle);
		}

	}
	/*
		private static final Random RAND = new Random();
	
		protected double x;
		protected double y;
		protected double z;
	
		public Vector3() {
			x = 0.0D;
			y = 0.0D;
			z = 0.0D;
		}
	
		public Vector3(Vector3 copy) {
			x = copy.x;
			y = copy.y;
			z = copy.z;
		}
	
		public Vector3(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	
		public Vector3(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	
		public Vector3(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	
		public Vector3(BlockPos pos) {
			this(pos.getX(), pos.getY(), pos.getZ());
		}
	
		public Vector3(Entity entity) {
			this(entity.posX, entity.posY, entity.posZ);
		}
	
		public Vector3(TileEntity te) {
			this(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
		}
	
		public Vector3(Vec3d vec) {
			this(vec.xCoord, vec.yCoord, vec.zCoord);
		}
	
		public Vector3 add(Vec3i vec) {
			x += vec.getX();
			y += vec.getY();
			z += vec.getZ();
			return this;
		}
	
		public Vector3 add(Vector3 vec) {
			x += vec.x;
			y += vec.y;
			z += vec.z;
			return this;
		}
	
		public Vector3 add(double x, double y, double z) {
			this.x += x;
			this.y += y;
			this.z += z;
			return this;
		}
	
		public Vector3 addX(double x) {
			this.x += x;
			return this;
		}
	
		public Vector3 addY(double y) {
			this.y += y;
			return this;
		}
	
		public Vector3 addZ(double z) {
			this.z += z;
			return this;
		}
	
		public Vector3 subtract(double vX, double vY, double vZ) {
			x -= vX;
			y -= vY;
			z -= vZ;
			return this;
		}
	
		public Vector3 subtract(Entity e) {
			x -= e.posX;
			y -= e.posY;
			z -= e.posZ;
			return this;
		}
	
		public Vector3 subtract(Vec3i vec) {
			x -= vec.getX();
			y -= vec.getY();
			z -= vec.getZ();
			return this;
		}
	
		public Vector3 subtract(Vector3 vec) {
			x -= vec.x;
			y -= vec.y;
			z -= vec.z;
			return this;
		}
	
		public Vector3 multiply(Vector3 vec) {
			x *= vec.x;
			y *= vec.y;
			z *= vec.z;
			return this;
		}
	
		public Vector3 divide(Vector3 vec) {
			x /= vec.x;
			y /= vec.y;
			z /= vec.z;
			return this;
		}
	
		public Vector3 divide(double divisor) {
			x /= divisor;
			y /= divisor;
			z /= divisor;
			return this;
		}
	
		public Vector3 negate() {
			x *= -1;
			y *= -1;
			z *= -1;
			return this;
		}
	
		public Vector3 copy(Vector3 vec) {
			x = vec.x;
			y = vec.y;
			z = vec.z;
			return this;
		}
	
		public double length() {
			return Math.sqrt(lengthSquared());
		}
	
		public double lengthSquared() {
			return x * x + y * y + z * z;
		}
	
		public double distance(Vector3 o) {
			return Math.sqrt(distanceSquared(o));
		}
	
		public double distanceSquared(Vector3 o) {
			double difX = x - o.x;
			double difY = y - o.y;
			double difZ = z - o.z;
			return difX * difX + difY * difY + difZ * difZ;
		}
	
		public double distance(Vec3i o) {
			return Math.sqrt(distanceSquared(o));
		}
	
		public double distanceSquared(Vec3i o) {
			double difX = x - o.getX();
			double difY = y - o.getY();
			double difZ = z - o.getZ();
			return difX * difX + difY * difY + difZ * difZ;
		}
	
		public double distance(Vec3d o) {
			return Math.sqrt(distanceSquared(o));
		}
	
		public double distanceSquared(Vec3d o) {
			double difX = x - o.xCoord;
			double difY = y - o.yCoord;
			double difZ = z - o.zCoord;
			return difX * difX + difY * difY + difZ * difZ;
		}
	
		public float angle(Vector3 other) {
			double dot = dot(other) / (length() * other.length());
	
			return (float) Math.acos(dot);
		}
	
		public Vector3 midpoint(Vector3 other) {
			x = ((x + other.x) / 2.0D);
			y = ((y + other.y) / 2.0D);
			z = ((z + other.z) / 2.0D);
			return this;
		}
	
		public Vector3 getMidpoint(Vector3 other) {
			double x = (this.x + other.x) / 2.0D;
			double y = (this.y + other.y) / 2.0D;
			double z = (this.z + other.z) / 2.0D;
			return new Vector3(x, y, z);
		}
	
		public Vector3 multiply(int m) {
			x *= m;
			y *= m;
			z *= m;
			return this;
		}
	
		public Vector3 multiply(double m) {
			x *= m;
			y *= m;
			z *= m;
			return this;
		}
	
		public Vector3 multiply(float m) {
			x *= m;
			y *= m;
			z *= m;
			return this;
		}
	
		public double dot(Vector3 other) {
			return x * other.x + y * other.y + z * other.z;
		}
	
		public Vector3 crossProduct(Vector3 o) {
			double newX = y * o.z - o.y * z;
			double newY = z * o.x - o.z * x;
			double newZ = x * o.y - o.x * y;
	
			x = newX;
			y = newY;
			z = newZ;
			return this;
		}
	
		public Vector3 perpendicular() {
			if (z == 0.0D) {
				return zCrossProduct();
			}
			return xCrossProduct();
		}
	
		public Vector3 xCrossProduct() {
			double d = z;
			double d1 = -y;
			x = 0.0D;
			y = d;
			z = d1;
			return this;
		}
	
		public Vector3 zCrossProduct() {
			double d = y;
	
			double d1 = -x;
			x = d;
			y = d1;
			z = 0.0D;
			return this;
		}
	
		public Vector3 yCrossProduct() {
			double d = -z;
			double d1 = x;
			x = d;
			y = 0.0D;
			z = d1;
			return this;
		}
	
		//In rad's
		public Vector3 rotate(double angle, Vector3 axis) {
			Quat.buildQuatFrom3DVector(axis.clone().normalize(), angle).rotateWithMagnitude(this);
			return this;
		}
	
		public Vector3 normalize() {
			double length = length();
			x /= length;
			y /= length;
			z /= length;
			return this;
		}
	
		public Vector3 fastLowAccNormalize() {
			double lengthSq = lengthSquared();
			lengthSq = fastInvSqrt(lengthSq);
	
			x *= lengthSq;
			y *= lengthSq;
			z *= lengthSq;
			return this;
		}
	
		//x -> about 1/sqrt(x)
		//~50% faster than 1/Math.sqrt(x) in its ~3-4th iterations for about the same numbers.
		public static double fastInvSqrt(double x) {
			double xhalf = 0.5d * x;
			long i = Double.doubleToLongBits(x);
			i = 0x5fe6ec85e7de30daL - (i >> 1);
			x = Double.longBitsToDouble(i);
			for (int it = 0; it < 4; it++) {
				x = x * (1.5d - xhalf * x * x);
			}
			return x;
		}
	
		public Vector3 zero() {
			x = 0.0D;
			y = 0.0D;
			z = 0.0D;
			return this;
		}
	
		public void toBytes(ByteBuf buf) {
			buf.writeDouble(x);
			buf.writeDouble(y);
			buf.writeDouble(z);
		}
	
		public static Vector3 fromBytes(ByteBuf buf) {
			return new Vector3(buf.readDouble(), buf.readDouble(), buf.readDouble());
		}
	
		public static Vector3 random() {
			return new Vector3(RAND.nextDouble() * (RAND.nextBoolean() ? 1 : -1), RAND.nextDouble() * (RAND.nextBoolean() ? 1 : -1), RAND.nextDouble() * (RAND.nextBoolean() ? 1 : -1));
		}
	
		public static Vector3 positiveYRandom() {
			return random().setY(Math.abs(random().getY()));
		}
	*/
	//RIP ChunkCoordinates BibleThump
	/*public static Vector3 fromCC(ChunkCoordinates cc) {
	    return new Vector3(cc.posX, cc.posY, cc.posZ);
	}
	public ChunkCoordinates getAsFloatCC() {
	    return new ChunkCoordinates(Float.floatToIntBits((float) this.x), Float.floatToIntBits((float) this.y), Float.floatToIntBits((float) this.z));
	}
	public static Vector3 getFromFloatCC(ChunkCoordinates cc) {
	    return new Vector3(Float.intBitsToFloat(cc.posX), Float.intBitsToFloat(cc.posY), Float.intBitsToFloat(cc.posZ));
	}*/
	/*
		public boolean isInAABB(Vector3 min, Vector3 max) {
			return (x >= min.x) && (x <= max.x) && (y >= min.y) && (y <= max.y) && (z >= min.z) && (z <= max.z);
		}
	
		public boolean isInSphere(Vector3 origin, double radius) {
			double difX = origin.x - x;
			double difY = origin.y - y;
			double difZ = origin.z - z;
			return (difX * difX + difY * difY + difZ * difZ) <= (radius * radius);
		}
	
		public Vec3d toVec3d() {
			return new Vec3d(x, y, z);
		}
	
		public BlockPos toBlockPos() {
			return new BlockPos(MathUtils.floor(x), MathUtils.floor(y), MathUtils.floor(z));
		}
	
		public Vector3 vectorFromHereTo(Vector3 target) {
			return new Vector3(target.x - x, target.y - y, target.z - z);
		}
	
		public Vector3 vectorFromHereTo(double tX, double tY, double tZ) {
			return new Vector3(tX - x, tY - y, tZ - z);
		}
	
		//copy & converts to polar coordinates (in degrees)
		//Order: Distance, Theta, Phi
		public Vector3 copyToPolar() {
			double length = length();
			double theta = Math.acos(y / length);
			double phi = Math.atan2(x, z);
			theta = Math.toDegrees(theta);
			phi = 180 + Math.toDegrees(phi);
			return new Vector3(length, theta, phi);
		}
	
		public Vector3 copyInterpolateWith(Vector3 next, float partial) {
			return new Vector3((x == next.x ? x : x + ((next.x - x) * partial)), (y == next.y ? y : y + ((next.y - y) * partial)), (z == next.z ? z : z + ((next.z - z) * partial)));
		}
	
		public double getX() {
			return x;
		}
	
		public int getBlockX() {
			return (int) Math.floor(x);
		}
	
		public double getY() {
			return y;
		}
	
		public int getBlockY() {
			return (int) Math.floor(y);
		}
	
		public double getZ() {
			return z;
		}
	
		public int getBlockZ() {
			return (int) Math.floor(z);
		}
	
		public Vector3 setX(int x) {
			this.x = x;
			return this;
		}
	
		public Vector3 setX(double x) {
			this.x = x;
			return this;
		}
	
		public Vector3 setX(float x) {
			this.x = x;
			return this;
		}
	
		public Vector3 setY(int y) {
			this.y = y;
			return this;
		}
	
		public Vector3 setY(double y) {
			this.y = y;
			return this;
		}
	
		public Vector3 setY(float y) {
			this.y = y;
			return this;
		}
	
		public Vector3 setZ(int z) {
			this.z = z;
			return this;
		}
	
		public Vector3 setZ(double z) {
			this.z = z;
			return this;
		}
	
		public Vector3 setZ(float z) {
			this.z = z;
			return this;
		}
	
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Vector3)) {
				return false;
			}
			Vector3 other = (Vector3) obj;
	
			return (Math.abs(x - other.x) < 1.0E-004D) && (Math.abs(y - other.y) < 1.0E-004D) && (Math.abs(z - other.z) < 1.0E-004D) && (getClass().equals(obj.getClass()));
		}
	
		@Override
		public int hashCode() {
			int hash = 7;
	
			hash = 79 * hash + (int) (Double.doubleToLongBits(x) ^ Double.doubleToLongBits(x) >>> 32);
			hash = 79 * hash + (int) (Double.doubleToLongBits(y) ^ Double.doubleToLongBits(y) >>> 32);
			hash = 79 * hash + (int) (Double.doubleToLongBits(z) ^ Double.doubleToLongBits(z) >>> 32);
			return hash;
		}
	
		@Override
		public Vector3 clone() {
			return new Vector3(x, y, z);
		}
	
		@Override
		public String toString() {
			return x + "," + y + "," + z;
		}
	
		public static class Quat {
	
			public double i;
			public double j;
			public double k;
			public double s;
	
			public Quat() {
				this(1D);
			}
	
			public Quat(double zeroMag) {
				s = zeroMag;
				i = 0D;
				j = 0D;
				k = 0D;
			}
	
			public Quat(Quat quat) {
				i = quat.i;
				j = quat.j;
				k = quat.k;
				s = quat.s;
			}
	
			public Quat(double w, double i, double j, double k) {
				this.i = i;
				this.j = j;
				this.k = k;
				s = w;
			}
	
			public void set(Quat quat) {
				i = quat.i;
				j = quat.j;
				k = quat.k;
				s = quat.s;
			}
	
			public static Quat buildQuatWithAngle(double ax, double ay, double az, double angle) {
				angle *= 0.5D;
				double d4 = Math.sin(angle);
				return new Quat(Math.cos(angle), ax * d4, ay * d4, az * d4);
			}
	
			public void leftMultiply(Quat quat) {
				double d = s * quat.s - i * quat.i - j * quat.j - k * quat.k;
				double d1 = s * quat.i + i * quat.s - j * quat.k + k * quat.j;
				double d2 = s * quat.j + i * quat.k + j * quat.s - k * quat.i;
				double d3 = s * quat.k - i * quat.j + j * quat.i + k * quat.s;
				s = d;
				i = d1;
				j = d2;
				k = d3;
			}
	
			public void rightMultiply(Quat quat) {
				double d = s * quat.s - i * quat.i - j * quat.j - k * quat.k;
				double d1 = s * quat.i + i * quat.s + j * quat.k - k * quat.j;
				double d2 = s * quat.j - i * quat.k + j * quat.s + k * quat.i;
				double d3 = s * quat.k + i * quat.j - j * quat.i + k * quat.s;
				s = d;
				i = d1;
				j = d2;
				k = d3;
			}
	
			public double mag() {
				return Math.sqrt(i * i + j * j + k * k + s * s);
			}
	
			public void normalize() {
				double d = mag();
				if (d == 0.0D) {
					return;
				}
				d = 1.0D / d;
				i *= d;
				j *= d;
				k *= d;
				s *= d;
			}
	
			public void rotateWithMagnitude(Vector3 vec) {
				double d = -i * vec.x - j * vec.y - k * vec.z;
				double d1 = s * vec.x + j * vec.z - k * vec.y;
				double d2 = s * vec.y - i * vec.z + k * vec.x;
				double d3 = s * vec.z + i * vec.y - j * vec.x;
				vec.x = (d1 * s - d * i - d2 * k + d3 * j);
				vec.y = (d2 * s - d * j + d1 * k - d3 * i);
				vec.z = (d3 * s - d * k - d1 * j + d2 * i);
			}
	
			@Override
			public String toString() {
				return String.format("Quaternion: { s=%f, i=%f, j=%f, k=%f }", s, i, j, k);
			}
	
			public static Quat buildQuatFrom3DVector(Vector3 axis, double angle) {
				return buildQuatWithAngle(axis.x, axis.y, axis.z, angle);
			}
	
		}
	
		public static class RotAxis {
	
			public static final Vector3 X_AXIS = new Vector3(1, 0, 0);
			public static final Vector3 Y_AXIS = new Vector3(0, 1, 0);
			public static final Vector3 Z_AXIS = new Vector3(0, 0, 1);
	
		}
	*/
}