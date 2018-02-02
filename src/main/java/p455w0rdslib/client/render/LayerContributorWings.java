package p455w0rdslib.client.render;

import java.awt.Color;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rdslib.LibGlobals;
import p455w0rdslib.LibGlobals.ConfigOptions;
import p455w0rdslib.client.model.ModelContributorWings;
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
	public void doRenderLayer(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!ContributorUtils.isContributor(player) || (!ConfigOptions.ENABLE_CONTRIB_CAPE && player.getUniqueID().equals(EasyMappings.player().getUniqueID()))) {
			return;
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		GlStateManager.disableBlend();
		RenderUtils.bindTexture(new ResourceLocation(LibGlobals.MODID, ContributorUtils.getWingType(player).getTexture()));

		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, 0.0 - 0.025F, 0.075F);
		GlStateManager.scale(0.5, 0.5, 0.5);
		modelWings.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, player);
		modelWings.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		if (ContributorUtils.isPlayerSpecial(player)) {
			renderEnchantedGlint(RenderUtils.getRenderPlayer(player), player, modelWings, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, ContributorUtils.getWingType(player));
		}
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
			EMERALD("textures/models/wings.png", new Color(0xFF17996D), "_EWINGS"),
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

		Type(String texture, Color colorIn, String ident) {
			textureLoc = texture;
			color = colorIn;
			identifier = ident;
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
	}

}