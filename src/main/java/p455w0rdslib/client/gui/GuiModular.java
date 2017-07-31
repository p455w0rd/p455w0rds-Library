package p455w0rdslib.client.gui;

import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import p455w0rdslib.api.gui.IGuiElement;
import p455w0rdslib.api.gui.IModularGui;
import p455w0rdslib.util.EasyMappings;
import p455w0rdslib.util.GuiUtils;

/**
 * @author p455w0rd
 *
 */
public class GuiModular extends GuiContainer implements IModularGui {

	ResourceLocation backgroundTexture = new ResourceLocation("textures/gui/demo_background.png");
	List<IGuiElement> elements = Lists.newArrayList();
	String title = "";
	float titleScale = 1.0F;
	int titleColor = 0xFFFFFF, tooltipColor1 = 0xFF17FF6D, tooltipColor2 = 0x9917FF6D, titleOffsetX = 5,
			titleOffsetY = 6;

	public GuiModular(Container inventorySlotsIn) {
		super(inventorySlotsIn);
	}

	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick) {
		super.drawScreen(mouseX, mouseY, partialTick);
		updateElements(mouseX, mouseY);
		for (IGuiElement element : getElements()) {
			if (element.hasTooltip()) {
				if (isMouseOverElement(element, mouseX, mouseY)) {
					GuiUtils.drawToolTipWithBorderColor(this, element.getTooltip(), mouseX, mouseY, tooltipColor1, tooltipColor2);
				}
			}
		}
	}

	@Override
	public void updateElements(int mouseX, int mouseY) {
		for (int i = elements.size(); i-- > 0;) {
			IGuiElement c = elements.get(i);
			if (c.isVisible() && c.isEnabled()) {
				c.update(mouseX, mouseY);
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
		GuiUtils.drawScaledString(getTitle(), titleOffsetX, titleOffsetY, titleScale, titleColor);
		drawElements(0, mouseX, mouseY, true);
		GlStateManager.enableBlend();
		GlStateManager.enableLighting();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (IGuiElement element : getElements()) {
			element.onMousePressed(mouseX - ((width - xSize) / 2), mouseY - ((height - ySize) / 2), mouseButton);
		}
		try {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		catch (IOException e) {
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int button) {
		for (IGuiElement element : getElements()) {
			element.onMouseReleased(mouseX - ((width - xSize) / 2), mouseY - ((height - ySize) / 2), button);
		}
		super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1, 1, 1, 1);
		GuiUtils.bindTexture(backgroundTexture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		GlStateManager.pushMatrix();
		GlStateManager.translate(guiLeft, guiTop, 0.0F);
		drawElements(partialTicks, x, y, false);
		GlStateManager.popMatrix();
	}

	@Override
	public int getX() {
		return guiLeft;
	}

	@Override
	public int getY() {
		return guiTop;
	}

	@Override
	public int getWidth() {
		return xSize;
	}

	@Override
	public int getHeight() {
		return ySize;
	}

	@Override
	public IModularGui setTitleScale(float scale) {
		titleScale = scale;
		return this;
	}

	@Override
	public IModularGui setTitleOffsetX(int offset) {
		titleOffsetX = offset;
		return this;
	}

	@Override
	public IModularGui setTitleOffsetY(int offset) {
		titleOffsetY = offset;
		return this;
	}

	@Override
	public IModularGui setTitlePos(int x, int y) {
		return setTitleOffsetX(x).setTitleOffsetY(y);
	}

	@Override
	public IModularGui setWidth(int w) {
		xSize = w;
		return this;
	}

	@Override
	public IModularGui setHeight(int h) {
		ySize = h;
		return this;
	}

	@Override
	public IModularGui setSize(int w, int h) {
		return setWidth(w).setHeight(h);
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public IModularGui setTitle(String titleText) {
		title = titleText;
		return this;
	}

	@Override
	public int getTitleColor() {
		return titleColor;
	}

	@Override
	public IModularGui setTitleColor(int color) {
		titleColor = color;
		return this;
	}

	@Override
	public ResourceLocation getBackgroundTexture() {
		return backgroundTexture;
	}

	@Override
	public IModularGui setBackgroundTexture(ResourceLocation location) {
		backgroundTexture = location;
		return this;
	}

	public boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY) {
		return isMouseOver(EasyMappings.slotPosX(slotIn), EasyMappings.slotPosY(slotIn), 16, 16, mouseX, mouseY);
	}

	@Override
	public boolean isMouseOverElement(IGuiElement element, int mouseX, int mouseY) {
		return isMouseOver(element.getX(), element.getY(), element.getWidth(), element.getHeight(), mouseX, mouseY);
	}

	@Override
	public boolean isMouseOver(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY) {
		return super.isPointInRegion(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
	}

	@Override
	protected void renderToolTip(ItemStack stack, int x, int y) {
		List<String> list = stack.getTooltip(EasyMappings.player(), mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);

		for (int i = 0; i < list.size(); ++i) {
			if (i == 0) {
				list.set(i, stack.getRarity().rarityColor + list.get(i));
			}
			else {
				list.set(i, TextFormatting.GRAY + list.get(i));
			}
		}

		net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);
		GuiUtils.drawToolTipWithBorderColor(this, list, x, y, tooltipColor1, tooltipColor2);
		net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
	}

	@Override
	public void drawElements(float partialTick, int mouseX, int mouseY, boolean foreground) {
		if (foreground) {
			for (int i = 0; i < elements.size(); i++) {
				IGuiElement element = elements.get(i);
				if (element.isVisible()) {
					element.drawForeground(mouseX, mouseY);
				}
			}
		}
		else {
			for (int i = 0; i < elements.size(); i++) {
				IGuiElement element = elements.get(i);
				if (element.isVisible()) {
					element.drawBackground(mouseX, mouseY, partialTick);
				}
			}
		}
	}

	@Override
	public IModularGui addElement(IGuiElement element) {
		elements.add(element);
		return this;
	}

	@Override
	public List<IGuiElement> getElements() {
		return elements;
	}

	@Override
	public IModularGui setTooltipColor(int color, boolean top) {
		if (top) {
			tooltipColor1 = color;
		}
		else {
			tooltipColor2 = color;
		}
		return this;
	}

	@Override
	public int[] getTooltipColors() {
		return new int[] {
				tooltipColor1,
				tooltipColor2
		};
	}

	@Override
	public boolean onMouseWheel(int mouseX, int mouseY, int wheelMovement) {
		return false;
	}

	@Override
	public void handleMouseInput() throws IOException {

		int x = Mouse.getEventX() * width / mc.displayWidth;
		int y = height - Mouse.getEventY() * height / mc.displayHeight - 1;

		int mouseX = x - guiLeft;
		int mouseY = y - guiTop;

		int wheelMovement = Mouse.getEventDWheel();

		if (wheelMovement != 0) {
			for (int i = elements.size(); i-- > 0;) {
				IGuiElement c = elements.get(i);
				if (!c.isVisible() || !c.isEnabled() || !c.isMouseOver(mouseX, mouseY)) {
					continue;
				}
				if (c.onMouseWheel(mouseX, mouseY, wheelMovement)) {
					return;
				}
			}

			if (onMouseWheel(mouseX, mouseY, wheelMovement)) {
				return;
			}
		}
		super.handleMouseInput();
	}
}
