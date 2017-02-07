package p455w0rdslib.client.gui.element;

import static org.lwjgl.opengl.GL11.glPopMatrix;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import p455w0rdslib.api.gui.IGuiListItem;

/**
 * @author p455w0rd
 *
 */
public class GuiList extends GuiElement {

	int selectedIndex = 0, firstIndex = 0, borderColor = 0xFF333333, backgroundColor = 0xFF333333,
			selectedBackgroundColor = 0xFF333333, disabledBackgroundColor = 0xFF000000, textColor = 0xFFFFFFFF,
			disabledTextColor = 0xFF333333, selectedTextColor = 0xFF00FF00;
	List<IGuiListItem> listList;

	public GuiList(Gui guiIn, GuiPos pos, int elementWidth, int elementHeight) {
		this(guiIn, pos, elementWidth, elementHeight, null);
	}

	public GuiList(Gui guiIn, GuiPos pos, int elementWidth, int elementHeight, @Nullable List<IGuiListItem> list) {
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
		if (elementIndex == selectedIndex) {
			element.draw(x, y + 4, element.isDisabled() ? getDisabledBackgroundColor() : getSelectedBackgroundColor(), element.isDisabled() ? getDisabledTextColor() : getSelectedTextColor());
		}
		else {
			element.draw(x, y + 4, element.isDisabled() ? getDisabledBackgroundColor() : 0xFF999999, element.isDisabled() ? getDisabledTextColor() : getTextColor());
		}

		return element.getHeight() + 5;
	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float partialTicks) {
		drawModalRect(getX(), getY(), getX() + getWidth() + 1, getY() + getHeight() + 1, getBorderColor());
		drawModalRect(getX() + 1, getY() + 1, getX() + getWidth(), getY() + getHeight(), 0xFF333333);
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		int heightDrawn = 0;
		int nextElement = firstIndex;

		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();

		//GL11.glEnable(GL_STENCIL_TEST);
		//drawStencil(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 1);

		//glPushMatrix();
		//glTranslated(-scrollHoriz, 0, 0);

		int e = size();
		while (nextElement < e && heightDrawn + 3 < getHeight()) {
			heightDrawn += drawListItem(nextElement, getX(), getY() + heightDrawn);
			nextElement++;
		}
		//glPopMatrix();
		//glDisable(GL_STENCIL_TEST);
		glPopMatrix();
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
			if (getY() + heightChecked <= mouseY && getY() + heightChecked + elementHeight >= mouseY && mouseX >= getX() && mouseX <= getX() + getWidth()) {
				if (!getList().get(i).isDisabled()) {
					setSelectedIndex(i);
				}
				//onElementClicked(getList().get(i));
				break;
			}
			heightChecked += elementHeight + 8;
		}
		return true;
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
			if (itemIndex < selectedIndex) {
				selectedIndex--;
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

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public GuiList setSelectedIndex(int index) {
		if (index < getList().size()) {
			selectedIndex = index;
		}
		return this;
	}

	public IGuiListItem getSelectedElement() {
		if (selectedIndex == -1 || selectedIndex >= getList().size()) {
			return null;
		}
		return getList().get(selectedIndex);
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
