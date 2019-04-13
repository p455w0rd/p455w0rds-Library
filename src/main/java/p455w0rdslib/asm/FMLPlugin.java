package p455w0rdslib.asm;

import com.elytradev.mini.MiniCoremod;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

/**
 * @author p455w0rd
 *
 */
@IFMLLoadingPlugin.TransformerExclusions({
		"com.elytradev.mini", "com.elytradev.mirage.asm", "p455w0rdslib.asm"
})
@IFMLLoadingPlugin.MCVersion(Loader.MC_VERSION)
@IFMLLoadingPlugin.Name("p455w0rdsCore")
@IFMLLoadingPlugin.SortingIndex(1002)
public class FMLPlugin extends MiniCoremod {

	public FMLPlugin() {
		super(ItemStackTransformer.class, HorseSaddleFixerTransformer.class);
	}

}