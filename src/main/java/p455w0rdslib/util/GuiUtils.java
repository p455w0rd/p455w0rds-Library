package p455w0rdslib.util;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author p455w0rd
 *
 */
@SideOnly(Side.CLIENT)
public class GuiUtils {

	private static Minecraft mc() {
		return MCUtils.mc();
	}

	public static void bindTexture(final String texturePath) {
		bindTexture(new ResourceLocation(texturePath)); //minecraft resource
	}

	public static void bindTexture(final String domain, final String texturePath) {
		bindTexture(new ResourceLocation(domain, texturePath));
	}

	public static void bindTexture(final ResourceLocation location) {
		RenderUtils.getTextureManager().bindTexture(location);
	}

	public static void drawItem(final ItemStack item, final int x, final int y) {
		RenderHelper.enableGUIStandardItemLighting();
		RenderUtils.getRenderItem().renderItemAndEffectIntoGUI(item, x, y);
		RenderHelper.disableStandardItemLighting();
	}

	public static void drawFluid(final Gui gui, final int x, final int y, final FluidStack fluid, final int width, final int height) {
		if (fluid == null || fluid.getFluid() == null) {
			return;
		}
		setBlockTextureSheet();
		final int colour = fluid.getFluid().getColor(fluid);
		GlStateManager.color(colour >> 16 & 0xFF, colour >> 8 & 0xFF, colour & 0xFF, colour >> 24 & 0xFF);
		drawTiledTexture(gui, x, y, getFluidTexture(fluid), width, height);
	}

	public static TextureAtlasSprite getFluidTexture(Fluid fluid) {
		if (fluid == null) {
			fluid = FluidRegistry.LAVA;
		}
		return getTexture(fluid.getStill());
	}

	public static TextureAtlasSprite getFluidTexture(FluidStack fluid) {
		if (fluid == null || fluid.getFluid() == null || fluid.getFluid().getStill(fluid) == null) {
			fluid = new FluidStack(FluidRegistry.LAVA, 1);
		}
		return getTexture(fluid.getFluid().getStill(fluid));
	}

	public static TextureAtlasSprite getTexture(final ResourceLocation location) {
		return getTexture(location.toString());
	}

	public static TextureAtlasSprite getTexture(final String location) {
		return textureMap().getAtlasSprite(location);
	}

	public static TextureMap textureMap() {
		return mc().getTextureMapBlocks();
	}

	public static void setBlockTextureSheet() {
		bindTexture(new ResourceLocation("textures/atlas/blocks.png"));
	}

	public static void drawTiledTexture(final Gui gui, final int x, final int y, final TextureAtlasSprite icon, final int width, final int height) {
		int i = 0;
		int j = 0;
		int drawHeight = 0;
		int drawWidth = 0;
		for (i = 0; i < width; i += 16) {
			for (j = 0; j < height; j += 16) {
				drawWidth = Math.min(width - i, 16);
				drawHeight = Math.min(height - j, 16);
				drawScaledTexturedModelRectFromIcon(gui, x + i, y + j, icon, drawWidth, drawHeight);
			}
		}
		GlStateManager.color(1, 1, 1, 1);
	}

	public static void drawScaledTexturedModelRectFromIcon(final Gui gui, final int x, final int y, final TextureAtlasSprite icon, final int width, final int height) {
		if (icon == null) {
			return;
		}
		final double minU = icon.getMinU();
		final double maxU = icon.getMaxU();
		final double minV = icon.getMinV();
		final double maxV = icon.getMaxV();

		final BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(x + 0, y + height, gui.zLevel).tex(minU, minV + (maxV - minV) * height / 16F).endVertex();
		buffer.pos(x + width, y + height, gui.zLevel).tex(minU + (maxU - minU) * width / 16F, minV + (maxV - minV) * height / 16F).endVertex();
		buffer.pos(x + width, y + 0, gui.zLevel).tex(minU + (maxU - minU) * width / 16F, minV).endVertex();
		buffer.pos(x + 0, y + 0, gui.zLevel).tex(minU, minV).endVertex();
		Tessellator.getInstance().draw();
	}

	public static void drawVanillaTooltip(final GuiScreen gui, final List<String> text, final int x, final int y) {
		drawHoveringText(gui, text, x, y, RenderUtils.getFontRenderer(), -267386864, 1347420415, 1344798847);
	}

	public static void drawToolTipWithBorderColor(final GuiScreen gui, final List<String> text, final int x, final int y, final int borderColor1, final int borderColor2) {
		drawHoveringText(gui, text, x, y, RenderUtils.getFontRenderer(), -267386864, borderColor1, borderColor2);
	}

	public static void drawContinuousTexturedBox(final Gui gui, final ResourceLocation res, final int x, final int y, final int u, final int v, final int width, final int height, final int textureWidth, final int textureHeight, final int borderSize) {
		drawContinuousTexturedBox(gui, res, x, y, u, v, width, height, textureWidth, textureHeight, borderSize, borderSize, borderSize, borderSize);
	}

	public static void drawContinuousTexturedBox(final Gui gui, final ResourceLocation res, final int x, final int y, final int u, final int v, final int width, final int height, final int textureWidth, final int textureHeight, final int topBorder, final int bottomBorder, final int leftBorder, final int rightBorder) {
		bindTexture(res);
		drawContinuousTexturedBox(gui, x, y, u, v, width, height, textureWidth, textureHeight, topBorder, bottomBorder, leftBorder, rightBorder);
	}

	public static void drawContinuousTexturedBox(final Gui gui, final int x, final int y, final int u, final int v, final int width, final int height, final int textureWidth, final int textureHeight, final int topBorder, final int bottomBorder, final int leftBorder, final int rightBorder) {
		final float zLevel = gui.zLevel;
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

		final int fillerWidth = textureWidth - leftBorder - rightBorder;
		final int fillerHeight = textureHeight - topBorder - bottomBorder;
		final int canvasWidth = width - leftBorder - rightBorder;
		final int canvasHeight = height - topBorder - bottomBorder;
		final int xPasses = canvasWidth / fillerWidth;
		final int remainderWidth = canvasWidth % fillerWidth;
		final int yPasses = canvasHeight / fillerHeight;
		final int remainderHeight = canvasHeight % fillerHeight;

		// Draw Border
		// Top Left
		drawTexturedModalRect(x, y, u, v, leftBorder, topBorder, zLevel);
		// Top Right
		drawTexturedModalRect(x + leftBorder + canvasWidth, y, u + leftBorder + fillerWidth, v, rightBorder, topBorder, zLevel);
		// Bottom Left
		drawTexturedModalRect(x, y + topBorder + canvasHeight, u, v + topBorder + fillerHeight, leftBorder, bottomBorder, zLevel);
		// Bottom Right
		drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + canvasHeight, u + leftBorder + fillerWidth, v + topBorder + fillerHeight, rightBorder, bottomBorder, zLevel);

		for (int i = 0; i < xPasses + (remainderWidth > 0 ? 1 : 0); i++) {
			// Top Border
			drawTexturedModalRect(x + leftBorder + i * fillerWidth, y, u + leftBorder, v, i == xPasses ? remainderWidth : fillerWidth, topBorder, zLevel);
			// Bottom Border
			drawTexturedModalRect(x + leftBorder + i * fillerWidth, y + topBorder + canvasHeight, u + leftBorder, v + topBorder + fillerHeight, i == xPasses ? remainderWidth : fillerWidth, bottomBorder, zLevel);

			// Throw in some filler for good measure
			for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++) {
				drawTexturedModalRect(x + leftBorder + i * fillerWidth, y + topBorder + j * fillerHeight, u + leftBorder, v + topBorder, i == xPasses ? remainderWidth : fillerWidth, j == yPasses ? remainderHeight : fillerHeight, zLevel);
			}
		}

		// Side Borders
		for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++) {
			// Left Border
			drawTexturedModalRect(x, y + topBorder + j * fillerHeight, u, v + topBorder, leftBorder, j == yPasses ? remainderHeight : fillerHeight, zLevel);
			// Right Border
			drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + j * fillerHeight, u + leftBorder + fillerWidth, v + topBorder, rightBorder, j == yPasses ? remainderHeight : fillerHeight, zLevel);
		}
	}

	public static void drawTexturedModalRect(final int x, final int y, final int u, final int v, final int width, final int height, final float zLevel) {
		final float uScale = 1f / 0x100;
		final float vScale = 1f / 0x100;
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder wr = tessellator.getBuffer();
		wr.begin(7, DefaultVertexFormats.POSITION_TEX);
		wr.pos(x, y + height, zLevel).tex(u * uScale, (v + height) * vScale).endVertex();
		wr.pos(x + width, y + height, zLevel).tex((u + width) * uScale, (v + height) * vScale).endVertex();
		wr.pos(x + width, y, zLevel).tex((u + width) * uScale, v * vScale).endVertex();
		wr.pos(x, y, zLevel).tex(u * uScale, v * vScale).endVertex();
		tessellator.draw();
	}

	private static void drawHoveringText(final GuiScreen gui, final List<String> textLines, final int x, final int y, final FontRenderer font, final int backgroundColor, final int borderColor1, final int borderColor2) {
		//net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(textLines, x, y, gui.width, gui.height, -1, font);

		if (!textLines.isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.disableRescaleNormal();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.disableDepth();
			int i = 0;

			for (final String s : textLines) {
				final int j = RenderUtils.getFontRenderer().getStringWidth(s);

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

			if (i2 + k + 8 > gui.height) {
				i2 = gui.height - k - 8;
			}

			gui.zLevel = 300.0f;
			gui.itemRender.zLevel = 300.0f;
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
				final String s1 = textLines.get(k1);
				RenderUtils.getFontRenderer().drawStringWithShadow(s1, l1, i2, -1);

				if (k1 == 0) {
					i2 += 2;
				}

				i2 += 10;
			}

			gui.zLevel = 0.0f;
			gui.itemRender.zLevel = 0.0f;
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableRescaleNormal();
			GlStateManager.popMatrix();

		}
	}

	public static void drawCenteredString(final String text, final int x, final int y, final int color) {
		RenderUtils.getFontRenderer().drawStringWithShadow(text, x - RenderUtils.getFontRenderer().getStringWidth(text) / 2, y, color);
	}

	public static void drawScaledString(final String text, final int x, final int y, final float scale, final int color) {
		//GlStateManager.disableLighting();
		//GlStateManager.disableAlpha();
		//GlStateManager.disableBlend();
		//GlStateManager.disableDepth();
		GlStateManager.pushMatrix();
		GlStateManager.scale(scale, scale, scale);

		//int X = (int) ((par4 + offset + 16.0F - getFontRenderer().getStringWidth(stackSize) * scaleFactor) * inverseScaleFactor);

		//int Y = (int) ((par5 + offset + 16.0F - 7.0F * scale) * inverseScaleFactor);
		RenderUtils.getFontRenderer().drawStringWithShadow(text, x, y, color);
		GlStateManager.popMatrix();
		//GlStateManager.enableDepth();
		//GlStateManager.enableBlend();
		//GlStateManager.enableAlpha();
		//GlStateManager.enableLighting();
	}

	public static void drawStringNoShadow(final String text, final int x, final int y, final int color) {
		RenderUtils.getFontRenderer().drawString(text, x, y, color, false);
	}

	public static void drawGradientRect(final Gui gui, final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
		final float f = (startColor >> 24 & 255) / 255.0F;
		final float f1 = (startColor >> 16 & 255) / 255.0F;
		final float f2 = (startColor >> 8 & 255) / 255.0F;
		final float f3 = (startColor & 255) / 255.0F;
		final float f4 = (endColor >> 24 & 255) / 255.0F;
		final float f5 = (endColor >> 16 & 255) / 255.0F;
		final float f6 = (endColor >> 8 & 255) / 255.0F;
		final float f7 = (endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		vertexbuffer.pos(right, top, gui.zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos(left, top, gui.zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos(left, bottom, gui.zLevel).color(f5, f6, f7, f4).endVertex();
		vertexbuffer.pos(right, bottom, gui.zLevel).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

	public static void drawSlot(final GuiContainer gui, final Slot slotIn) {
		final int i = EasyMappings.slotPosX(slotIn);
		final int j = EasyMappings.slotPosY(slotIn);
		ItemStack itemstack = slotIn.getStack();
		boolean flag = false;
		boolean flag1 = slotIn == gui.clickedSlot && gui.draggedStack != null && !gui.isRightMouseClick;
		final ItemStack itemstack1 = EasyMappings.player().inventory.getItemStack();
		String s = null;
		if (slotIn == gui.clickedSlot && gui.draggedStack != null && gui.isRightMouseClick && itemstack != null) {
			itemstack = itemstack.copy();
			itemstack.setCount(itemstack.getCount() / 2);
		}
		else if (gui.dragSplitting && gui.dragSplittingSlots.contains(slotIn) && itemstack1 != null) {
			if (gui.dragSplittingSlots.size() == 1) {
				return;
			}
			if (Container.canAddItemToSlot(slotIn, itemstack1, true) && gui.inventorySlots.canDragIntoSlot(slotIn)) {
				itemstack = itemstack1.copy();
				flag = true;
				Container.computeStackSize(gui.dragSplittingSlots, gui.dragSplittingLimit, itemstack, slotIn.getStack() == null ? 0 : slotIn.getStack().getCount());
				if (itemstack.getCount() > itemstack.getMaxStackSize()) {
					s = TextFormatting.YELLOW + "" + itemstack.getMaxStackSize();
					itemstack.setCount(itemstack.getMaxStackSize());
				}
				if (itemstack.getCount() > slotIn.getItemStackLimit(itemstack)) {
					s = TextFormatting.YELLOW + "" + slotIn.getItemStackLimit(itemstack);
					itemstack.setCount(slotIn.getItemStackLimit(itemstack));
				}
			}
			else {
				gui.dragSplittingSlots.remove(slotIn);
				updateDragSplitting(gui);
			}
		}
		gui.zLevel = 100.0f;
		gui.itemRender.zLevel = 100.0f;
		if (itemstack == null && slotIn.isEnabled()) {
			final TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();
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

			gui.itemRender.renderItemOverlayIntoGUI(RenderUtils.getFontRenderer(), itemstack, i, j, s);
		}
		gui.zLevel = 0.0f;
		gui.itemRender.zLevel = 0.0f;
	}

	public static void drawTexturedModalRect(final Gui gui, final int xCoord, final int yCoord, final TextureAtlasSprite textureSprite, final int widthIn, final int heightIn) {
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos(xCoord + 0, yCoord + heightIn, gui.zLevel).tex(textureSprite.getMinU(), textureSprite.getMaxV()).endVertex();
		vertexbuffer.pos(xCoord + widthIn, yCoord + heightIn, gui.zLevel).tex(textureSprite.getMaxU(), textureSprite.getMaxV()).endVertex();
		vertexbuffer.pos(xCoord + widthIn, yCoord + 0, gui.zLevel).tex(textureSprite.getMaxU(), textureSprite.getMinV()).endVertex();
		vertexbuffer.pos(xCoord + 0, yCoord + 0, gui.zLevel).tex(textureSprite.getMinU(), textureSprite.getMinV()).endVertex();
		tessellator.draw();
	}

	public static void updateDragSplitting(final GuiContainer gui) {
		final ItemStack itemstack = EasyMappings.player().inventory.getItemStack();

		if (itemstack != null && gui.dragSplitting) {
			gui.dragSplittingRemnant = itemstack.getCount();

			for (final Slot slot : gui.dragSplittingSlots) {
				final ItemStack itemstack1 = itemstack.copy();
				final int i = slot.getStack() == null ? 0 : slot.getStack().getCount();
				Container.computeStackSize(gui.dragSplittingSlots, gui.dragSplittingLimit, itemstack1, i);

				if (itemstack1.getCount() > itemstack1.getMaxStackSize()) {
					itemstack1.setCount(itemstack1.getMaxStackSize());
				}

				if (itemstack1.getCount() > slot.getItemStackLimit(itemstack1)) {
					itemstack1.setCount(slot.getItemStackLimit(itemstack1));
				}

				gui.dragSplittingRemnant = gui.dragSplittingRemnant - (itemstack1.getCount() - i);
			}
		}
	}

	public static void drawItemStack(final GuiContainer gui, final ItemStack stack, final int x, final int y, final String altText) {
		GlStateManager.translate(0.0F, 0.0F, 32.0F);
		gui.zLevel = 200.0f;
		gui.itemRender.zLevel = 200.0f;
		FontRenderer font = null;
		if (stack != null) {
			font = stack.getItem().getFontRenderer(stack);
		}
		if (font == null) {
			font = RenderUtils.getFontRenderer();
		}
		gui.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
		gui.itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (gui.draggedStack == null ? 0 : 8), altText);
		gui.zLevel = 0.0f;
		gui.itemRender.zLevel = 0.0f;
		GlStateManager.translate(0.0F, 0.0F, -32.0F);
	}

}
