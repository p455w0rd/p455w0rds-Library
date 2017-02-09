package p455w0rdslib.client.gui.element;

import net.minecraft.client.gui.Gui;
import p455w0rdslib.api.gui.IGuiList;
import p455w0rdslib.api.gui.IGuiListItem;
import p455w0rdslib.util.RenderUtils;

/**
 * @author p455w0rd
 *
 */
public class GuiListItem implements IGuiListItem {

	final String TEXT;
	final int HEIGHT;
	boolean disabled = false, highlighted = false;
	IGuiList parentElement = null;
	int posX = 0, posY = 0;

	public GuiListItem(String text, int listItemHeight) {
		TEXT = text;
		HEIGHT = listItemHeight;
	}

	@Override
	public IGuiList getParent() {
		return parentElement;
	}

	@Override
	public IGuiListItem setParent(IGuiList parent) {
		parentElement = parent;
		return this;
	}

	@Override
	public void draw(int x, int y, int backColor, int textColor) {
		setX(x);
		setY(y);
		if (getParent() != null) {
			//getParent().drawModalRect(x + 1, y - 3, x + getParent().getWidth(), y + getHeight() + 1, backColor);
			Gui.drawRect(x + 1, y - 3, x + getParent().getWidth(), y + getHeight() + 1, backColor);
		}
		else {
			Gui.drawRect(x + 1, y - 3, x + getWidth(), y + getHeight(), backColor);
		}
		RenderUtils.getFontRenderer().drawStringWithShadow(TEXT, x, y, textColor);
	}

	@Override
	public int getX() {
		return posX;
	}

	@Override
	public IGuiListItem setX(int x) {
		posX = x;
		return this;
	}

	@Override
	public int getY() {
		return posY;
	}

	@Override
	public IGuiListItem setY(int y) {
		posY = y;
		return this;
	}

	public IGuiListItem setHighlighted() {
		setHighlighted(true);
		return this;
	}

	@Override
	public IGuiListItem setHighlighted(boolean isSelected) {
		highlighted = isSelected;
		return this;
	}

	@Override
	public boolean isHighlighted() {
		return highlighted;
	}

	@Override
	public boolean isDisabled() {
		return disabled;
	}

	@Override
	public IGuiListItem setDisabled(boolean isDisabled) {
		disabled = isDisabled;
		return this;
	}

	@Override
	public String getDisplayText() {
		return TEXT;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}

	@Override
	public int getWidth() {
		return RenderUtils.getFontRenderer().getStringWidth(TEXT);
	}

}
