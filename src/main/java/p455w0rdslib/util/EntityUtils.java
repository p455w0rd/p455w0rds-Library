package p455w0rdslib.util;

import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author p455w0rd
 *
 */
public class EntityUtils {

	public static Entity getRenderViewEntity() {
		return Minecraft.getMinecraft().getRenderViewEntity();
	}

	public static List<Entity> getEntitiesInRange(final Class<? extends Entity> entityType, final World world, final double x, final double y, final double z, final double radius) {
		return world.getEntitiesWithinAABB(entityType, new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius));
	}

	public static IBlockState getBlockStateBelowEntity(final Entity entity, final int depth) {
		final int blockX = MathUtils.floor(entity.posX);
		final int blockY = MathUtils.floor(entity.getEntityBoundingBox().minY - depth);
		final int blockZ = MathUtils.floor(entity.posZ);
		return EasyMappings.world(entity).getBlockState(new BlockPos(blockX, blockY, blockZ));
	}

	public static Block getBlockBelowEntity(final Entity entity, final int depth) {
		return getBlockStateBelowEntity(entity, depth).getBlock();
	}

	public static IBlockState getBlockStateAboveEntity(final Entity entity, final int depth) {
		final int blockX = MathUtils.floor(entity.posX);
		final int blockY = MathUtils.floor(entity.getEntityBoundingBox().maxY + depth);
		final int blockZ = MathUtils.floor(entity.posZ);
		return EasyMappings.world(entity).getBlockState(new BlockPos(blockX, blockY, blockZ));
	}

	public static Block getBlockAboveEntity(final Entity entity, final int depth) {
		return getBlockStateAboveEntity(entity, depth).getBlock();
	}

	public static boolean isBlockAboveEntity(final Entity entity, final Block block, final int depth) {
		final World world = entity.getEntityWorld();
		final int blockX = MathUtils.ceil(entity.posX - 1);
		final int blockY = MathUtils.ceil(entity.posY + depth);
		final int blockZ = MathUtils.ceil(entity.posZ - 1);
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (world.getBlockState(new BlockPos(blockX + i + j, blockY, blockZ + i + j)).getBlock().equals(block)) {
					return true;
				}
			}
		}
		return false;
	}

	public static BlockPos getPosBelowEntity(final Entity entity, final int depth) {
		final int blockX = MathUtils.floor(entity.posX);
		final int blockY = MathUtils.floor(entity.getEntityBoundingBox().minY - depth);
		final int blockZ = MathUtils.floor(entity.posZ);
		return new BlockPos(blockX, blockY, blockZ);
	}

	public static Entity cloneEntity(final Entity sourceEntity) {
		Entity clonedEntity = null;
		final NBTTagCompound entityNBT = sourceEntity.serializeNBT();
		if (entityNBT != null && !entityNBT.hasNoTags() && entityNBT.getSize() > 0) {
			clonedEntity = EntityList.createEntityFromNBT(entityNBT, MCUtils.getWorld());
		}
		else {
			final Class<? extends Entity> clazz = sourceEntity.getClass();
			try {
				clonedEntity = clazz.getConstructor(World.class).newInstance(MCUtils.getWorld());
			}
			catch (final Exception e) {
			}
		}
		return clonedEntity;
	}

	public static Entity getEntityByUUID(final World world, final UUID uuid) {
		for (final Entity entity : world.getLoadedEntityList()) {
			if (entity.getUniqueID() == uuid) {
				return entity;
			}
		}
		return null;
	}

	public static void copyDataFromOld(final Entity oldEntity, final Entity newEntity) {
		final NBTTagCompound nbttagcompound = oldEntity.writeToNBT(new NBTTagCompound());
		nbttagcompound.removeTag("Dimension");
		newEntity.readFromNBT(nbttagcompound);
		newEntity.timeUntilPortal = oldEntity.timeUntilPortal;
		newEntity.lastPortalPos = oldEntity.lastPortalPos;
		newEntity.lastPortalVec = oldEntity.getLastPortalVec();
		newEntity.teleportDirection = oldEntity.getTeleportDirection();
	}

}