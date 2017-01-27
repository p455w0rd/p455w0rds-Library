package p455w0rdslib.api.gui;

import net.minecraft.item.ItemStack;

/**
 * @author p455w0rd
 *
 */
public interface IGuiPlayerListItem extends IGuiListItem {

	String getPlayerName();

	ItemStack getIcon();

	int getHeight();

}
