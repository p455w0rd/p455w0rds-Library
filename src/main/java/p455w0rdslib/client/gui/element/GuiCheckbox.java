package p455w0rdslib.client.gui.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.client.config.GuiUtils;

/**
 * @author p455w0rd
 *
 */
public class GuiCheckbox extends GuiButton {

	private boolean isChecked;
	private int boxWidth;

	public GuiCheckbox(int id, int xPos, int yPos, String displayString, boolean isChecked, int width) {
		super(id, xPos, yPos, displayString);
		this.isChecked = isChecked;
		boxWidth = 11;
		height = 11;
		this.width = width;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (visible) {
			hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + boxWidth && mouseY < yPosition + height;
			GuiUtils.drawContinuousTexturedBox(BUTTON_TEXTURES, xPosition + (width - boxWidth), yPosition, 0, 46, boxWidth, height, 200, 20, 2, 3, 2, 2, zLevel);
			mouseDragged(mc, mouseX, mouseY);
			int color = 14737632;

			if (!enabled) {
				color = 10526880;
			}
			else {
				color = 4210752;
			}
			if (isChecked) {
				drawCenteredString(mc.fontRendererObj, "✔", xPosition + (width - boxWidth) + boxWidth / 2 + 1, yPosition + 1, 0x00FF00);
			}

			drawStringNoShadow(mc.fontRendererObj, displayString, xPosition, yPosition + 2, color);
		}
	}

	public void drawStringNoShadow(FontRenderer fontRenderer, String text, int x, int y, int color) {
		fontRenderer.drawString(text, x, y, color);
	}

	@Override
	public boolean mousePressed(Minecraft mc, int x, int y) {
		if (enabled && visible && x >= xPosition && y >= yPosition && x < xPosition + width && y < yPosition + height) {
			isChecked = !isChecked;
			return true;
		}

		return false;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
}
