package p455w0rdslib.asm;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

/**
 * @author p455w0rd
 *
 */
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("p455w0rdc0re")
@IFMLLoadingPlugin.TransformerExclusions({
		"p455w0rdslib.asm"
})
@IFMLLoadingPlugin.SortingIndex(1001)
public class FMLPlugin implements IFMLLoadingPlugin {

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
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}