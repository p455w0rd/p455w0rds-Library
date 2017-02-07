package p455w0rdslib.client.gui.element;

import p455w0rdslib.api.gui.IGuiListItem;
import p455w0rdslib.util.GuiUtils;
import p455w0rdslib.util.RenderUtils;

/**
 * @author p455w0rd
 *
 */
public class GuiListItem implements IGuiListItem {

	final String TEXT;
	final int HEIGHT;
	boolean disabled = false;
	GuiList parentElement = null;

	public GuiListItem(String text, int listItemHeight) {
		TEXT = text;
		HEIGHT = listItemHeight;
	}

	@Override
	public GuiList getParent() {
		return parentElement;
	}

	@Override
	public GuiListItem setParent(GuiList parent) {
		parentElement = parent;
		return this;
	}

	@Override
	public void draw(int x, int y, int backColor, int textColor) {
		GuiUtils.drawGradientRect((getParent() != null ? getParent().getGui() : RenderUtils.mc().currentScreen), x + 2, y - 3, x + (getParent() == null ? getWidth() : getParent().getWidth()), y + getHeight(), backColor, backColor);
		RenderUtils.getFontRenderer().drawStringWithShadow(TEXT, x, y, textColor);
	}

	@Override
	public boolean isDisabled() {
		return disabled;
	}

	public GuiListItem setDisabled(boolean isDisabled) {
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
