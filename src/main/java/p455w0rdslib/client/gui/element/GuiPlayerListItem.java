package p455w0rdslib.client.gui.element;

import net.minecraft.item.ItemStack;
import p455w0rdslib.util.GuiUtils;
import p455w0rdslib.util.PlayerUtils;

/**
 * @author p455w0rd
 *
 */
public class GuiPlayerListItem extends GuiListItem {

	public GuiPlayerListItem(String playerName, int listItemHeight) {
		super(playerName, listItemHeight);
	}

	@Override
	public void draw(int x, int y, int backColor, int textColor) {
		GuiUtils.drawItem(getIcon(), 1, y);
		GuiUtils.getFontRenderer().drawStringWithShadow(getPlayerName(), x + 7, y, textColor);
	}

	public ItemStack getIcon() {
		return PlayerUtils.getPlayerSkull(getPlayerName());
	}

	public String getPlayerName() {
		return getDisplayText();
	}

}
