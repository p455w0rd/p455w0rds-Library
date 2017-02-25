package p455w0rdslib.client.gui.element;

import p455w0rdslib.api.gui.IGuiList;
import p455w0rdslib.api.gui.IGuiScrollbar;
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
	public void doDrag(int x, int y) {
		y += Math.round(getSliderHeight() * (y / (float) getHeight()) + (getSliderHeight() * 0.25f));
		int newPos = getMinPos() + ((getMaxPos() - getMinPos()) * y / getHeight());

		int direction = getScrollPos() == newPos ? 0 : getScrollPos() > newPos ? 1 : -1;
		if (getParentElement() != null && getParentElement() instanceof IGuiList) {
			boolean shouldMove = newPos != 0 && (getMaxPos() - getMinPos() + 1) % newPos == 0;
			if (direction > 0 && shouldMove) {
				((IGuiList) getParentElement()).scrollUp();
			}
			else if (direction < 0 && shouldMove) {
				((IGuiList) getParentElement()).scrollDown();
			}
		}
		setScrollPos(newPos);
	}

	@Override
	public IGuiScrollbar setBounds(int min, int max) {
		int dist = max - min;
		setSliderHeight(dist <= 0 ? getHeight() : Math.max(getHeight() / dist, 9));
		setSliderWidth(getWidth());
		return super.setBounds(min, max);
	}

}
