package p455w0rdslib.client.gui.element;

import p455w0rdslib.api.gui.IGuiListItem;
import p455w0rdslib.util.GuiUtils;

/**
 * @author p455w0rd
 *
 */
public class GuiListItem implements IGuiListItem {

	final String TEXT;
	final int HEIGHT;

	public GuiListItem(String text, int listItemHeight) {
		TEXT = text;
		HEIGHT = listItemHeight;
	}

	@Override
	public void draw(int x, int y, int backColor, int textColor) {
		GuiUtils.getFontRenderer().drawStringWithShadow(TEXT, x, y, textColor);
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
		return GuiUtils.getFontRenderer().getStringWidth(TEXT);
	}

}
