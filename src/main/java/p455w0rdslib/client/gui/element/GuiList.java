package p455w0rdslib.client.gui.element;

import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import p455w0rdslib.api.gui.IGuiListItem;

/**
 * @author p455w0rd
 *
 */
public class GuiList extends GuiElement {

	int numRows, selectedIndex = 0, firstIndex = 0, borderColor = 0xFF00FF00, backgroundColor = 0xFF333333,
			textColor = 0xFFFFFFFF;
	List<IGuiListItem> list;

	public GuiList(Gui guiIn, GuiPos pos, int elementWidth, int elementHeight) {
		this(guiIn, pos, elementWidth, elementHeight, null);
	}

	public GuiList(Gui guiIn, GuiPos pos, int elementWidth, int elementHeight, @Nullable List<IGuiListItem> list) {
		super(guiIn, pos, elementWidth, elementHeight);
		setList(list == null ? Lists.newArrayList() : list);
	}

	protected int drawListItem(int elementIndex, int x, int y) {
		IGuiListItem element = getList().get(elementIndex);
		if (elementIndex == selectedIndex) {
			element.draw(x + 1, y + 2, 0xFF000000, 0xFF00FF00);
		}
		else {
			element.draw(x + 1, y + 2, backgroundColor, textColor);
		}

		return element.getHeight() + 2;
	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float partialTicks) {
		drawModalRect(getX(), getY(), getX() + getWidth() + 1, getY() + getHeight() + 1, borderColor);
		drawModalRect(getX() + 1, getY() + 1, getX() + getWidth(), getY() + getHeight(), backgroundColor);
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		int heightDrawn = 0;
		int nextElement = firstIndex;

		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();

		GL11.glEnable(GL_STENCIL_TEST);
		drawStencil(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 1);

		glPushMatrix();
		//glTranslated(-scrollHoriz, 0, 0);

		int e = getList().size();
		while (nextElement < e && heightDrawn <= getHeight()) {
			heightDrawn += drawListItem(nextElement, getX(), getY() + heightDrawn);
			nextElement++;
		}
		glPopMatrix();
		glDisable(GL_STENCIL_TEST);
		glPopMatrix();
	}

	@Override
	public boolean onClick(int mouseX, int mouseY) {
		int heightChecked = 0;
		for (int i = firstIndex; i < getList().size(); i++) {
			if (heightChecked > getDisplayedHeight()) {
				break;
			}
			int elementHeight = getList().get(i).getHeight();
			if (getY() + heightChecked <= mouseY && getY() + heightChecked + elementHeight >= mouseY) {
				setSelectedIndex(i);
				//onElementClicked(getList().get(i));
				break;
			}
			heightChecked += elementHeight;
		}
		return true;
	}

	public GuiList add(IGuiListItem listItem) {
		getList().add(listItem);
		return this;
	}

	public GuiList addAll(List<IGuiListItem> list) {
		this.list.addAll(list);
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
			if (heightDisplayed + getList().get(i).getHeight() > getDisplayedHeight()) {
				break;
			}
			heightDisplayed += getList().get(i).getHeight();
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
		this.list = list;
		return this;
	}

	public List<IGuiListItem> getList() {
		return list;
	}

	public int getNumberOfItemsToDisplay() {
		return numRows;
	}

	public int size() {
		return getList().size();
	}

	public int getTotalListHeight() {
		int totalHeight = 0;
		for (IGuiListItem entry : getList()) {
			totalHeight += entry.getHeight();
		}
		return totalHeight;
	}

	public int getDisplayedHeight() {
		int displayHeight = 0;
		for (int i = 0; i < getNumberOfItemsToDisplay(); i++) {
			displayHeight += getList().get(i).getHeight();
		}
		return displayHeight;
	}
}
