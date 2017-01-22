package p455w0rdslib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author p455w0rd
 *
 */
@SideOnly(Side.CLIENT)
public class RenderUtils {

	public static void renderHighlightText(ScaledResolution scaledRes, int yOffset, String text) {
		Minecraft mc = Minecraft.getMinecraft();
		String s = TextFormatting.ITALIC + "" + text;

		int i = (scaledRes.getScaledWidth() - getFontRenderer().getStringWidth(text)) / 2;
		int j = scaledRes.getScaledHeight() - 59 - yOffset;

		if (!mc.playerController.shouldDrawHUD()) {
			j += 14;
		}

		int k = 255;
		GlStateManager.pushMatrix();
		//GlStateManager.enableBlend();
		//GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		getFontRenderer().drawStringWithShadow(s, i, j, 16777215 + (k << 24));
		//GlStateManager.disableBlend();
		GlStateManager.popMatrix();

	}

	public static FontRenderer getFontRenderer() {
		return Minecraft.getMinecraft().fontRendererObj;
	}

}
