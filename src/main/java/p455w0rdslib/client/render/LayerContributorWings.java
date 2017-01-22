package p455w0rdslib.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rdslib.LibGlobals;
import p455w0rdslib.client.model.ModelContributorWings;
import p455w0rdslib.util.ContributorUtils;

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
		if (!ContributorUtils.isContributor(player)) {
			return;
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		GlStateManager.disableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(LibGlobals.MODID, getTexture(ContributorUtils.getWingType(player))));

		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, 0.0 - 0.025F, 0.075F);
		GlStateManager.scale(0.5, 0.5, 0.5);
		modelWings.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, player);
		modelWings.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		//    renderEnchantedGlint(this.renderPlayer, player, this.modelWings, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.popMatrix();

	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

	private String getTexture(Type type) {
		switch (type) {
		case EMERALD:
			return "textures/models/wings.png";
		case BLOOD:
			return "textures/models/wings_r.png";
		case BLUE:
			return "textures/models/wings_b.png";
		case XMAS:
			return "textures/models/wings_xmas.png";
		}
		return "";
	}

	public static enum Type {
		EMERALD, BLOOD, BLUE, XMAS;
	}

}