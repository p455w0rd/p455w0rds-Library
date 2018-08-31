package p455w0rdslib.util;

import static net.minecraft.client.renderer.GlStateManager.blendFunc;
import static net.minecraft.client.renderer.GlStateManager.enableBlend;
import static net.minecraft.client.renderer.GlStateManager.enableDepth;
import static net.minecraft.client.renderer.GlStateManager.enableRescaleNormal;
import static net.minecraft.client.renderer.GlStateManager.popMatrix;
import static net.minecraft.client.renderer.GlStateManager.pushMatrix;
import static net.minecraft.client.renderer.GlStateManager.scale;

import java.awt.Color;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
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

	public static void bindTexture(String texturePath) {
		bindTexture(new ResourceLocation(texturePath)); //minecraft resource
	}

	public static void bindTexture(String domain, String texturePath) {
		bindTexture(new ResourceLocation(domain, texturePath));
	}

	public static void bindTexture(ResourceLocation location) {
		RenderUtils.getTextureManager().bindTexture(location);
	}

	public static void drawItem(ItemStack item, int x, int y) {
		RenderHelper.enableGUIStandardItemLighting();
		RenderUtils.getRenderItem().renderItemAndEffectIntoGUI(item, x, y);
		RenderHelper.disableStandardItemLighting();
	}

	public static void drawFluid(Gui gui, int x, int y, FluidStack fluid, int width, int height) {
		if (fluid == null || fluid.getFluid() == null) {
			return;
		}
		setBlockTextureSheet();
		int colour = fluid.getFluid().getColor(fluid);
		GlStateManager.color((colour >> 16) & 0xFF, (colour >> 8) & 0xFF, colour & 0xFF, (colour >> 24) & 0xFF);
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

	public static TextureAtlasSprite getTexture(ResourceLocation location) {
		return getTexture(location.toString());
	}

	public static TextureAtlasSprite getTexture(String location) {
		return textureMap().getAtlasSprite(location);
	}

	public static TextureMap textureMap() {
		return mc().getTextureMapBlocks();
	}

	public static void setBlockTextureSheet() {
		bindTexture(new ResourceLocation("textures/atlas/blocks.png"));
	}

	public static void drawTiledTexture(Gui gui, int x, int y, TextureAtlasSprite icon, int width, int height) {
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

	public static void drawScaledTexturedModelRectFromIcon(Gui gui, int x, int y, TextureAtlasSprite icon, int width, int height) {
		if (icon == null) {
			return;
		}
		double minU = icon.getMinU();
		double maxU = icon.getMaxU();
		double minV = icon.getMinV();
		double maxV = icon.getMaxV();

		VertexBuffer buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(x + 0, y + height, MCPrivateUtils.getGuiZLevel(gui)).tex(minU, minV + (maxV - minV) * height / 16F).endVertex();
		buffer.pos(x + width, y + height, MCPrivateUtils.getGuiZLevel(gui)).tex(minU + (maxU - minU) * width / 16F, minV + (maxV - minV) * height / 16F).endVertex();
		buffer.pos(x + width, y + 0, MCPrivateUtils.getGuiZLevel(gui)).tex(minU + (maxU - minU) * width / 16F, minV).endVertex();
		buffer.pos(x + 0, y + 0, MCPrivateUtils.getGuiZLevel(gui)).tex(minU, minV).endVertex();
		Tessellator.getInstance().draw();
	}

	public static void drawVanillaTooltip(GuiScreen gui, List<String> text, int x, int y) {
		drawHoveringText(gui, text, x, y, RenderUtils.getFontRenderer(), -267386864, 1347420415, 1344798847);
	}

	public static void drawToolTipWithBorderColor(GuiScreen gui, List<String> text, int x, int y, int borderColor1, int borderColor2) {
		drawHoveringText(gui, text, x, y, RenderUtils.getFontRenderer(), -267386864, borderColor1, borderColor2);
	}

	public static void drawContinuousTexturedBox(Gui gui, ResourceLocation res, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, int borderSize) {
		drawContinuousTexturedBox(gui, res, x, y, u, v, width, height, textureWidth, textureHeight, borderSize, borderSize, borderSize, borderSize);
	}

	public static void drawContinuousTexturedBox(Gui gui, ResourceLocation res, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, int topBorder, int bottomBorder, int leftBorder, int rightBorder) {
		bindTexture(res);
		drawContinuousTexturedBox(gui, x, y, u, v, width, height, textureWidth, textureHeight, topBorder, bottomBorder, leftBorder, rightBorder);
	}

	public static void drawContinuousTexturedBox(Gui gui, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, int topBorder, int bottomBorder, int leftBorder, int rightBorder) {
		float zLevel = MCPrivateUtils.getGuiZLevel(gui);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

		int fillerWidth = textureWidth - leftBorder - rightBorder;
		int fillerHeight = textureHeight - topBorder - bottomBorder;
		int canvasWidth = width - leftBorder - rightBorder;
		int canvasHeight = height - topBorder - bottomBorder;
		int xPasses = canvasWidth / fillerWidth;
		int remainderWidth = canvasWidth % fillerWidth;
		int yPasses = canvasHeight / fillerHeight;
		int remainderHeight = canvasHeight % fillerHeight;

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
			drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y, u + leftBorder, v, (i == xPasses ? remainderWidth : fillerWidth), topBorder, zLevel);
			// Bottom Border
			drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y + topBorder + canvasHeight, u + leftBorder, v + topBorder + fillerHeight, (i == xPasses ? remainderWidth : fillerWidth), bottomBorder, zLevel);

			// Throw in some filler for good measure
			for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++) {
				drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y + topBorder + (j * fillerHeight), u + leftBorder, v + topBorder, (i == xPasses ? remainderWidth : fillerWidth), (j == yPasses ? remainderHeight : fillerHeight), zLevel);
			}
		}

		// Side Borders
		for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++) {
			// Left Border
			drawTexturedModalRect(x, y + topBorder + (j * fillerHeight), u, v + topBorder, leftBorder, (j == yPasses ? remainderHeight : fillerHeight), zLevel);
			// Right Border
			drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + (j * fillerHeight), u + leftBorder + fillerWidth, v + topBorder, rightBorder, (j == yPasses ? remainderHeight : fillerHeight), zLevel);
		}
	}

	public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, float zLevel) {
		float uScale = 1f / 0x100;
		float vScale = 1f / 0x100;
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer wr = tessellator.getBuffer();
		wr.begin(7, DefaultVertexFormats.POSITION_TEX);
		wr.pos(x, y + height, zLevel).tex(u * uScale, ((v + height) * vScale)).endVertex();
		wr.pos(x + width, y + height, zLevel).tex((u + width) * uScale, ((v + height) * vScale)).endVertex();
		wr.pos(x + width, y, zLevel).tex((u + width) * uScale, (v * vScale)).endVertex();
		wr.pos(x, y, zLevel).tex(u * uScale, (v * vScale)).endVertex();
		tessellator.draw();
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
				int j = RenderUtils.getFontRenderer().getStringWidth(s);

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
				RenderUtils.getFontRenderer().drawStringWithShadow(s1, l1, i2, -1);

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

	public static void drawCenteredString(String text, int x, int y, int color) {
		RenderUtils.getFontRenderer().drawStringWithShadow(text, x - RenderUtils.getFontRenderer().getStringWidth(text) / 2, y, color);
	}

	public static void drawScaledString(String text, int x, int y, float scale, int color) {
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

	public static void drawStringNoShadow(String text, int x, int y, int color) {
		RenderUtils.getFontRenderer().drawString(text, x, y, color, false);
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
		int i = EasyMappings.slotPosX(slotIn);
		int j = EasyMappings.slotPosY(slotIn);
		ItemStack itemstack = slotIn.getStack();
		boolean flag = false;
		boolean flag1 = (slotIn == MCPrivateUtils.getGuiClickedSlot(gui)) && (MCPrivateUtils.getGuiDraggedStack(gui) != null) && (!MCPrivateUtils.getGuiIsRightMouseClick(gui));
		ItemStack itemstack1 = EasyMappings.player().inventory.getItemStack();
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

			MCPrivateUtils.getGuiScreenRenderItem(gui).renderItemOverlayIntoGUI(RenderUtils.getFontRenderer(), itemstack, i, j, s);
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
		ItemStack itemstack = EasyMappings.player().inventory.getItemStack();

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
			font = RenderUtils.getFontRenderer();
		}
		MCPrivateUtils.getGuiScreenRenderItem(gui).renderItemAndEffectIntoGUI(stack, x, y);
		MCPrivateUtils.getGuiScreenRenderItem(gui).renderItemOverlayIntoGUI(font, stack, x, y - (MCPrivateUtils.getGuiDraggedStack(gui) == null ? 0 : 8), altText);
		MCPrivateUtils.setGuiScreenRendererZLevel(gui, 0.0F);
		MCPrivateUtils.setGuiZLevel(gui, 0.0F);
	}

	public static void drawEntityOnScreen(int posX, int posY, float scale, float mouseX, float mouseY, EntityLivingBase ent) {
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate(posX, posY, 50.0F);
		GlStateManager.scale((-scale), scale, scale);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		float f = ent.renderYawOffset;
		float f1 = ent.rotationYaw;
		float f2 = ent.rotationPitch;
		float f3 = ent.prevRotationYawHead;
		float f4 = ent.rotationYawHead;
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-((float) Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
		ent.renderYawOffset = (float) Math.atan(mouseX / 40.0F) * 20.0F;
		ent.rotationYaw = (float) Math.atan(mouseX / 40.0F) * 40.0F;
		ent.rotationPitch = -((float) Math.atan(mouseY / 40.0F)) * 20.0F;
		ent.rotationYawHead = ent.rotationYaw;
		ent.prevRotationYawHead = ent.rotationYaw;
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.doRenderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		rendermanager.setRenderShadow(true);
		ent.renderYawOffset = f;
		ent.rotationYaw = f1;
		ent.rotationPitch = f2;
		ent.prevRotationYawHead = f3;
		ent.rotationYawHead = f4;
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public static void drawEntityOnScreenWithRotation(int posX, int posY, float scale, float rotation, EntityLivingBase ent) {
		drawEntityOnScreen(posX, posY, scale, rotation, 0.0f, ent);
	}

	public static void drawScaledEntityFollowMouse(EntityLivingBase livingEntity, int x, int y, float scale, float mouseX, float mouseY) {
		pushMatrix();
		enableBlend();
		blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		scale(scale, scale, 1.0F);
		RenderHelper.enableGUIStandardItemLighting();
		enableRescaleNormal();
		enableDepth();
		drawEntityOnScreen(x, y, scale, mouseX, mouseY, livingEntity);
		RenderHelper.disableStandardItemLighting();
		popMatrix();
	}

	public static void drawScaledEntityWithRotation(EntityLivingBase livingEntity, int x, int y, float scale, float rotation) {
		pushMatrix();
		enableBlend();
		blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		scale(scale, scale, 1.0F);
		RenderHelper.enableGUIStandardItemLighting();
		enableRescaleNormal();
		enableDepth();
		drawEntityOnScreenWithRotation(x, y, scale, rotation, livingEntity);
		RenderHelper.disableStandardItemLighting();
		popMatrix();
	}

	public static void drawCenteredTextGlowing(FontRenderer font, String s, int x, int y) {
		drawTextGlowing(font, s, x - font.getStringWidth(s) / 2, y);
	}

	public static void drawTextGlowing(FontRenderer font, String s, int x, int y) {
		float sine = 0.5f * ((float) Math.sin(Math.toRadians(4.0f * (Minecraft.getMinecraft().world.getTotalWorldTime() + Minecraft.getMinecraft().getRenderPartialTicks()))) + 1.0f);
		RenderUtils.drawTextRGBA(font, s, x - 1, y, 0, 0, 0, 64);
		RenderUtils.drawTextRGBA(font, s, x + 1, y, 0, 0, 0, 64);
		RenderUtils.drawTextRGBA(font, s, x, y - 1, 0, 0, 0, 64);
		RenderUtils.drawTextRGBA(font, s, x, y + 1, 0, 0, 0, 64);
		RenderUtils.drawTextRGBA(font, s, x - 2, y, 0, 0, 0, 40);
		RenderUtils.drawTextRGBA(font, s, x + 2, y, 0, 0, 0, 40);
		RenderUtils.drawTextRGBA(font, s, x, y - 2, 0, 0, 0, 40);
		RenderUtils.drawTextRGBA(font, s, x, y + 2, 0, 0, 0, 40);
		RenderUtils.drawTextRGBA(font, s, x - 1, y + 1, 0, 0, 0, 40);
		RenderUtils.drawTextRGBA(font, s, x + 1, y - 1, 0, 0, 0, 40);
		RenderUtils.drawTextRGBA(font, s, x - 1, y - 1, 0, 0, 0, 40);
		RenderUtils.drawTextRGBA(font, s, x + 1, y + 1, 0, 0, 0, 40);
		font.drawString(s, x, y, 0xff11bb11);
	}

	public static void drawTextGlowingAuraTransparent(FontRenderer font, String s, int x, int y, int r, int g, int b, int a) {
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
		font.drawString(s, x, y, intColor(r, g, b) + (a << 24));
		RenderUtils.drawTextRGBA(font, s, x - 1, y, r, g, b, (40 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x + 1, y, r, g, b, (40 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x, y - 1, r, g, b, (40 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x, y + 1, r, g, b, (40 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x - 2, y, r, g, b, (20 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x + 2, y, r, g, b, (20 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x, y - 2, r, g, b, (20 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x, y + 2, r, g, b, (20 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x - 1, y + 1, r, g, b, (20 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x + 1, y - 1, r, g, b, (20 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x - 1, y - 1, r, g, b, (20 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x + 1, y + 1, r, g, b, (20 * a) / 255);
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
	}

	public static void drawTextGlowingAuraTransparent(FontRenderer font, String s, int x, int y, int a) {
		float sine = 0.5f * ((float) Math.sin(Math.toRadians(4.0f * (Minecraft.getMinecraft().world.getTotalWorldTime() + Minecraft.getMinecraft().getRenderPartialTicks()))) + 1.0f);
		drawTextGlowingAuraTransparent(font, s, x, y, a, new Color(16, 64 + (int) (64 * sine), 255));
	}

	public static void drawTextGlowingAuraTransparent(FontRenderer font, String s, int x, int y, int a, Color color) {
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
		int b = color.getBlue();
		int g = color.getGreen();
		int r = color.getRed();
		font.drawString(s, x, y, intColor(r, g, b) + (a << 24));
		RenderUtils.drawTextRGBA(font, s, x - 2, y, r, g, b, (40 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x + 2, y, r, g, b, (40 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x, y - 2, r, g, b, (40 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x, y + 2, r, g, b, (40 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x - 4, y, r, g, b, (20 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x + 4, y, r, g, b, (20 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x, y - 4, r, g, b, (20 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x, y + 4, r, g, b, (20 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x - 1, y + 2, r, g, b, (20 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x + 1, y - 2, r, g, b, (20 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x - 1, y - 2, r, g, b, (20 * a) / 255);
		RenderUtils.drawTextRGBA(font, s, x + 1, y + 2, r, g, b, (20 * a) / 255);
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
	}

	public static int intColor(int r, int g, int b) {
		return (r * 65536 + g * 256 + b);
	}

	public static void drawText(FontRenderer font, String s, int x, int y, int color) {
		RenderUtils.drawTextRGBA(font, s, x - 1, y, 0, 0, 0, 64);
		RenderUtils.drawTextRGBA(font, s, x + 1, y, 0, 0, 0, 64);
		RenderUtils.drawTextRGBA(font, s, x, y - 1, 0, 0, 0, 64);
		RenderUtils.drawTextRGBA(font, s, x, y + 1, 0, 0, 0, 64);
		font.drawString(s, x, y, color);
	}

}
