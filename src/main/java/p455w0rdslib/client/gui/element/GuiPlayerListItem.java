package p455w0rdslib.client.gui.element;

import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import p455w0rdslib.api.gui.IGuiPlayerListItem;
import p455w0rdslib.util.GuiUtils;
import p455w0rdslib.util.PlayerUtils;
import p455w0rdslib.util.RenderUtils;

/**
 * @author p455w0rd
 *
 */
public class GuiPlayerListItem extends GuiListItem implements IGuiPlayerListItem {

	String playerName = "";

	public GuiPlayerListItem(String diaplayText, String playerName) {
		super(diaplayText, 12);
		this.playerName = playerName;
	}

	@Override
	public void draw(int x, int y, int backColor, int textColor) {
		setX(x);
		setY(y);
		if (getParent() != null) {
			Gui.drawRect(x + 1, y - 3, x + getParent().getWidth(), y + getHeight() + 1, backColor);
		}
		else {
			Gui.drawRect(x + 18 + 1, y - 3, x + getWidth(), y + getHeight(), backColor);
		}
		GuiUtils.drawItem(getIcon(), x + 2, y - 3);
		RenderUtils.getFontRenderer().drawStringWithShadow(getDisplayText(), x + 18, y + 1, textColor);
	}

	@Override
	public ItemStack getIcon() {
		return PlayerUtils.getPlayerSkull(getPlayerName());
	}

	@Override
	public String getPlayerName() {
		return playerName == "" ? "Notch" : playerName;
	}

}
