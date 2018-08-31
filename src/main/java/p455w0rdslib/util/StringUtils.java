package p455w0rdslib.util;

import java.util.List;

import com.google.gson.JsonParseException;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author p455w0rd
 *
 */
public class StringUtils {

	@SideOnly(Side.CLIENT)
	public static void renderScaledAsciiString(FontRenderer font, String text, int x, int y, int color, boolean shadow, float scale) {
		String finalStr = text;
		GlStateManager.pushMatrix();
		GlStateManager.scale(scale, scale, scale);
		boolean oldUnicode = font.getUnicodeFlag();
		font.setUnicodeFlag(false);

		//String test = "[\"URL Link Test [\",{\"text\":\"Download\",\"color\":\"green\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"URL Link Test\",\"color\":\"blue\"}},\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://minecraft.curseforge.com/projects/wireless-crafting-terminal\"}},\"]\"]";
		List<ITextComponent> stringList = GuiUtilRenderComponents.splitText(new TextComponentString(ITextComponent.Serializer.componentToJson(new TextComponentString(text))), 116, font, true, true);
		for (ITextComponent str : stringList) {
			try {
				ITextComponent txt = ITextComponent.Serializer.jsonToComponent(str.getUnformattedText());
				//this.cachedComponents = itextcomponent != null ? GuiUtilRenderComponents.splitText(itextcomponent, 116, this.fontRendererObj, true, true) : null;
				finalStr = txt.getFormattedText();
			}
			catch (JsonParseException var13) {
				finalStr = text;
			}
		}

		font.drawString(finalStr, x / scale, y / scale, color, shadow);

		font.setUnicodeFlag(oldUnicode);
		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	public static void renderSplitScaledAsciiString(FontRenderer font, String text, int x, int y, int color, boolean shadow, float scale, int length) {
		List<String> lines = font.listFormattedStringToWidth(text, (int) (length / scale));
		for (int i = 0; i < lines.size(); i++) {
			renderScaledAsciiString(font, lines.get(i), x, y + (i * (int) (font.FONT_HEIGHT * scale + 3)), color, shadow, scale);
		}
	}

}
