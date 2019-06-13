package p455w0rdslib;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import net.minecraftforge.common.MinecraftForge;
import p455w0rdslib.api.event.BlockGenEvent;

/**
 * @author p455w0rd
 *
 */
public class BlockGenDetector extends MapGenBase {

	final MapGenBase original;

	public BlockGenDetector(final MapGenBase original) {
		this.original = original;
	}

	@Override
	public void generate(final World world, final int x, final int z, final ChunkPrimer primer) {
		final int i = range;
		for (int l = x - i; l <= x + i; ++l) {
			for (int i1 = z - i; i1 <= z + i; ++i1) {
				for (int y = 0; y <= 255; y++) {
					final BlockPos p = new BlockPos(l, y, i1);
					MinecraftForge.TERRAIN_GEN_BUS.post(new BlockGenEvent(world, p, world.getBlockState(p)));
				}
			}
		}
		original.generate(world, x, z, primer);
	}

}
