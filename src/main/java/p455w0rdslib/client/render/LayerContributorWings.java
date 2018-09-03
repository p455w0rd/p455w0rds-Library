package p455w0rdslib.client.render;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rdslib.LibGlobals;
import p455w0rdslib.LibGlobals.ConfigOptions;
import p455w0rdslib.client.model.ModelContributorWings;
import p455w0rdslib.client.particle.ParticleWings;
import p455w0rdslib.math.Pos3D;
import p455w0rdslib.util.ContributorUtils;
import p455w0rdslib.util.EasyMappings;
import p455w0rdslib.util.RenderUtils;

/**
 * @author p455w0rd
 *
 */
@SideOnly(Side.CLIENT)
public class LayerContributorWings implements LayerRenderer<AbstractClientPlayer> {

	private ModelContributorWings modelWings = new ModelContributorWings();

	public LayerContributorWings() {

	}

	@Override
	public void doRenderLayer(AbstractClientPlayer clientPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!ContributorUtils.isContributor(clientPlayer) || (!ConfigOptions.ENABLE_CONTRIB_CAPE && clientPlayer.getUniqueID().equals(EasyMappings.player().getUniqueID()))) {
			return;
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		GlStateManager.disableBlend();
		RenderUtils.bindTexture(new ResourceLocation(LibGlobals.MODID, ContributorUtils.getWingType(clientPlayer).getTexture()));

		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, 0.0 - 0.025F, 0.075F);
		GlStateManager.scale(0.5, 0.5, 0.5);
		modelWings.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, clientPlayer);
		modelWings.render(clientPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		if (ContributorUtils.isPlayerSpecial(clientPlayer.getUniqueID())) {
			renderEnchantedGlint(RenderUtils.getRenderPlayer(clientPlayer), clientPlayer, modelWings, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, ContributorUtils.getWingType(clientPlayer));
		}
		if (Minecraft.getMinecraft().gameSettings.particleSetting == 0 && clientPlayer instanceof EntityPlayer) {
			EntityPlayer player = clientPlayer;
			if (!(player.getUniqueID().equals(Minecraft.getMinecraft().player.getUniqueID()) && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0)) {
				if (ContributorUtils.isPlayerSuperSpecial(player.getUniqueID().toString())) {
					boolean isPlayerSelf = player.getUniqueID().equals(Minecraft.getMinecraft().player.getUniqueID());
					if ((isPlayerSelf && ConfigOptions.ENABLE_CONTRIB_PARTICLES_SELF) || (!isPlayerSelf && ConfigOptions.ENABLE_CONTRIB_PARTICLES_OTHERS)) {
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
							if (ContributorUtils.getWingType(player.getUniqueID().toString()) == LayerContributorWings.Type.RAINBOW) {
								particle.setRBGColorF(LibGlobals.RED / 255.0F, LibGlobals.GREEN / 255.0F, LibGlobals.BLUE / 255.0F);
							}
							else {
								Color color = ContributorUtils.getWingType(player.getUniqueID().toString()).getParticleColor();
								particle.setRBGColorF(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
							}
							particle.setAlphaF(1.0f);
							particle.setMaxAge(24);
							pm.addEffect(particle);
						}
					}
				}
			}
		}
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.popMatrix();
	}

	public static void renderEnchantedGlint(RenderLivingBase<?> p_188364_0_, EntityLivingBase p_188364_1_, ModelBase model, float p_188364_3_, float p_188364_4_, float p_188364_5_, float p_188364_6_, float p_188364_7_, float p_188364_8_, float p_188364_9_, LayerContributorWings.Type type) {
		float f = p_188364_1_.ticksExisted + p_188364_5_;
		Color color = type.getColor();
		if (color == null) {
			color = new Color(LibGlobals.RED, LibGlobals.GREEN, LibGlobals.BLUE, 255);
		}
		p_188364_0_.bindTexture(new ResourceLocation("textures/misc/enchanted_item_glint.png"));
		GlStateManager.enableBlend();
		GlStateManager.depthFunc(514);
		GlStateManager.depthMask(false);
		float f1 = 0.5F;
		GlStateManager.color(f1, f1, f1, 1.0F);

		for (int i = 0; i < 2; ++i) {
			GlStateManager.disableLighting();
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
			float f2 = 0.76F;
			GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
			GlStateManager.matrixMode(5890);
			GlStateManager.loadIdentity();
			float f3 = 0.33333334F;
			GlStateManager.scale(f3, f3, f3);
			GlStateManager.rotate(30.0F - i * 60.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.translate(0.0F, f * (0.001F + i * 0.003F) * 20.0F, 0.0F);
			GlStateManager.matrixMode(5888);
			model.render(p_188364_1_, p_188364_3_, p_188364_4_, p_188364_6_, p_188364_7_, p_188364_8_, p_188364_9_);
		}

		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(5888);
		GlStateManager.enableLighting();
		GlStateManager.depthMask(true);
		GlStateManager.depthFunc(515);
		GlStateManager.disableBlend();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

	public static enum Type {
			EMERALD("textures/models/wings.png", new Color(0xFF17996D), new Color(0xFF17FF6D), "_EWINGS"),
			BLOOD("textures/models/wings_r.png", new Color(170, 0, 0, 255), "_RWINGS"),
			BLOOD2("textures/models/wings_r2.png", new Color(128, 0, 0, 255), "_RWINGS2"),
			BLUE("textures/models/wings_b.png", new Color(0.50F, 0.25F, 0.75F, 1.0F), "_BWINGS"),
			XMAS("textures/models/wings_xmas.png", new Color(0xFF808080), "_XMAS"),
			PURPLE("textures/models/wings_p.png", new Color(148, 3, 221, 255), "_PURPLE"),
			PURPLE2("textures/models/wings_p2.png", new Color(148, 3, 221, 255), "_PURPLE2"),
			GOLD("textures/models/wings_g.png", new Color(0xFFf8ea04), "_GOLD"),
			SIVER("textures/models/wings_silver.png", new Color(0xFFFFFFFF), "_SILVER"),
			RAINBOW("textures/models/wings_silver.png", null, "_RAINBOW"),
			CARBON("textures/models/wings.png", new Color(0.50F, 0.25F, 0.75F, 1.0F), "_CARBON");

		String textureLoc;
		String identifier;
		Color color;
		Color particleColor;

		Type(String texture, Color colorIn, String ident) {
			this(texture, colorIn, colorIn, ident);
		}

		Type(String texture, Color colorIn, Color particleColorIn, String ident) {
			textureLoc = texture;
			color = colorIn;
			identifier = ident;
			particleColor = particleColorIn;
		}

		public String getTexture() {
			return textureLoc;
		}

		public Color getColor() {
			return color;
		}

		public String getIdentifier() {
			return identifier;
		}

		public Color getParticleColor() {
			return particleColor;
		}
	}

}