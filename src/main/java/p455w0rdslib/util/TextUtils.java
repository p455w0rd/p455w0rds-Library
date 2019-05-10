package p455w0rdslib.util;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

/**
 * @author p455w0rd
 *
 */
@SuppressWarnings("deprecation")
public class TextUtils {

	public static List<TextFormatting> RAINBOW_COLORS = Lists.newArrayList(TextFormatting.AQUA, TextFormatting.YELLOW, TextFormatting.GOLD, TextFormatting.BLUE, TextFormatting.GREEN, TextFormatting.RED, TextFormatting.LIGHT_PURPLE);
	public static TextFormatting BOLD = TextFormatting.BOLD;

	public static String rainbow(final String text) {
		return rainbow(text, true);
	}

	public static String rainbow(final String text, final boolean bold) {
		int colorIndex = 5;
		final int colorMaxIndex = RAINBOW_COLORS.size() - 1;
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			builder.append(RAINBOW_COLORS.get(colorIndex) + "" + (bold ? BOLD : ""));
			builder.append(text.substring(i, i + 1));
			colorIndex++;
			if (colorIndex > colorMaxIndex) {
				colorIndex = 0;
			}
		}
		return builder.toString();
	}

	public static String translate(final String str) {
		return I18n.translateToLocal(str);
	}

	public static String translate(final String str, final Object... objects) {
		return I18n.translateToLocalFormatted(str, objects);
	}

}
