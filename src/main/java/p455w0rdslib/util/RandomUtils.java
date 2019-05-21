package p455w0rdslib.util;

import java.util.Random;

/**
 * @author p455w0rd
 *
 */
public class RandomUtils {

	private static final Random r = new Random();

	public static float randFloat(final float min, final float max) {
		return min + r.nextFloat() * (max - min);
	}

}
