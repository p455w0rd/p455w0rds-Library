package p455w0rdslib.api.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author p455w0rd
 *
 */
public class BlockGenEvent extends Event {

	private final World world;
	private final BlockPos pos;
	private final IBlockState state;

	public BlockGenEvent(final World world, final BlockPos pos, final IBlockState state) {
		this.world = world;
		this.pos = pos;
		this.state = state;
	}

	public World getWorld() {
		return world;
	}

	public BlockPos getPos() {
		return pos;
	}

	public IBlockState getState() {
		return state;
	}

}
