package p455w0rdslib.util;

import java.io.File;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * @author p455w0rd
 *
 */
public class ImageUtils {

	public static final File IMAGE_DIR = new File(MCUtils.mc().mcDataDir, "SavedImages");

	public static void saveEntity(final Entity entity, final int rotX, final int rotY, final boolean useMouseRot) {
		if (entity == null) {
			return;
		}
		if (entity instanceof EntityLivingBase) {
			//EntityLivingBase entityClone = (EntityLivingBase) EntityUtils.cloneEntity(entity);

			RenderUtils.pushFBO();
			final AxisAlignedBB aabb = entity.getEntityBoundingBox();
			final double minX = aabb.minX - entity.posX;
			final double maxX = aabb.maxX - entity.posX;
			final double minY = aabb.minY - entity.posY;
			final double maxY = aabb.maxY - entity.posY;
			final double minZ = aabb.minZ - entity.posZ;
			final double maxZ = aabb.maxZ - entity.posZ;

			final double minBound = Math.min(minX, Math.min(minY, minZ));
			final double maxBound = Math.max(maxX, Math.max(maxY, maxZ));

			final double boundLimit = Math.max(Math.abs(minBound), Math.abs(maxBound));

			GlStateManager.matrixMode(GL11.GL_PROJECTION);
			GlStateManager.pushMatrix();
			GlStateManager.loadIdentity();
			GlStateManager.ortho(-boundLimit * 0.75, boundLimit * 0.75, boundLimit * 0.25, -boundLimit * 1.25, -100.0, 100.0);

			GlStateManager.matrixMode(GL11.GL_MODELVIEW);

			//renderEntity((EntityLivingBase) entity);
			//RenderUtils.getRenderManager().setPlayerViewY(180.0F);
			//GuiInventory.drawEntityOnScreen(0, 0, 1, rotX, rotY, (EntityLivingBase) entity);
			RenderUtils.renderLivingEntity(0, 0, 1, rotX, rotY, (EntityLivingBase) entity, useMouseRot);

			GlStateManager.matrixMode(GL11.GL_PROJECTION);
			GlStateManager.popMatrix();

			RenderUtils.popFBO();
			RenderUtils.saveImage();
		}
	}

	public static void saveCurrentItem() {
		final ItemStack stack = PlayerUtils.getPlayer().getHeldItemMainhand();
		if (stack != null && stack.getItem() != null) {
			RenderUtils.pushFBO();
			GlStateManager.matrixMode(GL11.GL_PROJECTION);
			GlStateManager.pushMatrix();
			GlStateManager.loadIdentity();
			GlStateManager.ortho(0, 16, 16, 0, -100000.0, 100000.0);
			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
			final FloatBuffer matrix = GLAllocation.createDirectFloatBuffer(16);
			matrix.clear();
			//@formatter:off
			matrix.put(new float[] {
					1f,0f,0f,0f,
					0f,1f,0f,0f,
					0f,0f,-1f,0f,
					0f,0f,0f,1f
			});
			//@formatter:on
			matrix.rewind();
			//GlStateManager.multMatrix(matrix);
			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.enableRescaleNormal();
			GlStateManager.enableColorMaterial();
			GlStateManager.enableLighting();
			RenderUtils.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
			GlStateManager.disableLighting();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.matrixMode(GL11.GL_PROJECTION);
			GlStateManager.popMatrix();
			RenderUtils.popFBO();
			RenderUtils.saveImage();
		}
		else {
			if (MCUtils.mc().pointedEntity != null && MCUtils.mc().pointedEntity instanceof EntityLivingBase) {
				final EntityLivingBase entity = (EntityLivingBase) MCUtils.mc().pointedEntity;
				final EntityLivingBase entityClone = (EntityLivingBase) EntityUtils.cloneEntity(entity);
				if (entityClone == null) {
					return;
				}
				RenderUtils.pushFBO();
				final AxisAlignedBB aabb = entityClone.getEntityBoundingBox();
				final double minX = aabb.minX - entityClone.posX;
				final double maxX = aabb.maxX - entityClone.posX;
				final double minY = aabb.minY - entityClone.posY;
				final double maxY = aabb.maxY - entityClone.posY;
				final double minZ = aabb.minZ - entityClone.posZ;
				final double maxZ = aabb.maxZ - entityClone.posZ;

				final double minBound = Math.min(minX, Math.min(minY, minZ));
				final double maxBound = Math.max(maxX, Math.max(maxY, maxZ));

				final double boundLimit = Math.max(Math.abs(minBound), Math.abs(maxBound));

				GlStateManager.matrixMode(GL11.GL_PROJECTION);
				GlStateManager.pushMatrix();
				GlStateManager.loadIdentity();
				GlStateManager.ortho(-boundLimit * 0.75, boundLimit * 0.75, boundLimit * 0.25, -boundLimit * 1.25, -100.0, 100.0);

				GlStateManager.matrixMode(GL11.GL_MODELVIEW);

				renderEntity(entityClone);
				//GuiInventory.drawEntityOnScreen(0, 0, 1, 1, 1, current);

				GlStateManager.matrixMode(GL11.GL_PROJECTION);
				GlStateManager.popMatrix();

				RenderUtils.popFBO();
				RenderUtils.saveImage();
			}
		}
	}

	private static void renderEntity(final EntityLivingBase entity) {
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0, 50.0F);
		GlStateManager.scale(-1.0f, 1.0f, 1.0f);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		/*
		float f2 = entity.renderYawOffset;
		float f3 = entity.rotationYaw;
		float f4 = entity.rotationPitch;
		float f5 = entity.prevRotationYawHead;
		float f6 = entity.rotationYawHead;

		float f2 = 0.0f;
		float f3 = 0.0f;
		float f4 = 0.0f;
		float f5 = 0.0f;
		float f6 = 0.0f;
		*/
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);

		GlStateManager.rotate((float) Math.toDegrees(Math.asin(Math.tan(Math.toRadians(30)))), 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(-90, 0.0F, 1.0F, 0.0F);
		if (entity instanceof EntityPig) {
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
		}
		GlStateManager.rotate(-((float) Math.atan(1 / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
		//entity.renderYawOffset = (float) Math.atan(1 / 40.0F) * 20.0F;
		//entity.rotationYaw = (float) Math.atan(1 / 40.0F) * 40.0F;
		//entity.rotationPitch = -((float) Math.atan(1 / 40.0F)) * 20.0F;
		//entity.rotationYawHead = 0.0f;//entity.rotationYaw;
		//entity.prevRotationYawHead = 0.0f;//entity.rotationYaw;
		//GlStateManager.translate(0.0F, 0.0F, 0.0F);
		final RenderManager rendermanager = RenderUtils.getRenderManager();
		rendermanager.setPlayerViewY(90.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.renderEntity(entity, 0.0D, 0.0D, 0.0D, entity.rotationYaw, RenderUtils.getPartialTicks(), false);
		rendermanager.setRenderShadow(true);
		//entity.renderYawOffset = f2;
		//entity.rotationYaw = f3;
		//entity.rotationPitch = f4;
		//entity.prevRotationYawHead = f5;
		//entity.rotationYawHead = f6;
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

}
