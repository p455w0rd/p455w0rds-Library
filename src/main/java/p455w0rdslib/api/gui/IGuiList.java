package p455w0rdslib.api.gui;

import java.util.List;

/**
 * @author p455w0rd
 *
 */
public interface IGuiList extends IGuiElement {

	int drawListItem(int elementIndex, int x, int y);

	int getHighlightColor();

	IGuiList setHighlightColor(int color);

	int getDisabledBackgroundColor();

	IGuiList setDisabledBackgroundColor(int color);

	int getTextColor();

	IGuiList setTextColor(int color);

	int getBackgroundColor();

	IGuiList setBackgroundColor(int color);

	int getSelectedTextColor();

	IGuiList setSelectedTextColor(int color);

	int getSelectedBackgroundColor();

	IGuiList setSelectedBackgroundColor(int color);

	int getDisabledTextColor();

	IGuiList setDisabledTextColor(int color);

	int getBorderColor();

	IGuiList setBorderColor(int color);

	boolean isMouseOverElement(IGuiListItem element, int mouseX, int mouseY);

	IGuiList setClickedElement(IGuiListItem element);

	boolean isEmpty();

	IGuiList add(IGuiListItem listItem);

	IGuiList addAll(List<IGuiListItem> list);

	void remove(IGuiListItem listItem);

	void scrollTo(int index);

	void scrollDown();

	@Override
	boolean onMouseWheel(int mouseX, int mouseY, int direction);

	void scrollUp();

	void clear();

	int getSelectedIndex();

	IGuiList setSelectedIndex(int index);

	IGuiListItem getSelectedElement();

	String getSelectedText();

	IGuiList setList(List<IGuiListItem> list);

	List<IGuiListItem> getList();

	int size();

	int getTotalListHeight();

}
