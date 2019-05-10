package p455w0rdslib.api.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

/**
 * @author p455w0rd
 *
 */
public interface IModelHolder {

	// Where you would normally call ModelLoader#setCustomModelResourceLocation
	void initModel();

	//used for TEISR rendering
	default ItemLayerWrapper getWrappedModel() {
		return null;
	}

	//used for TEISR rendering
	default void setWrappedModel(final ItemLayerWrapper wrappedModel) {
	}

	//used for TEISR rendering
	default boolean shouldUseInternalTEISR() {
		return false;
	}

	//used for TEISR rendering (in situations where the item model uses a different registry name than the item)
	default ModelResourceLocation getModelResource(final Item item) {
		return new ModelResourceLocation(item.getRegistryName(), "inventory");
	}

	default ICustomItemRenderer getRenderer() {
		return null;
	}

}
