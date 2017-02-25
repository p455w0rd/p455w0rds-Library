package p455w0rdslib.client.gui.element;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import p455w0rdslib.api.gui.IGuiElement;
import p455w0rdslib.api.gui.IGuiScrollbar;
import p455w0rdslib.api.gui.IModularGui;

/**
 * @author p455w0rd
 *
 */
public abstract class GuiScrollbar extends GuiElement implements IGuiScrollbar {

	protected int scrollPos, minPos, maxPos, sliderWidth, sliderHeight, borderColor = 0xFF000000,
			faceColor = 0xFF333333;
	protected boolean dragging;
	IGuiElement parentElement;

	public GuiScrollbar(IModularGui gui, GuiPos posIn, int width, int height, int max) {
		this(gui, posIn, width, height, max, 0);
	}

	public GuiScrollbar(IModularGui gui, GuiPos posIn, int width, int height, int max, int min) {
		super(gui, posIn, width, height);
		minPos = min;
		maxPos = max;
		setSliderWidth(width);
		setSliderHeight(15);
	}

	@Override
	public void update(int mouseX, int mouseY) {
		if (isDragging()) {
			doDrag(mouseX - getX(), mouseY - (getY() + getSliderHeight()));
		}
	}

	@Override
	public boolean onMousePressed(int mouseX, int mouseY, int mouseButton) {
		if (isMouseOver(mouseX, mouseY)) {
			setDragging(mouseButton == 0);
			update(mouseX, mouseY);
			return true;
		}
		return false;
	}

	@Override
	public boolean isMouseOver(int mouseX, int mouseY) {
		return mouseX > getX() && mouseY > getY() && mouseX <= getX() + getWidth() && mouseY <= getY() + getHeight();
	}

	@Override
	public void onMouseReleased(int mouseX, int mouseY, int button) {
		if (isDragging()) {
			onStopDragging();
		}
		setDragging(false);
	}

	@Override
	public abstract void doDrag(int x, int y);

	@Override
	public void drawBackground(int mouseX, int mouseY, float partialTicks) {
		if (getParentElement() != null && getParentElement().isVisible()) {
			Gui.drawRect(getX() - 1, getY() - 1, getX() + getWidth() + 1, getY() + getHeight() + 1, borderColor);
			Gui.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), faceColor);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		if (getParentElement() != null && getParentElement().isVisible()) {
			int sliderMidX = getSliderWidth() / 2;
			int sliderMidY = getSliderHeight() / 2;
			int sliderEndX = getSliderWidth() - sliderMidX;
			int sliderEndY = getSliderHeight() - sliderMidY;

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			Gui.drawRect(getX() + getSliderXPos(), getY() + getSliderYPos() - 1, getX() + getSliderXPos() + getSliderWidth() + 1, getY() + getSliderYPos() + getSliderHeight() + 1, borderColor);
			Gui.drawRect(getX() + getSliderXPos() + 1, getY() + getSliderYPos(), getX() + getSliderXPos() + getSliderWidth(), getY() + getSliderYPos() + getSliderHeight(), 0xFFCCCCCC);
		}
	}

	@Override
	public boolean onMouseWheel(int mouseX, int mouseY, int direction) {

		if (getParentElement() != null) {
			getParentElement().onMouseWheel(mouseX, mouseY, direction);
		}
		else {
			if (direction > 0) {
				setScrollPos(getScrollPos() - 1);
			}
			else if (direction < 0) {
				setScrollPos(getScrollPos() + 1);
			}
		}
		return true;
	}

	@Override
	public int getSliderXPos() {
		return 0;
	}

	@Override
	public int getSliderYPos() {
		return 0;
	}

	@Override
	public IGuiElement getParentElement() {
		return parentElement;
	}

	@Override
	public IGuiScrollbar setParentElement(IGuiElement element) {
		parentElement = element;
		return this;
	}

	@Override
	public int getScrollPos() {
		return scrollPos;
	}

	@Override
	public IGuiScrollbar setScrollPos(int pos) {
		scrollPos = Math.max(getMinPos(), Math.min(getMaxPos(), pos));
		if (pos != getScrollPos()) {
			//scrollPos = pos;
			//onValueChanged(scrollPos);
		}
		return this;
	}

	/*
	public void onValueChanged(int value) {
		return;
	}
	*/
	@Override
	public void onStopDragging() {
		return;
	}

	@Override
	public int getMinPos() {
		return minPos;
	}

	@Override
	public IGuiScrollbar setMinPos(int pos) {
		minPos = pos;
		return this;
	}

	@Override
	public int getMaxPos() {
		return maxPos;
	}

	@Override
	public IGuiScrollbar setMaxPos(int pos) {
		maxPos = pos;
		return this;
	}

	@Override
	public IGuiScrollbar setBounds(int min, int max) {
		setMinPos(min);
		setMaxPos(max);
		setScrollPos(getScrollPos());
		return this;
	}

	@Override
	public int getSliderWidth() {
		return sliderWidth;
	}

	@Override
	public IGuiScrollbar setSliderWidth(int width) {
		sliderWidth = width;
		return this;
	}

	@Override
	public int getSliderHeight() {
		return sliderHeight;
	}

	@Override
	public IGuiScrollbar setSliderHeight(int height) {
		sliderHeight = height;
		return this;
	}

	@Override
	public int getBorderColor() {
		return borderColor;
	}

	@Override
	public IGuiScrollbar setBorderColor(int color) {
		borderColor = color;
		return this;
	}

	@Override
	public int getFaceColor() {
		return faceColor;
	}

	@Override
	public IGuiScrollbar setFaceColor(int color) {
		faceColor = color;
		return this;
	}

	@Override
	public IGuiScrollbar setColors(int border, int face) {
		return setBorderColor(border).setFaceColor(face);
	}

	@Override
	public boolean isDragging() {
		return dragging;
	}

	@Override
	public void setDragging(boolean isDragging) {
		dragging = isDragging;
	}

}
