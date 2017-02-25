package p455w0rdslib.api.gui;

import java.util.UUID;

import net.minecraft.item.ItemStack;

/**
 * @author p455w0rd
 *
 */
public interface IGuiPlayerListItem extends IGuiListItem {

	ItemStack getIcon();

	String getPlayerName();

	UUID getID();

}
