package p455w0rdslib.client.gui.element;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import p455w0rdslib.api.gui.IModularGui;
import p455w0rdslib.util.GuiUtils;

/**
 * @author p455w0rd
 *
 */
public class GuiCheckbox extends GuiElement {

	private boolean isChecked;//, isHovered;
	private final int boxWidth = 11;
	private final String displayedText;

	public GuiCheckbox(final IModularGui gui, final GuiPos pos, final String text, final boolean checked, final int width) {
		this(gui, pos, text, checked, width, false);
	}

	public GuiCheckbox(final IModularGui gui, final GuiPos pos, final String text, final boolean checked, final int width, final boolean checkBoxOnRight) {
		super(gui, pos, width, 11);
		isChecked = checked;
		displayedText = text;
	}

	@Override
	public void drawBackground(final int mouseX, final int mouseY, final float partialTicks) {
		if (isVisible()) {
			//isHovered = mouseX >= getX() && mouseY >= getY() && mouseX < getX() + boxWidth && mouseY < getY() + getHeight();
			GuiUtils.drawContinuousTexturedBox((Gui) getGui(), VANILLA_BUTTON_TEXTURES, getX() + getWidth() - boxWidth, getY(), 0, 46, boxWidth, getHeight(), 200, 20, 2, 3, 2, 2);
		}
	}

	@Override
	public void drawForeground(final int mouseX, final int mouseY) {
		int color = 14737632;

		if (!isEnabled()) {
			color = 10526880;
		}
		else {
			color = 4210752;
		}
		if (isChecked()) {
			GuiUtils.drawCenteredString("✔", getX() + getWidth() - boxWidth + boxWidth / 2 + 1, getY() + 1, 0x00FF00);
		}

		GuiUtils.drawStringNoShadow(displayedText, getX(), getY() + 2, color);
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void drawStringNoShadow(final FontRenderer fontRenderer, final String text, final int x, final int y, final int color) {
		fontRenderer.drawString(text, x, y, color);
	}

	@Override
	public boolean onClick(final int x, final int y) {
		if (x >= getX() && y >= getY() && x < getX() + getWidth() && y < getY() + getHeight()) {
			setChecked(!isChecked());
			return true;
		}
		return false;
	}

	public void setChecked(final boolean isChecked) {
		this.isChecked = isChecked;
	}

}
