package p455w0rdslib.client.gui.element;

import p455w0rdslib.api.gui.IModularGui;

/**
 * @author p455w0rd
 *
 */
public abstract class GuiScrollbar extends GuiElement {

	protected int scrollPos, minPos, maxPos, sliderWidth, sliderHeight, borderColor = 0xFF000000,
			faceColor = 0xFF333333;
	protected boolean dragging;
	GuiElement parentElement;

	public GuiScrollbar(IModularGui gui, GuiPos posIn, int width, int height, int max) {
		this(gui, posIn, width, height, max, 0);
	}

	public GuiScrollbar(IModularGui gui, GuiPos posIn, int width, int height, int max, int min) {
		super(gui, posIn, width, height);
		minPos = min;
		maxPos = max;
	}

	@Override
	public void update(int mouseX, int mouseY) {
		if (isDragging()) {
			doDrag(mouseX - getX(), mouseY - getY());
		}
	}

	@Override
	public boolean onMousePressed(int mouseX, int mouseY, int mouseButton) {
		setDragging(mouseButton == 0);
		update(mouseX, mouseY);
		return true;
	}

	@Override
	public void onMouseReleased(int mouseX, int mouseY, int button) {
		if (isDragging()) {
			onStopDragging();
		}
		setDragging(false);
	}

	protected abstract void doDrag(int x, int y);

	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks) {
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
	}

	@Override
	public boolean onMouseWheel(int mouseX, int mouseY, int direction) {
		if (direction > 0) {
			setScrollPos(getScrollPos() - 1);
		}
		else if (direction < 0) {
			setScrollPos(getScrollPos() + 1);
		}
		if (getParentElement() != null) {
			getParentElement().onMouseWheel(mouseX, mouseY, direction);
		}
		return true;
	}

	public int getSliderXPos() {
		return 0;
	}

	public int getSliderYPos() {
		return 0;
	}

	public GuiElement getParentElement() {
		return parentElement;
	}

	public GuiScrollbar setParentElement(GuiElement element) {
		parentElement = element;
		return this;
	}

	public int getScrollPos() {
		return scrollPos;
	}

	public GuiScrollbar setScrollPos(int pos) {
		scrollPos = Math.max(getMinPos(), Math.min(getMaxPos(), pos));
		if (pos != getScrollPos()) {
			scrollPos = pos;
			onValueChanged(scrollPos);
		}
		return this;
	}

	public void onValueChanged(int value) {
		return;
	}

	public void onStopDragging() {
		return;
	}

	public int getMinPos() {
		return minPos;
	}

	public GuiScrollbar setMinPos(int pos) {
		minPos = pos;
		return this;
	}

	public int getMaxPos() {
		return maxPos;
	}

	public GuiScrollbar setMaxPos(int pos) {
		maxPos = pos;
		return this;
	}

	public GuiScrollbar setBounds(int min, int max) {
		setMinPos(min);
		setMaxPos(max);
		setScrollPos(getScrollPos());
		return this;
	}

	public int getSliderWidth() {
		return sliderWidth;
	}

	public GuiScrollbar setSliderWidth(int width) {
		sliderWidth = width;
		return this;
	}

	public int getSliderHeight() {
		return sliderHeight;
	}

	public GuiScrollbar setSliderHeight(int height) {
		sliderHeight = height;
		return this;
	}

	public int getBorderColor() {
		return borderColor;
	}

	public GuiScrollbar setBorderColor(int color) {
		borderColor = color;
		return this;
	}

	public int getFaceColor() {
		return faceColor;
	}

	public GuiScrollbar setFaceColor(int color) {
		faceColor = color;
		return this;
	}

	public GuiScrollbar setColors(int border, int face) {
		return setBorderColor(border).setFaceColor(face);
	}

	public boolean isDragging() {
		return dragging;
	}

	public void setDragging(boolean isDragging) {
		dragging = isDragging;
	}

}
