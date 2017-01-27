package p455w0rdslib.client.gui.element;

import net.minecraft.item.ItemStack;
import p455w0rdslib.api.gui.IGuiPlayerListItem;
import p455w0rdslib.util.PlayerUtils;

/**
 * @author p455w0rd
 *
 */
public class GuiPlayerListItem implements IGuiPlayerListItem {

	final String PLAYER_NAME;
	final int HEIGHT;

	public GuiPlayerListItem(String playerName, int listItemHeight) {
		PLAYER_NAME = playerName;
		HEIGHT = listItemHeight;
	}

	@Override
	public String getDisplayText() {
		return PLAYER_NAME;
	}

	@Override
	public ItemStack getIcon() {
		return PlayerUtils.getPlayerSkull(getPlayerName());
	}

	@Override
	public String getPlayerName() {
		return getDisplayText();
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}

}
