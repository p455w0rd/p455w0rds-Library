package p455w0rdslib.client.gui.element;

import p455w0rdslib.api.gui.IModularGui;

/**
 * @author p455w0rd
 *
 */
public class GuiVScrollbar extends GuiScrollbar {

	public GuiVScrollbar(IModularGui gui, GuiPos posIn, int width, int height, int max) {
		this(gui, posIn, width, height, max, 0);
	}

	public GuiVScrollbar(IModularGui gui, GuiPos posIn, int width, int height, int max, int min) {
		super(gui, posIn, width, height, max, min);
	}

	@Override
	public int getSliderYPos() {
		int dist = getMaxPos() - getMinPos();
		int maxPos = getHeight() - getSliderHeight();
		return Math.min(dist == 0 ? 0 : maxPos * (getScrollPos() - getMinPos()) / dist, maxPos);
	}

	@Override
	protected void doDrag(int x, int y) {
		y += Math.round(getSliderHeight() * (y / (float) getHeight()) + (getSliderHeight() * 0.25f));
		setScrollPos(getMinPos() + ((getMaxPos() - getMinPos()) * y / getHeight()));
	}

	@Override
	public GuiScrollbar setBounds(int min, int max) {
		int dist = max - min;
		setSliderHeight(dist <= 0 ? getHeight() : Math.max(getHeight() / ++dist, 9));
		setSliderWidth(getWidth());
		return super.setBounds(min, max);
	}

}
