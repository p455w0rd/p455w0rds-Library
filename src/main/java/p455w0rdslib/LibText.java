package p455w0rdslib;

import net.minecraft.util.text.TextFormatting;

/**
 * @author p455w0rd
 *
 */
public class LibText {

	public static final String RADIO_BARS = TextFormatting.OBFUSCATED + "|||||||" + TextFormatting.RESET;
	public static final String LINE_SEP = "==========================";
	public static final String BLACK = TextFormatting.BLACK.toString();
	public static final String D_BLUE = TextFormatting.DARK_BLUE.toString();
	public static final String D_GREEN = TextFormatting.DARK_GREEN.toString();
	public static final String D_AQUA = TextFormatting.DARK_AQUA.toString();
	public static final String D_RED = TextFormatting.DARK_RED.toString();
	public static final String D_PURPLE = TextFormatting.DARK_PURPLE.toString();
	public static final String GOLD = TextFormatting.GOLD.toString();
	public static final String GRAY = TextFormatting.GRAY.toString();
	public static final String D_GRAY = TextFormatting.DARK_GRAY.toString();
	public static final String BLUE = TextFormatting.BLUE.toString();
	public static final String GREEN = TextFormatting.GREEN.toString();
	public static final String AQUA = TextFormatting.AQUA.toString();
	public static final String RED = TextFormatting.RED.toString();
	public static final String L_PURPLE = TextFormatting.LIGHT_PURPLE.toString();
	public static final String YELLOW = TextFormatting.YELLOW.toString();
	public static final String WHITE = TextFormatting.WHITE.toString();
	public static final String RESET = TextFormatting.RESET.toString();
	public static final String OBF = TextFormatting.OBFUSCATED.toString();
	public static final String BOLD = TextFormatting.BOLD.toString();
	public static final String UNDER = TextFormatting.UNDERLINE.toString();
	public static final String STRIKE = TextFormatting.STRIKETHROUGH.toString();
	public static final String ITALIC = TextFormatting.ITALIC.toString();

	public static String wrapRadioBars(String text) {
		return GOLD + RADIO_BARS + WHITE + BOLD + text + RESET + GOLD + RADIO_BARS;
	}

	public static String lineSep(String color) {
		return color + LINE_SEP + RESET;
	}
}
