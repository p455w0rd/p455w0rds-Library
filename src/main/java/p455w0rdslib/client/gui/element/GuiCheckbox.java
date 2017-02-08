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

	private boolean isChecked, isHovered;
	private int boxWidth = 11;
	private String displayedText;

	public GuiCheckbox(IModularGui gui, GuiPos pos, String text, boolean checked, int width) {
		this(gui, pos, text, checked, width, false);
	}

	public GuiCheckbox(IModularGui gui, GuiPos pos, String text, boolean checked, int width, boolean checkBoxOnRight) {
		super(gui, pos, width, 11);
		isChecked = checked;
		displayedText = text;
		//height = 11;
	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float partialTicks) {
		if (isVisible()) {
			isHovered = mouseX >= getX() && mouseY >= getY() && mouseX < getX() + boxWidth && mouseY < getY() + getHeight();
			GuiUtils.drawContinuousTexturedBox((Gui) getGui(), VANILLA_BUTTON_TEXTURES, getX() + (getWidth() - boxWidth), getY(), 0, 46, boxWidth, getHeight(), 200, 20, 2, 3, 2, 2);
		}
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		int color = 14737632;

		if (!isEnabled()) {
			color = 10526880;
		}
		else {
			color = 4210752;
		}
		if (isChecked()) {
			GuiUtils.drawCenteredString("✔", getX() + (getWidth() - boxWidth) + boxWidth / 2 + 1, getY() + 1, 0x00FF00);
		}

		GuiUtils.drawStringNoShadow(displayedText, getX(), getY() + 2, color);
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void drawStringNoShadow(FontRenderer fontRenderer, String text, int x, int y, int color) {
		fontRenderer.drawString(text, x, y, color);
	}

	@Override
	public boolean onClick(int x, int y) {
		if (x >= getX() && y >= getY() && x < getX() + getWidth() && y < getY() + getHeight()) {
			setChecked(!isChecked());
			return true;
		}
		return false;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

}
