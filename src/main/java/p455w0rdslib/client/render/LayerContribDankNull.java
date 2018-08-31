package p455w0rdslib.client.render;

import java.awt.Color;
import java.util.Map;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rdslib.LibGlobals;
import p455w0rdslib.client.model.ModelContribDankNull;
import p455w0rdslib.util.RenderUtils;

/**
 * @author p455w0rd
 *
 */
@SideOnly(Side.CLIENT)
public class LayerContribDankNull implements LayerRenderer<AbstractClientPlayer> {

	private static final ModelContribDankNull model = new ModelContribDankNull();

	@Override
	public void doRenderLayer(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		ModelSkeletonHead humanoidHead = new ModelHumanoidHead();
		ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();
		Minecraft mc = Minecraft.getMinecraft();
		GameProfile profile = mc.player.getGameProfile();
		if (profile != null) {
			Map<Type, MinecraftProfileTexture> map = mc.getSkinManager().loadSkinFromCache(profile);
			if (map.containsKey(Type.SKIN)) {
				resourcelocation = mc.getSkinManager().loadSkin(map.get(Type.SKIN), Type.SKIN);
			}
			else {
				UUID uuid = EntityPlayer.getUUID(profile);
				resourcelocation = DefaultPlayerSkin.getDefaultSkin(uuid);
			}
		}
		RenderUtils.bindTexture(resourcelocation);
		GlStateManager.pushMatrix();
		GlStateManager.rotate(0.017453292519943F * 180F, 1F, 0F, 0F);

		GlStateManager.rotate(netHeadYaw, 0, 1, 0);
		GlStateManager.rotate(headPitch, 1, 0, 0);
		GlStateManager.disableCull();
		GlStateManager.enableRescaleNormal();
		GlStateManager.translate(-0.01F, player.isSneaking() ? -0.4F : -0.66F, -0.01F);
		GlStateManager.scale(0.25F, 0.25F, 0.25F);
		GlStateManager.enableAlpha();
		GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		GlStateManager.rotate(LibGlobals.TIME2, 1.0F, LibGlobals.TIME2, 1.0F);
		humanoidHead.render((Entity) null, ageInTicks, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		//GlStateManager.disableBlend();
		RenderUtils.bindTexture(new ResourceLocation(LibGlobals.MODID, "textures/models/danknull.png"));
		GlStateManager.enableCull();
		GlStateManager.enableAlpha();
		GlStateManager.enableDepth();
		GlStateManager.pushMatrix();

		GlStateManager.rotate(0.017453292519943F * 180F, 1F, 0F, 0F);

		GlStateManager.rotate(netHeadYaw, 0, 1, 0);
		GlStateManager.rotate(headPitch, 1, 0, 0);

		GlStateManager.translate(-0.175F, player.isSneaking() ? -0.3F : -0.56F, -0.25F);
		GlStateManager.scale(0.35, 0.35, 0.35);
		//model.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, player);
		//renderEnchantedGlint(RenderUtils.getRenderPlayer(player), player, model, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, new Color(0.75F, 0, 0, 1.0F));
		model.render(player, limbSwing, limbSwingAmount, ageInTicks, 0, 0, scale);
		//if (ContributorUtils.isPlayerSpecial(player)) {
		renderEnchantedGlint(RenderUtils.getRenderPlayer(player), player, model, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, new Color(0.75F, 0, 0, 0.1F));
		//}
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.popMatrix();

	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

	public static void renderEnchantedGlint(RenderLivingBase<?> p_188364_0_, EntityLivingBase p_188364_1_, ModelBase model, float p_188364_3_, float p_188364_4_, float p_188364_5_, float p_188364_6_, float p_188364_7_, float p_188364_8_, float p_188364_9_, Color color) {
		float f = p_188364_1_.ticksExisted + p_188364_5_;
		if (color == null) {
			color = new Color(LibGlobals.RED, LibGlobals.GREEN, LibGlobals.BLUE, 255);
		}
		p_188364_0_.bindTexture(new ResourceLocation("textures/misc/enchanted_item_glint.png"));
		GlStateManager.enableBlend();
		//GlStateManager.enableDepth();
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

}
