package p455w0rdslib.util;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.Method;

/**
 * @author p455w0rd
 *
 */
@Interface(iface = "net.darkhax.tesla.api.ITeslaConsumer", modid = "tesla", striprefs = true)
public class TeslaDetectionUtils {

	@Method(modid = "tesla")
	public static boolean isTesla(Capability<?> capability) {
		if (Loader.isModLoaded("tesla")) {
			return capability == TeslaCapabilities.CAPABILITY_PRODUCER || capability == TeslaCapabilities.CAPABILITY_HOLDER || capability == TeslaCapabilities.CAPABILITY_CONSUMER;
		}
		return false;
	}

	@Method(modid = "tesla")
	public static <T> T getInfiniteConsumer(Capability<T> capability) {
		ITeslaConsumer invTesla = (power, simulated) -> power;
		if (capability == TeslaCapabilities.CAPABILITY_CONSUMER) {
			return TeslaCapabilities.CAPABILITY_CONSUMER.cast(invTesla);
		}
		return null;
	}

}
