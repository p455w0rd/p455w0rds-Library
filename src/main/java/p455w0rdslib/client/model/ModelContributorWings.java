package p455w0rdslib.client.model;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import p455w0rdslib.LibGlobals;
import p455w0rdslib.client.particle.ParticleWings;
import p455w0rdslib.client.render.LayerContributorWings;
import p455w0rdslib.math.Pos3D;
import p455w0rdslib.util.ContributorUtils;
import p455w0rdslib.util.MCPrivateUtils;
import p455w0rdslib.util.RenderUtils;

/**
 * @author brandon3055 - modified by TheRealp455w0rd
 *
 */
public class ModelContributorWings extends ModelBase {

	public ModelRenderer rightBaseStem;
	public ModelRenderer leftBaseStem;
	public ModelRenderer rightOuterStem;
	public ModelRenderer rightWingInner;
	public ModelRenderer rightWingOuter;
	public ModelRenderer leftOuterStem;
	public ModelRenderer leftWingInner;
	public ModelRenderer leftWingOuter;
	public RenderPlayer renderPlayer = null;

	public ModelContributorWings() {
		textureWidth = 32;
		textureHeight = 32;
		setupModel();
	}

	private void setupModel() {
		leftWingOuter = new ModelRenderer(this, 0, 18);
		leftWingOuter.mirror = true;
		leftWingOuter.setRotationPoint(0.5F, 0.0F, 0.0F);
		//        this.leftWingOuter.addBox(-0.5F, 0.5F, 0.0F, 15, 14, 0, 0.0F);
		leftWingOuter.cubeList.add(new ModelBoxFace(leftWingOuter, MCPrivateUtils.getTextureOffsetX(leftWingOuter) - 15, MCPrivateUtils.getTextureOffsetY(leftWingOuter), -2.0F, 0.5F, 0.0F, 25, 14, 0, 5));
		rightOuterStem = new ModelRenderer(this, 0, 2);
		rightOuterStem.setRotationPoint(-12.5F, 0.0F, 0.0F);
		rightOuterStem.addBox(-22.0F, -0.5F, -0.5F, 22, 1, 1, 0.0F);
		setRotateAngle(rightOuterStem, 0.0F, -0.6108652381980153F, 0.0F);
		leftWingInner = new ModelRenderer(this, 0, 4);
		leftWingInner.setRotationPoint(0.0F, 0.0F, 0.0F);
		//        this.leftWingInner.addBox(-0.5F, 0.5F, 0.0F, 13, 14, 0, 0.0F);
		leftWingInner.cubeList.add(new ModelBoxFace(leftWingInner, MCPrivateUtils.getTextureOffsetX(leftWingInner), MCPrivateUtils.getTextureOffsetY(leftWingInner), -0.5F, 0.5F, 0.0F, 13, 14, 0, 4));
		rightWingInner = new ModelRenderer(this, 0, 4);
		rightWingInner.mirror = true;
		rightWingInner.setRotationPoint(0.0F, 0.0F, 0.0F);
		//        this.rightWingInner.addBox(-12.5F, 0.5F, 0.0F, 13, 14, 0, 0.0F);
		rightWingInner.cubeList.add(new ModelBoxFace(rightWingInner, MCPrivateUtils.getTextureOffsetX(rightWingInner), MCPrivateUtils.getTextureOffsetY(rightWingInner), -12.5F, 0.5F, 0.0F, 13, 14, 0, 4));
		rightWingOuter = new ModelRenderer(this, 0, 18);
		rightWingOuter.setRotationPoint(-0.5F, 0.0F, 0.0F);
		//        this.rightWingOuter.addBox(-14.5F, 0.5F, 0.0F, 15, 14, 0, 0.0F);
		rightWingOuter.cubeList.add(new ModelBoxFace(rightWingOuter, MCPrivateUtils.getTextureOffsetX(rightWingOuter) - 15, MCPrivateUtils.getTextureOffsetY(rightWingOuter), -23F, 0.5F, 0.0F, 25, 14, 0, 5));
		leftBaseStem = new ModelRenderer(this, 0, 0);
		leftBaseStem.setRotationPoint(0.5F, 1.0F, 2.0F);
		leftBaseStem.addBox(-0.5F, -0.5F, -0.5F, 13, 1, 1, 0.0F);
		setRotateAngle(leftBaseStem, 0.0F, -0.3490658503988659F, 0.0F);
		leftOuterStem = new ModelRenderer(this, 0, 2);
		leftOuterStem.setRotationPoint(12.5F, 0.0F, 0.0F);
		leftOuterStem.addBox(0.0F, -0.5F, -0.5F, 22, 1, 1, 0.0F);
		setRotateAngle(leftOuterStem, 0.0F, 0.6108652381980153F, 0.0F);
		rightBaseStem = new ModelRenderer(this, 0, 0);
		rightBaseStem.setRotationPoint(-0.5F, 1.0F, 2.0F);
		rightBaseStem.addBox(-12.5F, -0.5F, -0.5F, 13, 1, 1, 0.0F);
		setRotateAngle(rightBaseStem, 0.0F, 0.3490658503988659F, 0.0F);
		leftOuterStem.addChild(leftWingOuter);
		rightBaseStem.addChild(rightOuterStem);
		leftBaseStem.addChild(leftWingInner);
		rightBaseStem.addChild(rightWingInner);
		rightOuterStem.addChild(rightWingOuter);
		leftBaseStem.addChild(leftOuterStem);
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		//setupModel();
		if (RenderUtils.mc().isGamePaused()) {
			return;
		}
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableCull();

		float baseRot = 0.45906584F;
		float outerRot = 0.61086524F;
		double speed = 20;
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (player.isSprinting() || player.isElytraFlying() || player.capabilities.isFlying || player.capabilities.getWalkSpeed() > 0.1F) {
				speed = 2D;
			}
		}

		float animation = (float) Math.sin((LibGlobals.ELAPSED_TICKS + Minecraft.getMinecraft().getRenderPartialTicks()) / speed) * 0.5F;

		if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isElytraFlying()) {
			float f4 = 1.0F;

			if (entity.motionY < 0.0D) {
				Vec3d vec3d = (new Vec3d(entity.motionX, entity.motionY, entity.motionZ)).normalize();
				f4 = 1.0F - (float) Math.pow(-vec3d.y, 1.5D);
			}

			float f1 = 0;
			f1 = f4 * ((float) Math.PI / 2F) + (1.0F - f4) * f1;
			animation = -3 + (f1 * 2);
		}

		if (Minecraft.getMinecraft().gameSettings.particleSetting == 0 && entity instanceof EntityPlayerSP) {
			EntityPlayerSP player = (EntityPlayerSP) entity;
			if (!(player.getUniqueID().equals(Minecraft.getMinecraft().player.getUniqueID()) && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0)) {
				if (ContributorUtils.isContributor(player) && ContributorUtils.isPlayerSuperSpecial(player)) {
					if (player.getEntityWorld() != null && player.getEntityWorld().rand.nextInt(101) <= 5) {
						Pos3D userPos = new Pos3D(player).translate(0, 1.8, 0);
						float rnga = player.getEntityWorld().rand.nextFloat();
						float rngb = 0.0f - player.getEntityWorld().rand.nextFloat();
						float rng = rnga + rngb;
						float yheight = -0.5f;
						yheight -= Math.abs(rng) / 4.5f;
						Pos3D vLeft = new Pos3D(rng, yheight + 0.12, -0.25).rotatePitch(0).rotateYaw(player.renderYawOffset);
						Pos3D v = userPos.translate(vLeft).translate(new Pos3D(player.motionX, player.motionY, player.motionZ).scale(0.5));
						ParticleManager pm = Minecraft.getMinecraft().effectRenderer;
						Particle particle = new ParticleWings(player.getEntityWorld(), v.x, v.y, v.z);
						if (ContributorUtils.getWingType(player) == LayerContributorWings.Type.RAINBOW) {
							particle.setRBGColorF(LibGlobals.RED / 255.0F, LibGlobals.GREEN / 255.0F, LibGlobals.BLUE / 255.0F);
						}
						else {
							Color color = ContributorUtils.getWingType(player).getColor();
							particle.setRBGColorF(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
						}
						particle.setAlphaF(1.0f);
						particle.setMaxAge(24);
						pm.addEffect(particle);
					}
				}
			}
		}
		if (entity.isSneaking()) {
			leftBaseStem.rotateAngleX = 1.0F;
			rightBaseStem.rotateAngleX = 1.0F;
		}
		else {
			leftBaseStem.rotateAngleX = 0.5F;
			rightBaseStem.rotateAngleX = 0.5F;
		}

		if (entity.isSneaking()) {
			leftBaseStem.offsetY = 0.4F;
			rightBaseStem.offsetY = 0.4F;
		}
		else {
			leftBaseStem.offsetY = 0F;
			rightBaseStem.offsetY = 0F;
		}

		leftBaseStem.rotateAngleY = -baseRot + (animation * 0.1F);
		leftOuterStem.rotateAngleY = outerRot + (animation * 0.5F);
		rightBaseStem.rotateAngleY = baseRot - (animation * 0.1F);
		rightOuterStem.rotateAngleY = -outerRot - (animation * 0.5F);
		/*
		leftBaseStem.rotateAngleY = -baseRot + (animation * 0.15F);
		leftOuterStem.rotateAngleY = outerRot + (animation * 0.3F);
		rightBaseStem.rotateAngleY = baseRot - (animation * 0.15F);
		rightOuterStem.rotateAngleY = -outerRot - (animation * 0.3F);
		*/
		leftBaseStem.render(scale);
		rightBaseStem.render(scale);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}