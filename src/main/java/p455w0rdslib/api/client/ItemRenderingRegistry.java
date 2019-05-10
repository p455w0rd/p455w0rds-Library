package p455w0rdslib.api.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author p455w0rd
 *
 */
@SideOnly(Side.CLIENT)
public class ItemRenderingRegistry {

	private static List<IModelHolder> ITEM_RENDERING_REGISTRY = new ArrayList<>();

	public static List<IModelHolder> getRegisteredItems() {
		return ITEM_RENDERING_REGISTRY;
	}

	public static void registerCustomRenderingItem(final IModelHolder holder) {
		if (!ITEM_RENDERING_REGISTRY.contains(holder)) {
			ITEM_RENDERING_REGISTRY.add(holder);
		}
	}

	public static final void initModels(final ModelBakeEvent event) {
		for (final IModelHolder holder : ITEM_RENDERING_REGISTRY) {
			if (holder instanceof Item) {
				initModel(event, (Item) holder);
			}
		}
	}

	private static final void initModel(final ModelBakeEvent event, final Item item) {
		final IModelHolder holder = (IModelHolder) item;
		holder.initModel();
		if (holder.shouldUseInternalTEISR() && holder.getRenderer() != null) {
			final IBakedModel currentModel = event.getModelRegistry().getObject(new ModelResourceLocation(item.getRegistryName(), "inventory"));
			holder.setWrappedModel(new ItemLayerWrapper(currentModel).setRenderer(holder.getRenderer()));
			event.getModelRegistry().putObject(new ModelResourceLocation(item.getRegistryName(), "inventory"), holder.getWrappedModel());
		}
	}

	public static final void registerTEISRs(final ModelRegistryEvent event) {
		for (final IModelHolder holder : ITEM_RENDERING_REGISTRY) {
			if (holder.shouldUseInternalTEISR() && holder.getRenderer() != null) {
				((Item) holder).setTileEntityItemStackRenderer((TileEntityItemStackRenderer) holder.getRenderer());
			}
		}
	}

}
