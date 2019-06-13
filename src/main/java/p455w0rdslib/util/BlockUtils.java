package p455w0rdslib.util;

import java.util.*;

import org.apache.logging.log4j.LogManager;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import p455w0rdslib.LibGlobals;

/**
 * @author p455w0rd
 *
 */
public class BlockUtils {

	public static IBlockState getAdjacentBlock(final World world, BlockPos pos, final EnumFacing facing) {
		pos = pos.offset(facing);
		return world == null || !world.isBlockLoaded(pos) ? Blocks.AIR.getDefaultState() : world.getBlockState(pos);
	}

	public static IBlockState getAdjacentBlock(final World world, final BlockPos pos, final int side) {
		return world == null ? Blocks.AIR.getDefaultState() : getAdjacentBlock(world, pos, EnumFacing.VALUES[side]);
	}

	public static List<IBlockState> getAdjacentBlocks(final World world, final BlockPos pos) {
		final List<IBlockState> blockList = Lists.newArrayList();
		for (final EnumFacing facing : EnumFacing.VALUES) {
			blockList.add(getAdjacentBlock(world, pos, facing));
		}
		return blockList;
	}

	public static List<IBlockState> getAdjacentHorizontalBlocks(final World world, final BlockPos pos) {
		final List<IBlockState> blockList = Lists.newArrayList();
		for (final EnumFacing facing : EnumFacing.HORIZONTALS) {
			blockList.add(world == null ? Blocks.AIR.getDefaultState() : getAdjacentBlock(world, pos, facing));
		}
		return blockList;
	}

	/**
	 * Resolves blockstate via name=>value pairs of strings. Useful for JSON<br>
	 * From https://github.com/MightyPirates/BedrockOres/blob/master-MC1.12/src/main/java/li/cil/bedrockores/common/config/ore/WrappedBlockState.java#L112
	 */
	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	public static IBlockState resolveBlockState(final ResourceLocation name, final Map<String, String> properties) {
		final Block block = ForgeRegistries.BLOCKS.getValue(name);
		if (block == null || block == Blocks.AIR) {
			return Blocks.AIR.getDefaultState();
		}
		IBlockState state = block.getDefaultState();
		if (properties != null) {
			final Collection<IProperty<?>> blockProperties = state.getPropertyKeys();
			outer: for (final Map.Entry<String, String> entry : properties.entrySet()) {
				final String serializedKey = entry.getKey();
				final String serializedValue = entry.getValue();
				for (final IProperty property : blockProperties) {
					if (Objects.equals(property.getName(), serializedKey)) {
						final Comparable originalValue = state.getValue(property);
						do {
							if (Objects.equals(property.getName(state.getValue(property)), serializedValue)) {
								continue outer;
							}
							state = state.cycleProperty(property);
						}
						while (!Objects.equals(state.getValue(property), originalValue));
						LogManager.getLogger(LibGlobals.MODID).warn("Cannot parse property value '{}' for property '{}' of block {}.", serializedValue, serializedKey, name);
						return Blocks.AIR.getDefaultState();
					}
				}
				LogManager.getLogger(LibGlobals.MODID).warn("Block {} has no property '{}'.", name, serializedKey);
				return Blocks.AIR.getDefaultState();
			}
		}
		return state;
	}

}
