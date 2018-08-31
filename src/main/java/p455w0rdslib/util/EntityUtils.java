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

	public static List<? extends Entity> getEntitiesInRange(Class<? extends Entity> entityType, World world, double x, double y, double z, double radius) {
		return world.getEntitiesWithinAABB(entityType, new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius));
	}

	public static IBlockState getBlockStateBelowEntity(Entity entity, int depth) {
		int blockX = MathUtils.floor(entity.posX);
		int blockY = MathUtils.floor(entity.getEntityBoundingBox().minY - depth);
		int blockZ = MathUtils.floor(entity.posZ);
		return EasyMappings.world(entity).getBlockState(new BlockPos(blockX, blockY, blockZ));
	}

	public static Block getBlockBelowEntity(Entity entity, int depth) {
		return getBlockStateBelowEntity(entity, depth).getBlock();
	}

	public static IBlockState getBlockStateAboveEntity(Entity entity, int depth) {
		int blockX = MathUtils.floor(entity.posX);
		int blockY = MathUtils.floor(entity.getEntityBoundingBox().maxY + depth);
		int blockZ = MathUtils.floor(entity.posZ);
		return EasyMappings.world(entity).getBlockState(new BlockPos(blockX, blockY, blockZ));
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

	public static Entity cloneEntity(Entity sourceEntity) {
		if (sourceEntity == null) {
			return null;
		}
		Entity clonedEntity = null;
		NBTTagCompound entityNBT = sourceEntity.serializeNBT();
		if (entityNBT != null && !entityNBT.hasNoTags() && entityNBT.getSize() > 0) {
			clonedEntity = EntityList.createEntityFromNBT(entityNBT, MCUtils.getWorld());
		}
		else {
			Class<? extends Entity> clazz = EntityList.NAME_TO_CLASS.get(EntityList.getEntityString(sourceEntity));
			try {
				clonedEntity = clazz.getConstructor(World.class).newInstance(MCUtils.getWorld());
			}
			catch (Exception e) {
			}
		}
		return clonedEntity;
	}

	public static Entity getEntityByUUID(World world, UUID uuid) {
		for (Entity entity : world.getLoadedEntityList()) {
			if (entity.getUniqueID() == uuid) {
				return entity;
			}
		}
		return null;
	}

	public static void copyDataFromOld(Entity oldEntity, Entity newEntity) {
		NBTTagCompound nbttagcompound = oldEntity.writeToNBT(new NBTTagCompound());
		nbttagcompound.removeTag("Dimension");
		newEntity.readFromNBT(nbttagcompound);
		newEntity.timeUntilPortal = oldEntity.timeUntilPortal;
		MCPrivateUtils.setEntityLastPortalPos(newEntity, MCPrivateUtils.getEntityLastPortalPos(oldEntity));
		MCPrivateUtils.setEntityLastPortalVec(newEntity, MCPrivateUtils.getEntityLastPortalVec(oldEntity));
		MCPrivateUtils.setEntityTeleportDirection(newEntity, MCPrivateUtils.getEntityTeleportDirection(oldEntity));
	}

}
