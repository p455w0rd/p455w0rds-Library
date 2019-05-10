package p455w0rdslib.api.client;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;

/**
 * @author p455w0rd
 *
 */
public interface ICustomItemRenderer {

	TransformType getTransformType();

	void setTransformType(TransformType type);

}
