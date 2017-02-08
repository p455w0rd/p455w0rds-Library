package p455w0rdslib.api.gui;

/**
 * @author p455w0rd
 *
 */
public interface IModularGui {

	boolean isMouseOver(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY);

	int getX();

	int getY();

	int getWidth();

	int getHeight();

}
