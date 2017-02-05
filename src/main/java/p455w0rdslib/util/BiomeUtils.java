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

	public static void enableRain(Biome biome) {
		MCPrivateUtils.setRainEnabled(biome, true);
	}

	public static void disableRain(Biome biome) {
		MCPrivateUtils.setRainEnabled(biome, false);
	}

	public static List<Biome> getBiomeList() {
		List<Biome> biomes = Lists.newArrayList();
		Iterator<Biome> biomeList = Biome.REGISTRY.iterator();
		while (biomeList.hasNext()) {
			biomes.add(biomeList.next());
		}
		return biomes;
	}

}
