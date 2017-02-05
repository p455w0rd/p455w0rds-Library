package p455w0rdslib.client.gui.element;

import net.minecraft.item.ItemStack;
import p455w0rdslib.util.GuiUtils;
import p455w0rdslib.util.PlayerUtils;
import p455w0rdslib.util.RenderUtils;

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
		GuiUtils.drawItem(getIcon(), 5, y - 3);
		RenderUtils.getFontRenderer().drawStringWithShadow(getPlayerName(), x + 15, y + 1, textColor);
	}

	public ItemStack getIcon() {
		return PlayerUtils.getPlayerSkull(getPlayerName());
	}

	public String getPlayerName() {
		return getDisplayText();
	}

}
