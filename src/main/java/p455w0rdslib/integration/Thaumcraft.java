package p455w0rdslib.integration;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rdslib.LibGlobals.Mods;
import thaumcraft.client.lib.events.RenderEventHandler;

/**
 * @author p455w0rd
 *
 */
public class Thaumcraft {

	@SideOnly(Side.CLIENT)
	public static void renderAspectsOnTooltip(final GuiContainer gui, final List<String> lines, final ItemStack stack, final int x, final int y) {
		if (Mods.THAUMCRAFT.isLoaded()) {
			GlStateManager.disableDepth();
			int k = 8;
			if (lines.size() > 1) {
				k += 2 + (lines.size() - 1) * 10;
			}
			int bottom = k - 12;
			for (int a = lines.size() - 1; a >= 0; --a) {
				if (lines.get(a) != null && !lines.get(a).contains("    ")) {
					bottom -= 10;
					continue;
				}
				if (a <= 0 || lines.get(a - 1) == null || !lines.get(a - 1).contains("    ")) {
					continue;
				}
				RenderEventHandler.hudHandler.renderAspectsInGui(gui, Minecraft.getMinecraft().player, stack, bottom, x + 12, y);
				break;
			}
			GlStateManager.enableDepth();
		}
	}

}
