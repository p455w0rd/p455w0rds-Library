package p455w0rdslib.util;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author p455w0rd
 *
 */
public class EntityUtils {

	public static List<Entity> getEntitiesInRange(Class<? extends Entity> entityType, World world, double x, double y, double z, double radius) {
		return world.getEntitiesWithinAABB(entityType, new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius));
	}

	public static IBlockState getBlockStateBelowEntity(Entity entity, int depth) {
		int blockX = MathUtils.floor(entity.posX);
		int blockY = MathUtils.floor(entity.getEntityBoundingBox().minY - depth);
		int blockZ = MathUtils.floor(entity.posZ);
		return entity.worldObj.getBlockState(new BlockPos(blockX, blockY, blockZ));
	}

	public static Block getBlockBelowEntity(Entity entity, int depth) {
		return getBlockStateBelowEntity(entity, depth).getBlock();
	}

	public static IBlockState getBlockStateAboveEntity(Entity entity, int depth) {
		int blockX = MathUtils.floor(entity.posX);
		int blockY = MathUtils.floor(entity.getEntityBoundingBox().maxY + depth);
		int blockZ = MathUtils.floor(entity.posZ);
		return entity.worldObj.getBlockState(new BlockPos(blockX, blockY, blockZ));
	}

	public static Block getBlockAboveEntity(Entity entity, int depth) {
		return getBlockStateAboveEntity(entity, depth).getBlock();
	}

	public static boolean isBlockAboveEntity(Entity entity, Block block, int depth) {
		World world = entity.getEntityWorld();
		int blockX = MathUtils.ceil(entity.posX - 1);
		int blockY = MathUtils.ceil(entity.posY + depth);
		int blockZ = MathUtils.ceil(entity.posZ - 1);
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (world.getBlockState(new BlockPos(blockX + i + j, blockY, blockZ + i + j)).getBlock().equals(block)) {
					return true;
				}
			}
		}
		return false;
	}

	public static BlockPos getPosBelowEntity(Entity entity, int depth) {
		int blockX = MathUtils.floor(entity.posX);
		int blockY = MathUtils.floor(entity.getEntityBoundingBox().minY - depth);
		int blockZ = MathUtils.floor(entity.posZ);
		return new BlockPos(blockX, blockY, blockZ);
	}

}
