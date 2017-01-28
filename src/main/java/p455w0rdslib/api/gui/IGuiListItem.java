package p455w0rdslib.api.gui;

/**
 * @author p455w0rd
 *
 */
public interface IGuiListItem {

	String getDisplayText();

	int getHeight();

	int getWidth();

	void draw(int x, int y, int backColor, int textColor);

}
