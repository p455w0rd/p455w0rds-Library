package p455w0rdslib.api.client.shader;

import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.math.BlockPos;

/**
 * @author p455w0rd
 *
 */
public class Cam extends Frustum {

	public Cam(final BlockPos pos) {
		this(pos.getX(), pos.getY(), pos.getZ());
	}

	public Cam(final double x, final double y, final double z) {
		super(ClippingHelperImpl.getInstance());
		setPosition(x, y, z);
	}

}
