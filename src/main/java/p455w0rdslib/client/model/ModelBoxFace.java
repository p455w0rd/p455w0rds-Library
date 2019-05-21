package p455w0rdslib.client.model;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;

/**
 * Created by brandon3055 on 31/10/2016.
 */
public class ModelBoxFace extends ModelBox {

	public final int face;

	public ModelBoxFace(final ModelRenderer renderer, final int textureOffsetX, final int textureOffsetY, final float offX, final float offY, final float offZ, final int width, final int height, final int depth, final int face) {
		super(renderer, textureOffsetX, textureOffsetY, offX, offY, offZ, width, height, depth, 0);
		this.face = face;
	}

	@Override
	public void render(final BufferBuilder renderer, final float scale) {
		quadList[face].draw(renderer, scale);
	}
}