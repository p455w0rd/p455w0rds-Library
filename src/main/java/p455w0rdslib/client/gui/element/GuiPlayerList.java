package p455w0rdslib.client.gui.element;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import p455w0rdslib.api.gui.IGuiPlayerListItem;

/**
 * @author p455w0rd
 *
 */
public class GuiPlayerList extends GuiElement {

	int numRows;
	List<IGuiPlayerListItem> playerList;

	public GuiPlayerList(Gui guiIn, GuiPos pos, int elementWidth, int elementHeight, int itemsToDisplay) {
		this(guiIn, pos, elementWidth, elementHeight, itemsToDisplay, null);
	}

	public GuiPlayerList(Gui guiIn, GuiPos pos, int elementWidth, int elementHeight, int itemsToDisplay, @Nullable List<IGuiPlayerListItem> playerList) {
		super(guiIn, pos, elementWidth, elementHeight);
		numRows = itemsToDisplay;
		setPlayerList(playerList == null ? Lists.newArrayList() : playerList);
	}

	public void draw(final GuiContainer g) {

	}

	public GuiPlayerList setPlayerList(List<IGuiPlayerListItem> list) {
		playerList = list;
		return this;
	}

	public List<IGuiPlayerListItem> getPlayerList() {
		return playerList;
	}

	public int getNumberOfItemsToDisplay() {
		return numRows;
	}

	public int size() {
		return getPlayerList().size();
	}

	public int getTotalListHeight() {
		int totalHeight = 0;
		for (IGuiPlayerListItem entry : getPlayerList()) {
			totalHeight += entry.getHeight();
		}
		return totalHeight;
	}

	public int getDisplayedListHeight() {
		int displayHeight = 0;
		for (int i = 0; i < getNumberOfItemsToDisplay(); i++) {
			displayHeight += getPlayerList().get(i).getHeight();
		}
		return displayHeight;
	}
}
