package p455w0rdslib.client.gui.element;

import java.util.List;

import com.google.common.collect.Lists;

import p455w0rdslib.api.gui.IModularGui;

/**
 * @author p455w0rd
 *
 */
public class GuiButtonMultiState extends GuiButton {

	List<String> labels = Lists.newArrayList();
	int index = 0;

	public GuiButtonMultiState(IModularGui gui, GuiPos posIn, int height, List<String> labelsIn) {
		super(gui, posIn, height, labelsIn.size() > 0 ? labelsIn.get(0) : "");
		labels = labelsIn;
	}

	public GuiButtonMultiState(IModularGui gui, GuiPos posIn, int width, int height, List<String> labelsIn) {
		super(gui, posIn, width, height, labelsIn.size() > 0 ? labelsIn.get(0) : "");
		labels = labelsIn;
	}

	@Override
	public boolean onClick(int x, int y) {
		if (x >= getX() && y >= getY() && x < getX() + getWidth() && y < getY() + getHeight()) {
			cycleState();
			return true;
		}
		return false;
	}

	public int getState() {
		return index;
	}

	public GuiButtonMultiState setState(int newState) {
		index = newState;
		setLabel(labels.get(index));
		return this;
	}

	public GuiButtonMultiState cycleState() {
		index++;
		if (index >= labels.size()) {
			index = 0;
		}
		setState(index);
		return this;
	}

}
