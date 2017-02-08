package p455w0rdslib.client.gui.element;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.gui.GuiScreen;
import p455w0rdslib.api.gui.IGuiListItem;
import p455w0rdslib.api.gui.IModularGui;

/**
 * @author p455w0rd
 *
 */
public class GuiList extends GuiElement {

	int clickedIndex = 0, firstIndex = 0, borderColor = 0xFF333333, backgroundColor = 0xFF333333,
			selectedBackgroundColor = 0xFF333333, disabledBackgroundColor = 0xFF000000, textColor = 0xFFFFFFFF,
			disabledTextColor = 0xFF333333, selectedTextColor = 0xFF00FF00, highlightColor = 0x6617FF6D;
	List<IGuiListItem> listList;

	public GuiList(IModularGui guiIn, GuiPos pos, int elementWidth, int elementHeight) {
		this(guiIn, pos, elementWidth, elementHeight, null);
	}

	public GuiList(IModularGui guiIn, GuiPos pos, int elementWidth, int elementHeight, @Nullable List<IGuiListItem> list) {
		super(guiIn, pos, elementWidth, elementHeight);
		if (list != null) {
			setList(list);
		}
	}

	protected int drawListItem(int elementIndex, int x, int y) {
		IGuiListItem element = getList().get(elementIndex);
		if (element.getParent() == null) {
			element.setParent(this);
		}
		if (element.isHighlighted()) {
			element.draw(x, y + 4, element.isDisabled() ? getDisabledBackgroundColor() : getHighlightColor(), element.isDisabled() ? getDisabledTextColor() : getSelectedTextColor());
		}
		else {
			if (elementIndex == clickedIndex) {
				element.draw(x, y + 4, element.isDisabled() ? getDisabledBackgroundColor() : getSelectedBackgroundColor(), element.isDisabled() ? getDisabledTextColor() : getSelectedTextColor());
			}
			else {
				element.draw(x, y + 4, element.isDisabled() ? getDisabledBackgroundColor() : getBackgroundColor(), element.isDisabled() ? getDisabledTextColor() : getTextColor());
			}
		}

		return element.getHeight() + 5;
	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float partialTicks) {
		drawModalRect(getX(), getY(), getX() + getWidth() + 1, getY() + getHeight() + 1, getBorderColor());
		drawModalRect(getX() + 1, getY() + 1, getX() + getWidth(), getY() + getHeight(), getBackgroundColor());
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		int heightDrawn = 0;
		int nextElement = firstIndex;

		int e = size();
		while (nextElement < e && heightDrawn + 3 < getHeight()) {
			heightDrawn += drawListItem(nextElement, getX(), getY() + heightDrawn);
			nextElement++;
		}
	}

	public int getHighlightColor() {
		return highlightColor;
	}

	public GuiList setHighlightColor(int color) {
		highlightColor = color;
		return this;
	}

	public int getDisabledBackgroundColor() {
		return disabledBackgroundColor;
	}

	public GuiList setDisabledBackgroundColor(int color) {
		disabledBackgroundColor = color;
		return this;
	}

	public int getTextColor() {
		return textColor;
	}

	public GuiList setTextColor(int color) {
		textColor = color;
		return this;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public GuiList setBackgroundColor(int color) {
		backgroundColor = color;
		return this;
	}

	public int getSelectedTextColor() {
		return selectedTextColor;
	}

	public GuiList setSelectedTextColor(int color) {
		selectedTextColor = color;
		return this;
	}

	public int getSelectedBackgroundColor() {
		return selectedBackgroundColor;
	}

	public GuiList setSelectedBackgroundColor(int color) {
		selectedBackgroundColor = color;
		return this;
	}

	public int getDisabledTextColor() {
		return disabledTextColor;
	}

	public GuiList setDisabledTextColor(int color) {
		disabledTextColor = color;
		return this;
	}

	public int getBorderColor() {
		return borderColor;
	}

	public GuiList setBorderColor(int color) {
		borderColor = color;
		return this;
	}

	@Override
	public boolean onClick(int mouseX, int mouseY) {
		int heightChecked = 0;
		for (int i = firstIndex; i < getList().size(); i++) {
			if (heightChecked >= getHeight()) {
				break;
			}

			int elementHeight = getList().get(i).getHeight();
			if (getY() + heightChecked <= mouseY + 12 && getY() + heightChecked + elementHeight >= mouseY && mouseX >= getX() && mouseX <= getX() + getWidth()) {
				if (!getList().get(i).isDisabled()) {

					if (GuiScreen.isCtrlKeyDown()) {
						getList().get(i).setHighlighted(!getList().get(i).isHighlighted());
					}
					else {
						setClickedIndex(i);
					}
				}
				break;
			}

			heightChecked += elementHeight + 8;
		}

		/*
		for (IGuiListItem element : getList()) {
			if (isMouseOverElement(element, getGui().getX() - mouseX, mouseY - getGui().getY())) {
				if (!element.isDisabled()) {
					if (GuiScreen.isCtrlKeyDown()) {
						element.setHighlighted(!element.isHighlighted());
					}
					else {
						setClickedElement(element);
					}
				}
			}
		}
		*/
		return true;
	}

	public boolean isMouseOverElement(IGuiListItem element, int mouseX, int mouseY) {
		return mouseX > element.getX() && mouseX < element.getX() + element.getWidth() && mouseY > element.getY() - 6 && mouseY < element.getY() + element.getHeight() + 6;
	}

	public GuiList setClickedElement(IGuiListItem element) {
		if (!isEmpty()) {
			for (int i = 0; i < getList().size(); i++) {
				if (element.equals(getList().get(i))) {
					setClickedIndex(i);
					break;
				}
			}
		}
		return this;
	}

	public boolean isEmpty() {
		return getList().isEmpty();
	}

	public int getPrevScrollPos() {

		int position = size() - 1;
		if (position < 0) {
			return 0;
		}
		int heightUsed = 0;

		while (position >= 0 && heightUsed < getHeight()) {
			heightUsed += getList().get(position--).getHeight();
		}
		if (heightUsed > getHeight()) {
			++position;
		}
		return position + 1;
	}

	public GuiList add(IGuiListItem listItem) {
		getList().add(listItem);
		return this;
	}

	public GuiList addAll(List<IGuiListItem> list) {
		listList.addAll(list);
		return this;
	}

	public void remove(IGuiListItem listItem) {
		int itemIndex = getList().indexOf(listItem);
		if (getList().remove(listItem)) {
			if (itemIndex < firstIndex) {
				firstIndex--;
			}
			if (itemIndex < clickedIndex) {
				clickedIndex--;
			}
		}
	}

	public void scrollTo(int index) {
		if (index >= 0 && index < getList().size()) {
			firstIndex = index;
		}
	}

	public void scrollDown() {
		int heightDisplayed = 0;
		int elementsDisplayed = 0;

		for (int i = firstIndex; i < getList().size(); i++) {
			if (heightDisplayed > getHeight()) {
				break;
			}
			heightDisplayed += getList().get(i).getHeight() + 10;
			elementsDisplayed++;
		}
		if (firstIndex + elementsDisplayed < getList().size()) {
			firstIndex++;
		}
		onScroll(firstIndex);
	}

	@Override
	public boolean onMouseWheel(int mouseX, int mouseY, int movement) {
		if (movement > 0) {
			scrollUp();
		}
		else if (movement < 0) {
			scrollDown();
		}
		return true;
	}

	public void onScroll(int firstIndexIn) {
	}

	public void scrollUp() {
		if (firstIndex > 0) {
			firstIndex--;
		}
		onScroll(firstIndex);
	}

	public void clear() {
		getList().clear();
	}

	public int getClickedIndex() {
		return clickedIndex;
	}

	public GuiList setClickedIndex(int index) {
		if (index == clickedIndex) {
			clickedIndex = -1;
		}
		else {
			if (index < getList().size()) {
				clickedIndex = index;
			}
		}
		return this;
	}

	public IGuiListItem getSelectedElement() {
		if (clickedIndex == -1 || clickedIndex >= getList().size()) {
			return null;
		}
		return getList().get(clickedIndex);
	}

	public String getSelectedText() {
		return getSelectedElement() == null ? "" : getSelectedElement().getDisplayText();
	}

	public GuiList setList(List<IGuiListItem> list) {
		listList = list;
		return this;
	}

	public List<IGuiListItem> getList() {
		return listList;
	}

	public int size() {
		return getList().size();
	}

	public int getTotalListHeight() {
		int totalHeight = 0;
		for (IGuiListItem entry : getList()) {
			totalHeight += entry.getHeight() + 2;
		}
		return totalHeight;
	}

}
