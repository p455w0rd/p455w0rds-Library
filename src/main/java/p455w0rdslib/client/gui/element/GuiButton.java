package p455w0rdslib.client.gui.element;

import net.minecraft.client.gui.inventory.GuiContainer;
import p455w0rdslib.api.gui.IModularGui;
import p455w0rdslib.util.MCPrivateUtils;
import p455w0rdslib.util.RenderUtils;

/**
 * @author p455w0rd
 *
 */
public class GuiButton extends GuiElement {

	int borderColor = 0xFF000000, backgroundColor = 0xFFCCCCCC, textColor = 0xFFFFFFFF;
	String text = "";

	public GuiButton(IModularGui gui, GuiPos posIn, int height, String label) {
		this(gui, posIn, MCPrivateUtils.getGuiContainerXSize((GuiContainer) gui) - (posIn.getX() * 2), height, label);
	}

	public GuiButton(IModularGui gui, GuiPos posIn, int width, int height, String label) {
		super(gui, posIn, width, height);
		text = label;
	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks) {
		drawModalRect(getX(), getY(), getX() + getWidth() + 1, getY() + getHeight() + 1, borderColor);
		drawModalRect(getX() + 1, getY() + 1, getX() + getWidth(), getY() + getHeight(), backgroundColor);
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		drawCenteredString(text);
	}

	public int getBGColor() {
		return backgroundColor;
	}

	public GuiButton setBGColor(int color) {
		backgroundColor = color;
		return this;
	}

	public int getBorderColor() {
		return borderColor;
	}

	public GuiButton setBorderColor(int color) {
		borderColor = color;
		return this;
	}

	public int getTextColor() {
		return textColor;
	}

	public GuiButton setTextColor(int color) {
		textColor = color;
		return this;
	}

	public void drawCenteredString(String text) {
		int x1 = (getWidth() / 2) - (RenderUtils.getFontRenderer().getStringWidth(text) / 2);
		RenderUtils.getFontRenderer().drawStringWithShadow(text, getX() + x1, getY() + 3, textColor);
	}

	public String getLabel() {
		return text;
	}

	public GuiButton setLabel(String label) {
		text = label;
		return this;
	}

}
