package p455w0rdslib.asm;

import java.util.Map;

import org.apache.logging.log4j.LogManager;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

/**
 * @author p455w0rd
 *
 */
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name(FMLPlugin.COREMODID)
@IFMLLoadingPlugin.TransformerExclusions({
		"p455w0rdslib.asm"
})
@IFMLLoadingPlugin.SortingIndex(Integer.MAX_VALUE)
public class FMLPlugin implements IFMLLoadingPlugin {

	public static final String COREMODID = "p455w0rdc0re";
	public static boolean isDeobf = false;

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {
				"p455w0rdslib.asm.ClassTransformer"
		};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(final Map<String, Object> data) {
		isDeobf = !(Boolean) data.get("runtimeDeobfuscationEnabled");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

	public static void log(final String msg) {
		LogManager.getLogger(FMLPlugin.COREMODID).info(msg);
	}

	public static void log(final String msg, final Object... format) {
		LogManager.getLogger(FMLPlugin.COREMODID).info(msg, format);
	}

}