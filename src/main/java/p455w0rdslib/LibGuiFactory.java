package p455w0rdslib;

import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import p455w0rdslib.client.gui.GuiLibConfig;

/**
 * @author p455w0rd
 *
 */
public class LibGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(final Minecraft minecraftInstance) {
	}

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(final GuiScreen parentScreen) {
		return new GuiLibConfig(parentScreen);
	}

	@Nullable
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

}