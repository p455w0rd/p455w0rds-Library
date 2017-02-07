package p455w0rdslib.api.gui;

import p455w0rdslib.client.gui.element.GuiList;

/**
 * @author p455w0rd
 *
 */
public interface IGuiListItem {

	String getDisplayText();

	int getHeight();

	int getWidth();

	void draw(int x, int y, int backColor, int textColor);

	boolean isDisabled();

	GuiList getParent();

	IGuiListItem setParent(GuiList parent);

}
