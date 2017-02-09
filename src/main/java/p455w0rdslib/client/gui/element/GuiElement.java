package p455w0rdslib.client.gui.element;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;
import p455w0rdslib.api.gui.IGuiElement;
import p455w0rdslib.api.gui.IModularGui;

/**
 * Base class for all GUI elements
 *
 * @author p455w0rd
 *
 */
public abstract class GuiElement implements IGuiElement {

	protected static final ResourceLocation VANILLA_BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");

	private IModularGui gui;
	private GuiPos pos;
	private int w, h;
	private boolean enabled = true, visible = true;
	private List<String> tooltip = Lists.newArrayList();

	public GuiElement(IModularGui gui, GuiPos posIn) {
		this.gui = gui;
		pos = posIn;
	}

	public GuiElement(IModularGui gui, GuiPos posIn, int width, int height) {
		this.gui = gui;
		pos = posIn;
		w = width;
		h = height;
	}

	@Override
	public abstract void drawBackground(int mouseX, int mouseY, float gameTicks);

	@Override
	public abstract void drawForeground(int mouseX, int mouseY);

	@Override
	public boolean onClick(int mouseX, int mouseY) {
		return false;
	}

	@Override
	public boolean onRightClick(int mouseX, int mouseY) {
		return false;
	}

	@Override
	public boolean onMiddleClick(int mouseX, int mouseY) {
		return false;
	}

	@Override
	public boolean onMousePressed(int mouseX, int mouseY, int button) {
		switch (button) {
		default:
		case 0:
			return onClick(mouseX, mouseY);
		case 1:
			return onRightClick(mouseX, mouseY);
		case 2:
			return onMiddleClick(mouseX, mouseY);
		}
	}

	@Override
	public void onMouseReleased(int mouseX, int mouseY, int button) {
		return;
	}

	@Override
	public IModularGui getGui() {
		return gui;
	}

	@Override
	public IGuiElement setGui(IModularGui guiIn) {
		gui = guiIn;
		return this;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public IGuiElement setVisible(boolean visibility) {
		visible = visibility;
		return this;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public IGuiElement setEnabled(boolean doEnable) {
		enabled = doEnable;
		return this;
	}

	@Override
	public IGuiElement enable() {
		return setEnabled(true);
	}

	@Override
	public IGuiElement disable() {
		return setEnabled(false);
	}

	@Override
	public GuiPos getPos() {
		return pos;
	}

	@Override
	public IGuiElement setPos(int xPos, int yPos) {
		pos = new GuiPos(xPos, yPos);
		return this;
	}

	@Override
	public int getX() {
		return pos.getX();
	}

	@Override
	public IGuiElement setX(int posX) {
		setPos(posX, getY());
		return this;
	}

	@Override
	public int getY() {
		return pos.getY();
	}

	@Override
	public IGuiElement setY(int posY) {
		setPos(getX(), posY);
		return this;
	}

	@Override
	public int getWidth() {
		return w;
	}

	@Override
	public IGuiElement setWidth(int width) {
		w = width;
		return this;
	}

	@Override
	public int getHeight() {
		return h;
	}

	@Override
	public IGuiElement setHeight(int height) {
		h = height;
		return this;
	}

	@Override
	public IGuiElement setSize(int width, int height) {
		w = width;
		h = height;
		return this;
	}

	@Override
	public void update(int mouseX, int mouseY) {
	}

	@Override
	public boolean isMouseOver(int mouseX, int mouseY) {
		return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
	}

	@Override
	public boolean onMouseWheel(int mouseX, int mouseY, int movement) {
		return false;
	}

	@Override
	public List<String> getTooltip() {
		return tooltip;
	}

	@Override
	public IGuiElement setTooltip(List<String> tooltipLines) {
		tooltip = tooltipLines;
		return this;
	}

	@Override
	public boolean hasTooltip() {
		return getTooltip().size() > 0;
	}

}
