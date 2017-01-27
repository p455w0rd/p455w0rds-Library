package p455w0rdslib.client.gui.element;

import net.minecraft.client.gui.Gui;

/**
 * Base class for all GUI elements
 *
 * @author p455w0rd
 *
 */
public abstract class GuiElement {

	private Gui gui;
	private GuiPos pos;
	private int w, h;
	private boolean enabled = true, visible = true;

	public GuiElement(Gui gui, GuiPos posIn) {
		this.gui = gui;
		pos = posIn;
	}

	public GuiElement(Gui gui, GuiPos posIn, int width, int height) {
		this.gui = gui;
		pos = posIn;
		w = width;
		h = height;
	}

	public Gui getGui() {
		return gui;
	}

	public GuiElement setGui(Gui guiIn) {
		gui = guiIn;
		return this;
	}

	public boolean isVisible() {
		return visible;
	}

	public GuiElement setVisible(boolean visibility) {
		visible = visibility;
		return this;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public GuiElement setEnabled(boolean doEnable) {
		enabled = doEnable;
		return this;
	}

	public GuiElement enable() {
		return setEnabled(true);
	}

	public GuiElement disable() {
		return setEnabled(false);
	}

	public GuiPos getPos() {
		return pos;
	}

	public GuiElement setPos(int xPos, int yPos) {
		pos = new GuiPos(xPos, yPos);
		return this;
	}

	public int getX() {
		return pos.getX();
	}

	public GuiElement setX(int posX) {
		setPos(posX, getY());
		return this;
	}

	public int getY() {
		return pos.getY();
	}

	public GuiElement setY(int posY) {
		setPos(getX(), posY);
		return this;
	}

	public int getWidth() {
		return w;
	}

	public GuiElement setWidth(int width) {
		w = width;
		return this;
	}

	public int getHeight() {
		return h;
	}

	public GuiElement setHeight(int height) {
		h = height;
		return this;
	}

	public GuiElement setSize(int width, int height) {
		w = width;
		h = height;
		return this;
	}
}
