package p455w0rdslib.client.gui;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import p455w0rdslib.LibConfig;
import p455w0rdslib.LibGlobals;
import p455w0rdslib.util.TextUtils;

/**
 * @author p455w0rd
 *
 */
public class GuiLibConfig extends GuiConfig {

	public GuiLibConfig(final GuiScreen parent) {
		super(getParent(parent), getConfigElements(), LibGlobals.MODID, false, false, getTitle(parent));
	}

	private static GuiScreen getParent(final GuiScreen parent) {
		return parent;
	}

	private static List<IConfigElement> getConfigElements() {
		final List<IConfigElement> elements = Lists.newArrayList(new ConfigElement(LibConfig.CONFIG.getCategory(LibConfig.CLIENT_CAT)).getChildElements());
		elements.addAll(new ConfigElement(LibConfig.CONFIG.getCategory(LibConfig.SHADER_CAT)).getChildElements());
		return elements;
	}

	private static String getTitle(final GuiScreen parent) {
		return TextUtils.translate(LibGlobals.NAME + " Config");
	}

}