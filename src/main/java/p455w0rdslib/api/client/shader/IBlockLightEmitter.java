package p455w0rdslib.api.client.shader;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**This is temporary until I finish implementing capabilities..<b>DO NOT USE<b>*/
@Deprecated
public interface IBlockLightEmitter {

	/*@SideOnly(Side.CLIENT)
	default List<Light> emitLight(final List<Light> lights, final TileEntity tile) {
		return emitLight(lights, tile.getWorld().getBlockState(tile.getPos()), tile.getPos());
	}*/

	@SideOnly(Side.CLIENT)
	default List<Light> emitLight(final List<Light> lights, final IBlockState state, final BlockPos pos) {
		return Collections.emptyList();
	}

}
