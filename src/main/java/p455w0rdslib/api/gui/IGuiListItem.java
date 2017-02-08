package p455w0rdslib.api.gui;

import p455w0rdslib.client.gui.element.GuiList;

/**
 * @author p455w0rd
 *
 */
public interface IGuiListItem {

	int getX();

	IGuiListItem setX(int y);

	int getY();

	IGuiListItem setY(int y);

	String getDisplayText();

	int getHeight();

	int getWidth();

	void draw(int x, int y, int backColor, int textColor);

	boolean isDisabled();

	IGuiListItem setDisabled(boolean isDisabled);

	GuiList getParent();

	IGuiListItem setParent(GuiList parent);

	boolean isHighlighted();

	IGuiListItem setHighlighted(boolean isSelected);

}
