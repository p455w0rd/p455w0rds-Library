package p455w0rdslib.client.gui.element;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import p455w0rdslib.util.GuiUtils;

/**
 * Base class for all GUI elements
 *
 * @author p455w0rd
 *
 */
public abstract class GuiElement {

	protected static final ResourceLocation VANILLA_BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");

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

	public abstract void drawBackground(int mouseX, int mouseY, float gameTicks);

	public abstract void drawForeground(int mouseX, int mouseY);

	public boolean onClick(int mouseX, int mouseY) {
		return false;
	}

	public boolean onRightClick(int mouseX, int mouseY) {
		return false;
	}

	public boolean onMiddleClick(int mouseX, int mouseY) {
		return false;
	}

	public boolean onMouseWheel(int mouseX, int mouseY, int movement) {
		return false;
	}

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

	public void drawModalRect(int x, int y, int width, int height, int color) {
		GuiUtils.drawGradientRect(gui, x, y, width, height, color, color);
	}

	public void drawStencil(int xStart, int yStart, int xEnd, int yEnd, int flag) {

		GlStateManager.disableTexture2D();
		GL11.glStencilFunc(GL11.GL_ALWAYS, flag, flag);
		GL11.glStencilOp(GL11.GL_ZERO, GL11.GL_ZERO, GL11.GL_REPLACE);
		GL11.glStencilMask(flag);
		GlStateManager.colorMask(false, false, false, false);
		GlStateManager.depthMask(false);
		GL11.glClearStencil(0);
		GlStateManager.clear(GL11.GL_STENCIL_BUFFER_BIT);

		VertexBuffer buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		buffer.pos(xStart, yEnd, 0).endVertex();
		buffer.pos(xEnd, yEnd, 0).endVertex();
		buffer.pos(xEnd, yStart, 0).endVertex();
		buffer.pos(xStart, yStart, 0).endVertex();
		Tessellator.getInstance().draw();

		GlStateManager.enableTexture2D();
		GL11.glStencilFunc(GL11.GL_EQUAL, flag, flag);
		GL11.glStencilMask(0);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.depthMask(true);
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

	public void update(int mouseX, int mouseY) {
		update();
	}

	public void update() {

	}
}
