package p455w0rdslib.util;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * @author p455w0rd
 *
 */
public class PotionUtils {

	public static void effectPlayer(EntityPlayer player, Potion potion, int amplifier) {
		if ((player.getActivePotionEffect(potion) == null) || (player.getActivePotionEffect(potion).getDuration() >= 0)) {
			player.addPotionEffect(new PotionEffect(potion, 318, amplifier, false, false));
		}
	}

	public static boolean isPotionActive(EntityPlayer player, Potion potion) {
		return player.getActivePotionEffect(potion) != null;
	}

	public static void clearPotionEffect(EntityPlayer player, Potion potion, int amplifier) {
		Iterator<PotionEffect> iterator = player.getActivePotionEffects().iterator();
		while (iterator.hasNext()) {
			PotionEffect currEffect = iterator.next();
			Potion currPotion = currEffect.getPotion();
			if ((currPotion == potion) && (player.getActivePotionEffect(potion) != null)) {
				currPotion.removeAttributesModifiersFromEntity(player, player.getAttributeMap(), currEffect.getAmplifier());
				iterator.remove();
			}
		}
	}

	public static ItemStack createSplashPotionStack(String name) {
		ItemStack stack = new ItemStack(Items.SPLASH_POTION);
		net.minecraft.potion.PotionUtils.addPotionToItemStack(stack, PotionTypes.SWIFTNESS);
		return stack;
	}
}