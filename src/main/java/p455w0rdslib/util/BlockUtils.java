package p455w0rdslib.util;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author p455w0rd
 *
 */
public class BlockUtils {

	public static IBlockState getAdjacentBlock(World world, BlockPos pos, EnumFacing facing) {
		pos = pos.offset(facing);
		return world == null || !world.isBlockLoaded(pos) ? Blocks.AIR.getDefaultState() : world.getBlockState(pos);
	}

	public static IBlockState getAdjacentBlock(World world, BlockPos pos, int side) {
		return world == null ? Blocks.AIR.getDefaultState() : getAdjacentBlock(world, pos, EnumFacing.VALUES[side]);
	}

	public static List<IBlockState> getAdjacentBlocks(World world, BlockPos pos) {
		List<IBlockState> blockList = Lists.newArrayList();
		for (EnumFacing facing : EnumFacing.VALUES) {
			blockList.add(getAdjacentBlock(world, pos, facing));
		}
		return blockList;
	}

	public static List<IBlockState> getAdjacentHorizontalBlocks(World world, BlockPos pos) {
		List<IBlockState> blockList = Lists.newArrayList();
		for (EnumFacing facing : EnumFacing.HORIZONTALS) {
			blockList.add(world == null ? Blocks.AIR.getDefaultState() : getAdjacentBlock(world, pos, facing));
		}
		return blockList;
	}

}
