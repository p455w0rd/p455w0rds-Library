package p455w0rdslib.client.gui.element;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import p455w0rdslib.api.gui.IGuiList;
import p455w0rdslib.api.gui.IGuiListItem;
import p455w0rdslib.api.gui.IModularGui;

/**
 * @author p455w0rd
 *
 */
public class GuiList extends GuiElement implements IGuiList {

	int selectedIndex = 0, firstIndex = 0, borderColor = 0xFF333333, backgroundColor = 0xFF333333,
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

	@Override
	public int drawListItem(int elementIndex, int x, int y) {
		IGuiListItem element = getList().get(elementIndex);
		if (element.getParent() == null) {
			element.setParent(this);
		}
		if (element.isHighlighted()) {
			element.draw(x, y + 4, element.isDisabled() ? getDisabledBackgroundColor() : getHighlightColor(), element.isDisabled() ? getDisabledTextColor() : getSelectedTextColor());
		}
		else {
			if (elementIndex == selectedIndex) {
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
		Gui.drawRect(getX(), getY(), getX() + getWidth() + 1, getY() + getHeight() + 1, getBorderColor());
		Gui.drawRect(getX() + 1, getY() + 1, getX() + getWidth(), getY() + getHeight(), getBackgroundColor());
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

	@Override
	public int getHighlightColor() {
		return highlightColor;
	}

	@Override
	public IGuiList setHighlightColor(int color) {
		highlightColor = color;
		return this;
	}

	@Override
	public int getDisabledBackgroundColor() {
		return disabledBackgroundColor;
	}

	@Override
	public IGuiList setDisabledBackgroundColor(int color) {
		disabledBackgroundColor = color;
		return this;
	}

	@Override
	public int getTextColor() {
		return textColor;
	}

	@Override
	public IGuiList setTextColor(int color) {
		textColor = color;
		return this;
	}

	@Override
	public int getBackgroundColor() {
		return backgroundColor;
	}

	@Override
	public IGuiList setBackgroundColor(int color) {
		backgroundColor = color;
		return this;
	}

	@Override
	public int getSelectedTextColor() {
		return selectedTextColor;
	}

	@Override
	public IGuiList setSelectedTextColor(int color) {
		selectedTextColor = color;
		return this;
	}

	@Override
	public int getSelectedBackgroundColor() {
		return selectedBackgroundColor;
	}

	@Override
	public IGuiList setSelectedBackgroundColor(int color) {
		selectedBackgroundColor = color;
		return this;
	}

	@Override
	public int getDisabledTextColor() {
		return disabledTextColor;
	}

	@Override
	public IGuiList setDisabledTextColor(int color) {
		disabledTextColor = color;
		return this;
	}

	@Override
	public int getBorderColor() {
		return borderColor;
	}

	@Override
	public IGuiList setBorderColor(int color) {
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
						setSelectedIndex(i);
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

	@Override
	public boolean isMouseOverElement(IGuiListItem element, int mouseX, int mouseY) {
		return mouseX > element.getX() && mouseX < element.getX() + element.getWidth() && mouseY > element.getY() - 6 && mouseY < element.getY() + element.getHeight() + 6;
	}

	@Override
	public IGuiList setClickedElement(IGuiListItem element) {
		if (!isEmpty()) {
			for (int i = 0; i < getList().size(); i++) {
				if (element.equals(getList().get(i))) {
					setSelectedIndex(i);
					break;
				}
			}
		}
		return this;
	}

	@Override
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

	@Override
	public IGuiList add(IGuiListItem listItem) {
		getList().add(listItem);
		return this;
	}

	@Override
	public IGuiList addAll(List<IGuiListItem> list) {
		listList.addAll(list);
		return this;
	}

	@Override
	public void remove(IGuiListItem listItem) {
		int itemIndex = getList().indexOf(listItem);
		if (getList().remove(listItem)) {
			if (itemIndex < firstIndex) {
				firstIndex--;
			}
			if (itemIndex < selectedIndex) {
				selectedIndex--;
			}
		}
	}

	@Override
	public void scrollTo(int index) {
		if (index >= 0 && index < getList().size()) {
			firstIndex = index;
		}
	}

	@Override
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
	public boolean onMouseWheel(int mouseX, int mouseY, int direction) {
		if (direction > 0) {
			scrollUp();
		}
		else if (direction < 0) {
			scrollDown();
		}
		return true;
	}

	public void onScroll(int firstIndexIn) {
	}

	@Override
	public void scrollUp() {
		if (firstIndex > 0) {
			firstIndex--;
		}
		onScroll(firstIndex);
	}

	@Override
	public void clear() {
		getList().clear();
	}

	@Override
	public int getSelectedIndex() {
		return selectedIndex;
	}

	@Override
	public IGuiList setSelectedIndex(int index) {
		if (index == selectedIndex) {
			selectedIndex = -1;
		}
		else {
			if (index < getList().size()) {
				selectedIndex = index;
			}
		}
		return this;
	}

	@Override
	public IGuiListItem getSelectedElement() {
		if (selectedIndex == -1 || selectedIndex >= getList().size()) {
			return null;
		}
		return getList().get(selectedIndex);
	}

	@Override
	public String getSelectedText() {
		return getSelectedElement() == null ? "" : getSelectedElement().getDisplayText();
	}

	@Override
	public IGuiList setList(List<IGuiListItem> list) {
		listList = list;
		return this;
	}

	@Override
	public List<IGuiListItem> getList() {
		return listList;
	}

	@Override
	public int size() {
		return getList().size();
	}

	@Override
	public int getTotalListHeight() {
		int totalHeight = 0;
		for (IGuiListItem entry : getList()) {
			totalHeight += entry.getHeight() + 2;
		}
		return totalHeight;
	}

}
