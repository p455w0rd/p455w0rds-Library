package p455w0rdslib.util;

import java.util.Random;
import java.util.UUID;

import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author p455w0rd
 *
 */
public class MathUtils {

	public static float getPercent(final double number, final double total) {
		return (float) (number * 100.0D / total);
	}

	public static int[] longToIntArray(final long value) {
		final int[] digits = Long.toString(value).chars().map(c -> c -= '0').toArray();
		return digits;
	}

	public static long intArrayToLong(final int[] array) {
		final StringBuilder sb = new StringBuilder();
		for (final int element : array) {
			sb.append(element);
		}
		return Long.parseLong(sb.toString());
	}

	public static int getRandom(final int min, final int max) {
		return new Random().nextInt(max - min + 1) + min;
	}

	public static boolean between(final double min, final double value, final double max) {
		return min <= value && value <= max;
	}

	public static final float SQRT_2 = sqrt(2.0F);
	/** A table of sin values computed from 0 (inclusive) to 2*pi (exclusive), with steps of 2*PI / 65536. */
	private static final float[] SIN_TABLE = new float[65536];
	private static final Random RANDOM = new Random();
	/**
	 * Though it looks like an array, this is really more like a mapping.  Key (index of this array) is the upper 5 bits
	 * of the result of multiplying a 32-bit unsigned integer by the B(2, 5) De Bruijn sequence 0x077CB531.  Value
	 * (value stored in the array) is the unique index (from the right) of the leftmost one-bit in a 32-bit unsigned
	 * integer that can cause the upper 5 bits to get that value.  Used for highly optimized "find the log-base-2 of
	 * this number" calculations.
	 */
	private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION;
	private static final double FRAC_BIAS;
	private static final double[] ASINE_TAB;
	private static final double[] COS_TAB;

	/**
	 * sin looked up in a table
	 */
	public static float sin(final float value) {
		return SIN_TABLE[(int) (value * 10430.378F) & 65535];
	}

	/**
	 * cos looked up in the sin table with the appropriate offset
	 */
	public static float cos(final float value) {
		return SIN_TABLE[(int) (value * 10430.378F + 16384.0F) & 65535];
	}

	public static float sqrt(final float value) {
		return (float) Math.sqrt(value);
	}

	public static float sqrt(final double value) {
		return (float) Math.sqrt(value);
	}

	/**
	 * Returns the greatest integer less than or equal to the float argument
	 */
	public static int floor(final float value) {
		final int i = (int) value;
		return value < i ? i - 1 : i;
	}

	/**
	 * returns par0 cast as an int, and no greater than Integer.MAX_VALUE-1024
	 */
	@SideOnly(Side.CLIENT)
	public static int fastFloor(final double value) {
		return (int) (value + 1024.0D) - 1024;
	}

	/**
	 * Returns the greatest integer less than or equal to the double argument
	 */
	public static int floor(final double value) {
		final int i = (int) value;
		return value < i ? i - 1 : i;
	}

	/**
	 * Long version of floor()
	 */
	public static long lfloor(final double value) {
		final long i = (long) value;
		return value < i ? i - 1L : i;
	}

	@SideOnly(Side.CLIENT)
	public static int absFloor(final double value) {
		return (int) (value >= 0.0D ? value : -value + 1.0D);
	}

	public static float abs(final float value) {
		return value >= 0.0F ? value : -value;
	}

	/**
	 * Returns the unsigned value of an int.
	 */
	public static int abs(final int value) {
		return value >= 0 ? value : -value;
	}

	public static int ceil(final float value) {
		final int i = (int) value;
		return value > i ? i + 1 : i;
	}

	public static int ceil(final double value) {
		final int i = (int) value;
		return value > i ? i + 1 : i;
	}

	/**
	 * Returns the value of the first parameter, clamped to be within the lower and upper limits given by the second and
	 * third parameters.
	 */
	public static int clamp(final int num, final int min, final int max) {
		return num < min ? min : num > max ? max : num;
	}

	/**
	 * Returns the value of the first parameter, clamped to be within the lower and upper limits given by the second and
	 * third parameters
	 */
	public static float clamp(final float num, final float min, final float max) {
		return num < min ? min : num > max ? max : num;
	}

	public static double clamp(final double num, final double min, final double max) {
		return num < min ? min : num > max ? max : num;
	}

	public static double clampedLerp(final double lowerBnd, final double upperBnd, final double slide) {
		return slide < 0.0D ? lowerBnd : slide > 1.0D ? upperBnd : lowerBnd + (upperBnd - lowerBnd) * slide;
	}

	/**
	 * Maximum of the absolute value of two numbers.
	 */
	public static double absMax(double p_76132_0_, double p_76132_2_) {
		if (p_76132_0_ < 0.0D) {
			p_76132_0_ = -p_76132_0_;
		}

		if (p_76132_2_ < 0.0D) {
			p_76132_2_ = -p_76132_2_;
		}

		return p_76132_0_ > p_76132_2_ ? p_76132_0_ : p_76132_2_;
	}

	/**
	 * Buckets an integer with specifed bucket sizes.
	 */
	@SideOnly(Side.CLIENT)
	public static int intFloorDiv(final int p_76137_0_, final int p_76137_1_) {
		return p_76137_0_ < 0 ? -((-p_76137_0_ - 1) / p_76137_1_) - 1 : p_76137_0_ / p_76137_1_;
	}

	public static int getInt(final Random random, final int minimum, final int maximum) {
		return minimum >= maximum ? minimum : random.nextInt(maximum - minimum + 1) + minimum;
	}

	public static float nextFloat(final Random random, final float minimum, final float maximum) {
		return minimum >= maximum ? minimum : random.nextFloat() * (maximum - minimum) + minimum;
	}

	public static double nextDouble(final Random random, final double minimum, final double maximum) {
		return minimum >= maximum ? minimum : random.nextDouble() * (maximum - minimum) + minimum;
	}

	public static double average(final long[] values) {
		long i = 0L;

		for (final long j : values) {
			i += j;
		}

		return (double) i / (double) values.length;
	}

	@SideOnly(Side.CLIENT)
	public static boolean epsilonEquals(final float p_180185_0_, final float p_180185_1_) {
		return abs(p_180185_1_ - p_180185_0_) < 1.0E-5F;
	}

	@SideOnly(Side.CLIENT)
	public static int normalizeAngle(final int p_180184_0_, final int p_180184_1_) {
		return (p_180184_0_ % p_180184_1_ + p_180184_1_) % p_180184_1_;
	}

	@SideOnly(Side.CLIENT)
	public static float positiveModulo(final float numerator, final float denominator) {
		return (numerator % denominator + denominator) % denominator;
	}

	/**
	 * the angle is reduced to an angle between -180 and +180 by mod, and a 360 check
	 */
	public static float wrapDegrees(float value) {
		value = value % 360.0F;

		if (value >= 180.0F) {
			value -= 360.0F;
		}

		if (value < -180.0F) {
			value += 360.0F;
		}

		return value;
	}

	/**
	 * the angle is reduced to an angle between -180 and +180 by mod, and a 360 check
	 */
	public static double wrapDegrees(double value) {
		value = value % 360.0D;

		if (value >= 180.0D) {
			value -= 360.0D;
		}

		if (value < -180.0D) {
			value += 360.0D;
		}

		return value;
	}

	/**
	 * Adjust the angle so that his value is in range [-180;180[
	 */
	public static int clampAngle(int angle) {
		angle = angle % 360;

		if (angle >= 180) {
			angle -= 360;
		}

		if (angle < -180) {
			angle += 360;
		}

		return angle;
	}

	/**
	 * parses the string as integer or returns the second parameter if it fails
	 */
	public static int getInt(final String value, final int defaultValue) {
		try {
			return Integer.parseInt(value);
		}
		catch (final Throwable var3) {
			return defaultValue;
		}
	}

	/**
	 * parses the string as integer or returns the second parameter if it fails. this value is capped to par2
	 */
	public static int getInt(final String value, final int defaultValue, final int max) {
		return Math.max(max, getInt(value, defaultValue));
	}

	/**
	 * parses the string as double or returns the second parameter if it fails.
	 */
	public static double getDouble(final String value, final double defaultValue) {
		try {
			return Double.parseDouble(value);
		}
		catch (final Throwable var4) {
			return defaultValue;
		}
	}

	public static double getDouble(final String value, final double defaultValue, final double max) {
		return Math.max(max, getDouble(value, defaultValue));
	}

	/**
	 * Returns the input value rounded up to the next highest power of two.
	 */
	public static int smallestEncompassingPowerOfTwo(final int value) {
		int i = value - 1;
		i = i | i >> 1;
		i = i | i >> 2;
		i = i | i >> 4;
		i = i | i >> 8;
		i = i | i >> 16;
		return i + 1;
	}

	/**
	 * Is the given value a power of two?  (1, 2, 4, 8, 16, ...)
	 */
	private static boolean isPowerOfTwo(final int value) {
		return value != 0 && (value & value - 1) == 0;
	}

	/**
	 * Uses a B(2, 5) De Bruijn sequence and a lookup table to efficiently calculate the log-base-two of the given
	 * value. Optimized for cases where the input value is a power-of-two. If the input value is not a power-of-two,
	 * then subtract 1 from the return value.
	 */
	public static int log2DeBruijn(int value) {
		value = isPowerOfTwo(value) ? value : smallestEncompassingPowerOfTwo(value);
		return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int) (value * 125613361L >> 27) & 31];
	}

	/**
	 * Efficiently calculates the floor of the base-2 log of an integer value.  This is effectively the index of the
	 * highest bit that is set.  For example, if the number in binary is 0...100101, this will return 5.
	 */
	public static int log2(final int value) {
		/**
		 * Uses a B(2, 5) De Bruijn sequence and a lookup table to efficiently calculate the log-base-two of the given
		 * value. Optimized for cases where the input value is a power-of-two. If the input value is not a power-of-two,
		 * then subtract 1 from the return value.
		 */
		return log2DeBruijn(value) - (isPowerOfTwo(value) ? 0 : 1);
	}

	/**
	 * Rounds the first parameter up to the next interval of the second parameter.
	 *
	 * For instance, {@code roundUp(1, 4)} returns 4; {@code roundUp(0, 4)} returns 0; and {@code roundUp(4, 4)} returns
	 * 4.
	 */
	public static int roundUp(final int number, int interval) {
		if (interval == 0) {
			return 0;
		}
		else if (number == 0) {
			return interval;
		}
		else {
			if (number < 0) {
				interval *= -1;
			}

			final int i = number % interval;
			return i == 0 ? number : number + interval - i;
		}
	}

	/**
	 * Makes an integer color from the given red, green, and blue float values
	 */
	@SideOnly(Side.CLIENT)
	public static int rgb(final float rIn, final float gIn, final float bIn) {
		/**
		 * Makes a single int color with the given red, green, and blue values.
		 */
		return rgb(floor(rIn * 255.0F), floor(gIn * 255.0F), floor(bIn * 255.0F));
	}

	/**
	 * Makes a single int color with the given red, green, and blue values.
	 */
	@SideOnly(Side.CLIENT)
	public static int rgb(final int rIn, final int gIn, final int bIn) {
		int lvt_3_1_ = (rIn << 8) + gIn;
		lvt_3_1_ = (lvt_3_1_ << 8) + bIn;
		return lvt_3_1_;
	}

	@SideOnly(Side.CLIENT)
	public static int multiplyColor(final int p_180188_0_, final int p_180188_1_) {
		final int i = (p_180188_0_ & 16711680) >> 16;
		final int j = (p_180188_1_ & 16711680) >> 16;
		final int k = (p_180188_0_ & 65280) >> 8;
		final int l = (p_180188_1_ & 65280) >> 8;
		final int i1 = (p_180188_0_ & 255) >> 0;
		final int j1 = (p_180188_1_ & 255) >> 0;
		final int k1 = (int) ((float) i * (float) j / 255.0F);
		final int l1 = (int) ((float) k * (float) l / 255.0F);
		final int i2 = (int) ((float) i1 * (float) j1 / 255.0F);
		return p_180188_0_ & -16777216 | k1 << 16 | l1 << 8 | i2;
	}

	/**
	 * Gets the decimal portion of the given double. For instance, {@code frac(5.5)} returns {@code .5}.
	 */
	@SideOnly(Side.CLIENT)
	public static double frac(final double number) {
		return number - Math.floor(number);
	}

	@SideOnly(Side.CLIENT)
	public static long getPositionRandom(final Vec3i pos) {
		return getCoordinateRandom(pos.getX(), pos.getY(), pos.getZ());
	}

	public static UUID getRandomUUID(final Random rand) {
		final long i = rand.nextLong() & -61441L | 16384L;
		final long j = rand.nextLong() & 4611686018427387903L | Long.MIN_VALUE;
		return new UUID(i, j);
	}

	/**
	 * Generates a random UUID using the shared random
	 */
	public static UUID getRandomUUID() {
		return getRandomUUID(RANDOM);
	}

	@SideOnly(Side.CLIENT)
	public static long getCoordinateRandom(final int x, final int y, final int z) {
		long i = x * 3129871 ^ z * 116129781L ^ y;
		i = i * i * 42317861L + i * 11L;
		return i;
	}

	public static double pct(final double p_181160_0_, final double p_181160_2_, final double p_181160_4_) {
		return (p_181160_0_ - p_181160_2_) / (p_181160_4_ - p_181160_2_);
	}

	public static double atan2(double p_181159_0_, double p_181159_2_) {
		final double d0 = p_181159_2_ * p_181159_2_ + p_181159_0_ * p_181159_0_;

		if (Double.isNaN(d0)) {
			return Double.NaN;
		}
		else {
			final boolean flag = p_181159_0_ < 0.0D;

			if (flag) {
				p_181159_0_ = -p_181159_0_;
			}

			final boolean flag1 = p_181159_2_ < 0.0D;

			if (flag1) {
				p_181159_2_ = -p_181159_2_;
			}

			final boolean flag2 = p_181159_0_ > p_181159_2_;

			if (flag2) {
				final double d1 = p_181159_2_;
				p_181159_2_ = p_181159_0_;
				p_181159_0_ = d1;
			}

			final double d9 = fastInvSqrt(d0);
			p_181159_2_ = p_181159_2_ * d9;
			p_181159_0_ = p_181159_0_ * d9;
			final double d2 = FRAC_BIAS + p_181159_0_;
			final int i = (int) Double.doubleToRawLongBits(d2);
			final double d3 = ASINE_TAB[i];
			final double d4 = COS_TAB[i];
			final double d5 = d2 - FRAC_BIAS;
			final double d6 = p_181159_0_ * d4 - p_181159_2_ * d5;
			final double d7 = (6.0D + d6 * d6) * d6 * 0.16666666666666666D;
			double d8 = d3 + d7;

			if (flag2) {
				d8 = Math.PI / 2D - d8;
			}

			if (flag1) {
				d8 = Math.PI - d8;
			}

			if (flag) {
				d8 = -d8;
			}

			return d8;
		}
	}

	public static double fastInvSqrt(double p_181161_0_) {
		final double d0 = 0.5D * p_181161_0_;
		long i = Double.doubleToRawLongBits(p_181161_0_);
		i = 6910469410427058090L - (i >> 1);
		p_181161_0_ = Double.longBitsToDouble(i);
		p_181161_0_ = p_181161_0_ * (1.5D - d0 * p_181161_0_ * p_181161_0_);
		return p_181161_0_;
	}

	@SideOnly(Side.CLIENT)
	public static int hsvToRGB(final float hue, final float saturation, final float value) {
		final int i = (int) (hue * 6.0F) % 6;
		final float f = hue * 6.0F - i;
		final float f1 = value * (1.0F - saturation);
		final float f2 = value * (1.0F - f * saturation);
		final float f3 = value * (1.0F - (1.0F - f) * saturation);
		float f4;
		float f5;
		float f6;

		switch (i) {
		case 0:
			f4 = value;
			f5 = f3;
			f6 = f1;
			break;
		case 1:
			f4 = f2;
			f5 = value;
			f6 = f1;
			break;
		case 2:
			f4 = f1;
			f5 = value;
			f6 = f3;
			break;
		case 3:
			f4 = f1;
			f5 = f2;
			f6 = value;
			break;
		case 4:
			f4 = f3;
			f5 = f1;
			f6 = value;
			break;
		case 5:
			f4 = value;
			f5 = f1;
			f6 = f2;
			break;
		default:
			throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
		}

		final int j = clamp((int) (f4 * 255.0F), 0, 255);
		final int k = clamp((int) (f5 * 255.0F), 0, 255);
		final int l = clamp((int) (f6 * 255.0F), 0, 255);
		return j << 16 | k << 8 | l;
	}

	public static int hash(int p_188208_0_) {
		p_188208_0_ = p_188208_0_ ^ p_188208_0_ >>> 16;
		p_188208_0_ = p_188208_0_ * -2048144789;
		p_188208_0_ = p_188208_0_ ^ p_188208_0_ >>> 13;
		p_188208_0_ = p_188208_0_ * -1028477387;
		p_188208_0_ = p_188208_0_ ^ p_188208_0_ >>> 16;
		return p_188208_0_;
	}

	static {
		for (int i = 0; i < 65536; ++i) {
			SIN_TABLE[i] = (float) Math.sin(i * Math.PI * 2.0D / 65536.0D);
		}

		MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[] {
				0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9
		};
		FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);
		ASINE_TAB = new double[257];
		COS_TAB = new double[257];

		for (int j = 0; j < 257; ++j) {
			final double d0 = j / 256.0D;
			final double d1 = Math.asin(d0);
			COS_TAB[j] = Math.cos(d1);
			ASINE_TAB[j] = d1;
		}
	}

	public static double getDistanceSq(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
		return Math.pow(x1 - x2, 2.0) + Math.pow(y1 - y2, 2.0) + Math.pow(z1 - z2, 2.0);
	}

}
