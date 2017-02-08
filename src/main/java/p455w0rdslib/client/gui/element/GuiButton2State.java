package p455w0rdslib.client.gui.element;

import p455w0rdslib.api.gui.IModularGui;

/**
 * @author p455w0rd
 *
 */
public class GuiButton2State extends GuiButton {

	String text1 = "", text2 = "";
	boolean state = true;

	public GuiButton2State(IModularGui gui, GuiPos posIn, int height, String label, String label2) {
		super(gui, posIn, height, label);
		text1 = label;
		text2 = label2;
	}

	public GuiButton2State(IModularGui gui, GuiPos posIn, int width, int height, String label, String label2) {
		super(gui, posIn, width, height, label);
		text1 = label;
		text2 = label2;
	}

	@Override
	public boolean onClick(int x, int y) {
		if (x >= getX() && y >= getY() && x < getX() + getWidth() && y < getY() + getHeight()) {
			cycleState();
			return true;
		}
		return false;
	}

	public boolean getState() {
		return state;
	}

	public GuiButton2State setState(boolean newState) {
		state = newState;
		setLabel(state ? text1 : text2);
		return this;
	}

	public GuiButton2State cycleState() {
		setState(!state);
		return this;
	}

}
