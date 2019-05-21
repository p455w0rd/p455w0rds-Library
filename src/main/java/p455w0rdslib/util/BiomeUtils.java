package p455w0rdslib.util;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.biome.Biome;

/**
 * @author p455w0rd
 *
 */
public class BiomeUtils {

	public static void enableRain(final Biome biome) {
		//MCPrivateUtils.setRainEnabled(biome, true);
		biome.enableRain = true;
	}

	public static void disableRain(final Biome biome) {
		biome.enableRain = false;
	}

	public static List<Biome> getBiomeList() {
		final List<Biome> biomes = Lists.newArrayList();
		final Iterator<Biome> biomeList = Biome.REGISTRY.iterator();
		while (biomeList.hasNext()) {
			biomes.add(biomeList.next());
		}
		return biomes;
	}

}
