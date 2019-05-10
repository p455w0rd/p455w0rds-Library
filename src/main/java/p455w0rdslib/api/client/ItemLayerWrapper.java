package p455w0rdslib.api.client;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

/**
 * @author p455w0rd
 *
 */
public class ItemLayerWrapper implements IBakedModel {

	private IBakedModel internal;
	private ICustomItemRenderer renderer;

	public ItemLayerWrapper(final IBakedModel internal) {
		this.internal = internal;
	}

	public ItemLayerWrapper setRenderer(final ICustomItemRenderer renderer) {
		this.renderer = renderer;
		return this;
	}

	public ICustomItemRenderer getRenderer() {
		return renderer;
	}

	@Override
	public List<BakedQuad> getQuads(final IBlockState state, final EnumFacing side, final long rand) {
		return internal.getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return internal.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return internal.isGui3d();
	}

	public IBakedModel getInternal() {
		return internal;
	}

	public void setInternal(final IBakedModel model) {
		internal = model;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return true;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return internal.getParticleTexture();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(final TransformType type) {
		if (getRenderer() != null) {
			getRenderer().setTransformType(type);
		}
		return Pair.of(this, internal.handlePerspective(type).getRight());
	}

}