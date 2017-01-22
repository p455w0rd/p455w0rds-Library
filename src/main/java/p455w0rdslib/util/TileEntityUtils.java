package p455w0rdslib.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author p455w0rd
 *
 */
public class TileEntityUtils {

	public static void markBlockForUpdate(World world, BlockPos pos) {
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
	}

}
