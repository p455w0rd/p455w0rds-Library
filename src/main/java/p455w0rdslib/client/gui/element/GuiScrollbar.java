package p455w0rdslib.client.gui.element;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import p455w0rdslib.api.gui.*;

/**
 * @author p455w0rd
 *
 */
public abstract class GuiScrollbar extends GuiElement implements IGuiScrollbar {

	protected int scrollPos, minPos, maxPos, sliderWidth, sliderHeight, borderColor = 0xFF000000,
			faceColor = 0xFF333333;
	protected boolean dragging;
	IGuiElement parentElement;

	public GuiScrollbar(final IModularGui gui, final GuiPos posIn, final int width, final int height, final int max) {
		this(gui, posIn, width, height, max, 0);
	}

	public GuiScrollbar(final IModularGui gui, final GuiPos posIn, final int width, final int height, final int max, final int min) {
		super(gui, posIn, width, height);
		minPos = min;
		maxPos = max;
		setSliderWidth(width);
		setSliderHeight(15);
	}

	@Override
	public void update(final int mouseX, final int mouseY) {
		if (isDragging()) {
			doDrag(mouseX - getX(), mouseY - (getY() + getSliderHeight()));
		}
	}

	@Override
	public boolean onMousePressed(final int mouseX, final int mouseY, final int mouseButton) {
		if (isMouseOver(mouseX, mouseY)) {
			setDragging(mouseButton == 0);
			update(mouseX, mouseY);
			return true;
		}
		return false;
	}

	@Override
	public boolean isMouseOver(final int mouseX, final int mouseY) {
		return mouseX > getX() && mouseY > getY() && mouseX <= getX() + getWidth() && mouseY <= getY() + getHeight();
	}

	@Override
	public void onMouseReleased(final int mouseX, final int mouseY, final int button) {
		if (isDragging()) {
			onStopDragging();
		}
		setDragging(false);
	}

	@Override
	public abstract void doDrag(int x, int y);

	@Override
	public void drawBackground(final int mouseX, final int mouseY, final float partialTicks) {
		if (getParentElement() != null && getParentElement().isVisible()) {
			Gui.drawRect(getX() - 1, getY() - 1, getX() + getWidth() + 1, getY() + getHeight() + 1, borderColor);
			Gui.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), faceColor);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	@Override
	public void drawForeground(final int mouseX, final int mouseY) {
		if (getParentElement() != null && getParentElement().isVisible()) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			Gui.drawRect(getX() + getSliderXPos(), getY() + getSliderYPos() - 1, getX() + getSliderXPos() + getSliderWidth() + 1, getY() + getSliderYPos() + getSliderHeight() + 1, borderColor);
			Gui.drawRect(getX() + getSliderXPos() + 1, getY() + getSliderYPos(), getX() + getSliderXPos() + getSliderWidth(), getY() + getSliderYPos() + getSliderHeight(), 0xFFCCCCCC);
		}
	}

	@Override
	public boolean onMouseWheel(final int mouseX, final int mouseY, final int direction) {

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
	public IGuiScrollbar setParentElement(final IGuiElement element) {
		parentElement = element;
		return this;
	}

	@Override
	public int getScrollPos() {
		return scrollPos;
	}

	@Override
	public IGuiScrollbar setScrollPos(final int pos) {
		scrollPos = Math.max(getMinPos(), Math.min(getMaxPos(), pos));
		return this;
	}

	@Override
	public void onStopDragging() {
		return;
	}

	@Override
	public int getMinPos() {
		return minPos;
	}

	@Override
	public IGuiScrollbar setMinPos(final int pos) {
		minPos = pos;
		return this;
	}

	@Override
	public int getMaxPos() {
		return maxPos;
	}

	@Override
	public IGuiScrollbar setMaxPos(final int pos) {
		maxPos = pos;
		return this;
	}

	@Override
	public IGuiScrollbar setBounds(final int min, final int max) {
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
	public IGuiScrollbar setSliderWidth(final int width) {
		sliderWidth = width;
		return this;
	}

	@Override
	public int getSliderHeight() {
		return sliderHeight;
	}

	@Override
	public IGuiScrollbar setSliderHeight(final int height) {
		sliderHeight = height;
		return this;
	}

	@Override
	public int getBorderColor() {
		return borderColor;
	}

	@Override
	public IGuiScrollbar setBorderColor(final int color) {
		borderColor = color;
		return this;
	}

	@Override
	public int getFaceColor() {
		return faceColor;
	}

	@Override
	public IGuiScrollbar setFaceColor(final int color) {
		faceColor = color;
		return this;
	}

	@Override
	public IGuiScrollbar setColors(final int border, final int face) {
		return setBorderColor(border).setFaceColor(face);
	}

	@Override
	public boolean isDragging() {
		return dragging;
	}

	@Override
	public void setDragging(final boolean isDragging) {
		dragging = isDragging;
	}

}
