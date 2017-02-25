package p455w0rdslib.api.gui;

/**
 * @author p455w0rd
 *
 */
public interface IGuiScrollbar extends IGuiElement {

	void doDrag(int x, int y);

	int getSliderXPos();

	int getSliderYPos();

	IGuiElement getParentElement();

	IGuiScrollbar setParentElement(IGuiElement element);

	int getScrollPos();

	IGuiScrollbar setScrollPos(int pos);

	void onStopDragging();

	int getMinPos();

	IGuiScrollbar setMinPos(int pos);

	int getMaxPos();

	IGuiScrollbar setMaxPos(int pos);

	IGuiScrollbar setBounds(int min, int max);

	int getSliderWidth();

	IGuiScrollbar setSliderWidth(int width);

	int getSliderHeight();

	IGuiScrollbar setSliderHeight(int height);

	int getBorderColor();

	IGuiScrollbar setBorderColor(int color);

	int getFaceColor();

	IGuiScrollbar setFaceColor(int color);

	IGuiScrollbar setColors(int border, int face);

	boolean isDragging();

	void setDragging(boolean isDragging);

}
