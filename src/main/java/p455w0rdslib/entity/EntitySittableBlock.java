package p455w0rdslib.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import p455w0rdslib.util.EasyMappings;

/**
 * @author p455w0rd
 *
 */
public class EntitySittableBlock extends Entity {

	public int blockPosX;
	public int blockPosY;
	public int blockPosZ;

	public EntitySittableBlock(World world) {
		super(world);
		noClip = true;
		height = 0.01F;
		width = 0.01F;
	}

	public EntitySittableBlock(World world, double x, double y, double z, double y0ffset) {
		this(world);
		blockPosX = (int) x;
		blockPosY = (int) y;
		blockPosZ = (int) z;
		setPosition(x + 0.5D, y + y0ffset, z + 0.5D);
	}

	public EntitySittableBlock(World world, double x, double y, double z, double y0ffset, int rotation, double rotationOffset) {
		this(world);
		blockPosX = (int) x;
		blockPosY = (int) y;
		blockPosZ = (int) z;
		setPostionConsideringRotation(x + 0.5D, y + y0ffset, z + 0.5D, rotation, rotationOffset);
	}

	public void setPostionConsideringRotation(double x, double y, double z, int rotation, double rotationOffset) {
		switch (rotation) {
		case 2:
			z += rotationOffset;
			break;
		case 0:
			z -= rotationOffset;
			break;
		case 3:
			x -= rotationOffset;
			break;
		case 1:
			x += rotationOffset;
			break;
		}
		setPosition(x, y, z);
	}

	@Override
	public double getMountedYOffset() {
		return height * 0.0D;
	}

	@Override
	protected boolean shouldSetPosAfterLoading() {
		return false;
	}

	@Override
	public void onEntityUpdate() {
		if (!EasyMappings.world(this).isRemote) {
			if (!isBeingRidden() || EasyMappings.world(this).isAirBlock(new BlockPos(blockPosX, blockPosY, blockPosZ))) {
				setDead();
				EasyMappings.world(this).updateComparatorOutputLevel(getPosition(), EasyMappings.world(this).getBlockState(getPosition()).getBlock());
			}
		}
	}

	@Override
	protected void entityInit() {
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	}

}