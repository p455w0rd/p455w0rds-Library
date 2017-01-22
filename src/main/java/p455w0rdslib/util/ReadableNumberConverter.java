package p455w0rdslib.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;

/**
 * @author p455w0rd
 *
 */
public enum ReadableNumberConverter {

	INSTANCE;

	/**
	 * String representation of the sorted suffixes K = thousand M = Million B =
	 * Billion T = Trillion Qa = Quadrillion Qi = Quintillion
	 */
	private static final String[] ENCODED_SUFFIXES = {
			"K", "M", "B", "T", "Qa", "Qi"
	};

	private final Format format;

	/**
	 * Initializes the specific decimal format with special format for negative
	 * and positive numbers
	 */
	ReadableNumberConverter() {
		final DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		final DecimalFormat format = new DecimalFormat(".#;0.#");
		format.setDecimalFormatSymbols(symbols);
		format.setRoundingMode(RoundingMode.DOWN);

		this.format = format;
	}

	public String toSlimReadableForm(final long number) {
		return toReadableFormRestrictedByWidth(number, 3);
	}

	/**
	 * restricts a string representation of a number to a specific width
	 *
	 * @param number
	 *            to be formatted number
	 * @param width
	 *            width limitation of the resulting number
	 *
	 * @return formatted number restricted by the width limitation
	 */
	private String toReadableFormRestrictedByWidth(final long number, final int width) {
		assert number >= 0;

		// handles low numbers more efficiently since no format is needed
		final String numberString = Long.toString(number);
		int numberSize = numberString.length();
		if (numberSize <= width) {
			return numberString;
		}

		long base = number;
		double last = base * 1000;
		int exponent = -1;
		String postFix = "";

		while (numberSize > width) {
			last = base;
			base /= 1000;

			exponent++;

			// adds +1 due to the postfix
			numberSize = Long.toString(base).length() + 1;
			postFix = ENCODED_SUFFIXES[exponent];
		}

		final String withPrecision = format.format(last / 1000) + postFix;
		final String withoutPrecision = Long.toString(base) + postFix;

		final String slimResult = (withPrecision.length() <= width) ? withPrecision : withoutPrecision;

		// post condition
		assert slimResult.length() <= width;

		return slimResult;
	}

	public String toWideReadableForm(final long number) {
		return toReadableFormRestrictedByWidth(number, 4);
	}

}
