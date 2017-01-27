package p455w0rdslib.util;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author p455w0rd
 *
 */
@SideOnly(Side.CLIENT)
public class GuiUtils {

	private static Minecraft mc() {
		return Minecraft.getMinecraft();
	}

	public static RenderItem getRenderItem() {
		return mc().getRenderItem();
	}

	public static TextureManager getTextureManager() {
		return mc().getTextureManager();
	}

	public static FontRenderer getFontRenderer() {
		return mc().fontRendererObj;
	}

	public static void bindTexture(String texturePath) {
		bindTexture(new ResourceLocation(texturePath)); //minecraft resource
	}

	public static void bindTexture(String domain, String texturePath) {
		bindTexture(new ResourceLocation(domain, texturePath));
	}

	public static void bindTexture(ResourceLocation location) {
		getTextureManager().bindTexture(location);
	}

	public static void drawItem(ItemStack item, int x, int y) {
		RenderHelper.enableGUIStandardItemLighting();
		getRenderItem().renderItemAndEffectIntoGUI(item, x, y);
		RenderHelper.enableStandardItemLighting();
	}

	public static void drawVanillaTooltip(GuiScreen gui, List<String> text, int x, int y) {
		drawHoveringText(gui, text, x, y, getFontRenderer(), -267386864, 1347420415, 1344798847);
	}

	public static void drawToolTipWithBorderColor(GuiScreen gui, List<String> text, int x, int y, int borderColor1, int borderColor2) {
		drawHoveringText(gui, text, x, y, getFontRenderer(), -267386864, borderColor1, borderColor2);
	}

	private static void drawHoveringText(GuiScreen gui, List<String> textLines, int x, int y, FontRenderer font, int backgroundColor, int borderColor1, int borderColor2) {
		//net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(textLines, x, y, gui.width, gui.height, -1, font);
		if (!textLines.isEmpty()) {
			GlStateManager.disableRescaleNormal();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			int i = 0;

			for (String s : textLines) {
				int j = getFontRenderer().getStringWidth(s);

				if (j > i) {
					i = j;
				}
			}

			int l1 = x + 12;
			int i2 = y - 12;
			int k = 8;

			if (textLines.size() > 1) {
				k += 2 + (textLines.size() - 1) * 10;
			}

			if (l1 + i > gui.width) {
				l1 -= 28 + i;
			}

			if (i2 + k + 6 > gui.height) {
				i2 = gui.height - k - 6;
			}

			MCPrivateUtils.setGuiZLevel(gui, 300.0F);
			MCPrivateUtils.setGuiScreenRendererZLevel(gui, 300.0F);
			drawGradientRect(gui, l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, backgroundColor, backgroundColor);
			drawGradientRect(gui, l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, backgroundColor, backgroundColor);
			drawGradientRect(gui, l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, backgroundColor, backgroundColor);
			drawGradientRect(gui, l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, backgroundColor, backgroundColor);
			drawGradientRect(gui, l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, backgroundColor, backgroundColor);
			drawGradientRect(gui, l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, borderColor1, borderColor2);
			drawGradientRect(gui, l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, borderColor1, borderColor2);
			drawGradientRect(gui, l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, borderColor1, borderColor1);
			drawGradientRect(gui, l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, borderColor2, borderColor2);

			for (int k1 = 0; k1 < textLines.size(); ++k1) {
				String s1 = textLines.get(k1);
				getFontRenderer().drawStringWithShadow(s1, l1, i2, -1);

				if (k1 == 0) {
					i2 += 2;
				}

				i2 += 10;
			}

			MCPrivateUtils.setGuiZLevel(gui, 0.0F);
			MCPrivateUtils.setGuiScreenRendererZLevel(gui, 0.0F);
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableRescaleNormal();
		}
	}

	public static void drawGradientRect(Gui gui, int left, int top, int right, int bottom, int startColor, int endColor) {
		float f = (startColor >> 24 & 255) / 255.0F;
		float f1 = (startColor >> 16 & 255) / 255.0F;
		float f2 = (startColor >> 8 & 255) / 255.0F;
		float f3 = (startColor & 255) / 255.0F;
		float f4 = (endColor >> 24 & 255) / 255.0F;
		float f5 = (endColor >> 16 & 255) / 255.0F;
		float f6 = (endColor >> 8 & 255) / 255.0F;
		float f7 = (endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		vertexbuffer.pos(right, top, MCPrivateUtils.getGuiZLevel(gui)).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos(left, top, MCPrivateUtils.getGuiZLevel(gui)).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos(left, bottom, MCPrivateUtils.getGuiZLevel(gui)).color(f5, f6, f7, f4).endVertex();
		vertexbuffer.pos(right, bottom, MCPrivateUtils.getGuiZLevel(gui)).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

	public static void drawSlot(GuiContainer gui, Slot slotIn) {
		int i = slotIn.xDisplayPosition;
		int j = slotIn.yDisplayPosition;
		ItemStack itemstack = slotIn.getStack();
		boolean flag = false;
		boolean flag1 = (slotIn == MCPrivateUtils.getGuiClickedSlot(gui)) && (MCPrivateUtils.getGuiDraggedStack(gui) != null) && (!MCPrivateUtils.getGuiIsRightMouseClick(gui));
		ItemStack itemstack1 = mc().thePlayer.inventory.getItemStack();
		String s = null;
		if ((slotIn == MCPrivateUtils.getGuiClickedSlot(gui)) && (MCPrivateUtils.getGuiDraggedStack(gui) != null) && (MCPrivateUtils.getGuiIsRightMouseClick(gui)) && (itemstack != null)) {
			itemstack = itemstack.copy();
			itemstack.stackSize /= 2;
		}
		else if ((MCPrivateUtils.getGuiDragSplitting(gui)) && (MCPrivateUtils.getGuiDragSplittingSlots(gui).contains(slotIn)) && (itemstack1 != null)) {
			if (MCPrivateUtils.getGuiDragSplittingSlots(gui).size() == 1) {
				return;
			}
			if ((Container.canAddItemToSlot(slotIn, itemstack1, true)) && (gui.inventorySlots.canDragIntoSlot(slotIn))) {
				itemstack = itemstack1.copy();
				flag = true;
				Container.computeStackSize(MCPrivateUtils.getGuiDragSplittingSlots(gui), MCPrivateUtils.getGuiDragSplittingLimit(gui), itemstack, slotIn.getStack() == null ? 0 : slotIn.getStack().stackSize);
				if (itemstack.stackSize > itemstack.getMaxStackSize()) {
					s = TextFormatting.YELLOW + "" + itemstack.getMaxStackSize();
					itemstack.stackSize = itemstack.getMaxStackSize();
				}
				if (itemstack.stackSize > slotIn.getItemStackLimit(itemstack)) {
					s = TextFormatting.YELLOW + "" + slotIn.getItemStackLimit(itemstack);
					itemstack.stackSize = slotIn.getItemStackLimit(itemstack);
				}
			}
			else {
				MCPrivateUtils.getGuiDragSplittingSlots(gui).remove(slotIn);
				updateDragSplitting(gui);
			}
		}
		MCPrivateUtils.setGuiZLevel(gui, 100.0F);
		MCPrivateUtils.setGuiScreenRendererZLevel(gui, 100.0F);
		if ((itemstack == null) && (slotIn.canBeHovered())) {
			TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();
			if (textureatlassprite != null) {
				GlStateManager.disableLighting();
				bindTexture(slotIn.getBackgroundLocation());
				drawTexturedModalRect(gui, i, j, textureatlassprite, 16, 16);
				GlStateManager.enableLighting();
				flag1 = true;
			}
		}
		if (!flag1) {
			if (flag) {
				Gui.drawRect(i, j, i + 16, j + 16, -2130706433);
			}
			GlStateManager.enableDepth();

			Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(itemstack, i, j);

			MCPrivateUtils.getGuiScreenRenderItem(gui).renderItemOverlayIntoGUI(getFontRenderer(), itemstack, i, j, s);
		}
		MCPrivateUtils.setGuiScreenRendererZLevel(gui, 0.0F);
		MCPrivateUtils.setGuiZLevel(gui, 0.0F);
	}

	public static void drawTexturedModalRect(Gui gui, int xCoord, int yCoord, TextureAtlasSprite textureSprite, int widthIn, int heightIn) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos(xCoord + 0, yCoord + heightIn, MCPrivateUtils.getGuiZLevel(gui)).tex(textureSprite.getMinU(), textureSprite.getMaxV()).endVertex();
		vertexbuffer.pos(xCoord + widthIn, yCoord + heightIn, MCPrivateUtils.getGuiZLevel(gui)).tex(textureSprite.getMaxU(), textureSprite.getMaxV()).endVertex();
		vertexbuffer.pos(xCoord + widthIn, yCoord + 0, MCPrivateUtils.getGuiZLevel(gui)).tex(textureSprite.getMaxU(), textureSprite.getMinV()).endVertex();
		vertexbuffer.pos(xCoord + 0, yCoord + 0, MCPrivateUtils.getGuiZLevel(gui)).tex(textureSprite.getMinU(), textureSprite.getMinV()).endVertex();
		tessellator.draw();
	}

	public static void updateDragSplitting(GuiContainer gui) {
		ItemStack itemstack = mc().thePlayer.inventory.getItemStack();

		if (itemstack != null && MCPrivateUtils.getGuiDragSplitting(gui)) {
			MCPrivateUtils.setGuiDragSplittingRemnant(gui, itemstack.stackSize);

			for (Slot slot : MCPrivateUtils.getGuiDragSplittingSlots(gui)) {
				ItemStack itemstack1 = itemstack.copy();
				int i = slot.getStack() == null ? 0 : slot.getStack().stackSize;
				Container.computeStackSize(MCPrivateUtils.getGuiDragSplittingSlots(gui), MCPrivateUtils.getGuiDragSplittingLimit(gui), itemstack1, i);

				if (itemstack1.stackSize > itemstack1.getMaxStackSize()) {
					itemstack1.stackSize = itemstack1.getMaxStackSize();
				}

				if (itemstack1.stackSize > slot.getItemStackLimit(itemstack1)) {
					itemstack1.stackSize = slot.getItemStackLimit(itemstack1);
				}

				MCPrivateUtils.setGuiDragSplittingRemnant(gui, MCPrivateUtils.getGuiDragSplittingRemnant(gui) - (itemstack1.stackSize - i));
			}
		}
	}

	public static void drawItemStack(GuiContainer gui, ItemStack stack, int x, int y, String altText) {
		GlStateManager.translate(0.0F, 0.0F, 32.0F);
		MCPrivateUtils.setGuiZLevel(gui, 200.0F);
		MCPrivateUtils.setGuiScreenRendererZLevel(gui, 200.0F);
		FontRenderer font = null;
		if (stack != null) {
			font = stack.getItem().getFontRenderer(stack);
		}
		if (font == null) {
			font = getFontRenderer();
		}
		MCPrivateUtils.getGuiScreenRenderItem(gui).renderItemAndEffectIntoGUI(stack, x, y);
		MCPrivateUtils.getGuiScreenRenderItem(gui).renderItemOverlayIntoGUI(font, stack, x, y - (MCPrivateUtils.getGuiDraggedStack(gui) == null ? 0 : 8), altText);
		MCPrivateUtils.setGuiScreenRendererZLevel(gui, 0.0F);
		MCPrivateUtils.setGuiZLevel(gui, 0.0F);
	}

}