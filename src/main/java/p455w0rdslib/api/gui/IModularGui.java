package p455w0rdslib.api.gui;

import java.util.List;

import net.minecraft.util.ResourceLocation;

/**
 * @author p455w0rd
 *
 */
public interface IModularGui {

	IModularGui addElement(IGuiElement element);

	boolean isMouseOverElement(IGuiElement element, int mouseX, int mouseY);

	void drawElements(float partialTick, int mouseX, int mouseY, boolean foreground);

	List<IGuiElement> getElements();

	void updateElements(int mouseX, int mouseY);

	String getTitle();

	IModularGui setTitle(String titleText);

	IModularGui setTitleScale(float scale);

	IModularGui setTitleOffsetX(int offset);

	IModularGui setTitleOffsetY(int offset);

	IModularGui setTitlePos(int x, int y);

	int getTitleColor();

	IModularGui setTitleColor(int color);

	ResourceLocation getBackgroundTexture();

	IModularGui setBackgroundTexture(ResourceLocation location);

	boolean isMouseOver(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY);

	int getX();

	int getY();

	int getWidth();

	IModularGui setWidth(int w);

	int getHeight();

	IModularGui setHeight(int h);

	IModularGui setSize(int w, int h);

	IModularGui setTooltipColor(int color, boolean top);

	int[] getTooltipColors();

	boolean onMouseWheel(int mouseX, int mouseY, int wheelMovement);

}
