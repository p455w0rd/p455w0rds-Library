package p455w0rdslib.api.gui;

import java.util.List;

import p455w0rdslib.client.gui.element.GuiPos;

/**
 * @author p455w0rd
 *
 */
public interface IGuiElement {

	int getX();

	IGuiElement setX(int posX);

	int getY();

	IGuiElement setY(int posY);

	int getWidth();

	IGuiElement setWidth(int width);

	int getHeight();

	IGuiElement setHeight(int height);

	IGuiElement setSize(int width, int height);

	void drawBackground(int mouseX, int mouseY, float gameTicks);

	void drawForeground(int mouseX, int mouseY);

	boolean onClick(int mouseX, int mouseY);

	boolean onRightClick(int mouseX, int mouseY);

	boolean onMiddleClick(int mouseX, int mouseY);

	boolean onMousePressed(int mouseX, int mouseY, int button);

	void onMouseReleased(int mouseX, int mouseY, int button);

	boolean onMouseWheel(int mouseX, int mouseY, int movement);

	IModularGui getGui();

	IGuiElement setGui(IModularGui guiIn);

	boolean isVisible();

	IGuiElement setVisible(boolean visibility);

	boolean isEnabled();

	IGuiElement setEnabled(boolean doEnable);

	IGuiElement enable();

	IGuiElement disable();

	GuiPos getPos();

	IGuiElement setPos(int xPos, int yPos);

	void update(int mouseX, int mouseY);

	boolean isMouseOver(int mouseX, int mouseY);

	List<String> getTooltip();

	IGuiElement setTooltip(List<String> tooltipLines);

	boolean hasTooltip();

}
