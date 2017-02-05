package p455w0rdslib.util;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author p455w0rd
 *
 */
@SideOnly(Side.CLIENT)
public class RenderUtils {

	public static ItemModelMesher getMesher() {
		return getRenderItem().getItemModelMesher();
	}

	public static Minecraft mc() {
		return MCUtils.mc();
	}

	public static RenderItem getRenderItem() {
		return mc().getRenderItem();
	}

	public static TextureManager getTextureManager() {
		return mc().getTextureManager();
	}

	public static FontRenderer getFontRenderer() {
		return mc().fontRendererObj;
	}

	public static TextureManager getRenderEngine() {
		return mc().renderEngine;
	}

	public static void renderHighlightText(ScaledResolution scaledRes, int yOffset, String text) {
		Minecraft mc = Minecraft.getMinecraft();
		String s = TextFormatting.ITALIC + "" + text;

		int i = (scaledRes.getScaledWidth() - getFontRenderer().getStringWidth(text)) / 2;
		int j = scaledRes.getScaledHeight() - 59 - yOffset;

		if (!mc.playerController.shouldDrawHUD()) {
			j += 14;
		}

		int k = 255;
		GlStateManager.pushMatrix();
		//GlStateManager.enableBlend();
		//GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		getFontRenderer().drawStringWithShadow(s, i, j, 16777215 + (k << 24));
		//GlStateManager.disableBlend();
		GlStateManager.popMatrix();

	}

	/**
	 * vanilla beamRadius = 0.2D
	 * vanilla glowRadius = 0.25D
	 */
	public static void renderBeam(TileEntity tileEntity, float partialTicks, int length, @Nonnull int red, @Nonnull int blue, @Nonnull int green, double beamRadius, double glowRadius, EnumFacing... sides) {
		if (tileEntity == null || tileEntity.getWorld() == null) {
			return;
		}
		length = length < 1 ? 1 : length;
		List<EnumFacing> sideList = Arrays.asList(sides);
		int height = length;
		float[] colors = new float[] {
				((float) red) / 255, ((float) green) / 255, ((float) blue) / 255
		};
		float a = 1.0F;
		double yOffset = 0.5D;
		BlockPos pos = tileEntity.getPos();
		double x = pos.getX() - TileEntityRendererDispatcher.staticPlayerX;
		double y = pos.getY() - TileEntityRendererDispatcher.staticPlayerY;
		double z = pos.getZ() - TileEntityRendererDispatcher.staticPlayerZ;
		double totalWorldTime = tileEntity.getWorld().getTotalWorldTime();
		double textureScale = 1.0;
		double i = yOffset + height;
		Minecraft.getMinecraft().getTextureManager().bindTexture(TileEntityBeaconRenderer.TEXTURE_BEACON_BEAM);
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.glTexParameteri(3553, 10242, 10497);
		GlStateManager.glTexParameteri(3553, 10243, 10497);
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		GlStateManager.disableFog();
		GlStateManager.depthMask(true);
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		double d0 = totalWorldTime + partialTicks;
		double d1 = height < 0 ? d0 : -d0;
		double d2 = MathHelper.frac(d1 * 0.2D - MathHelper.floor_double(d1 * 0.1D));
		float f = colors[0];
		float f1 = colors[1];
		float f2 = colors[2];
		double d3 = d0 * 0.025D * -1.5D;
		double d4 = 0.5D + Math.cos(d3 + 2.356194490192345D) * beamRadius;
		double d5 = 0.5D + Math.sin(d3 + 2.356194490192345D) * beamRadius;
		double d6 = 0.5D + Math.cos(d3 + (Math.PI / 4D)) * beamRadius;
		double d7 = 0.5D + Math.sin(d3 + (Math.PI / 4D)) * beamRadius;
		double d8 = 0.5D + Math.cos(d3 + 3.9269908169872414D) * beamRadius;
		double d9 = 0.5D + Math.sin(d3 + 3.9269908169872414D) * beamRadius;
		double d10 = 0.5D + Math.cos(d3 + 5.497787143782138D) * beamRadius;
		double d11 = 0.5D + Math.sin(d3 + 5.497787143782138D) * beamRadius;
		double d12 = 0.0D;
		double d13 = 1.0D;
		double d14 = -1.0D + d2;
		double d15 = height * textureScale * (0.5D / beamRadius) + d14;
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		if (sideList.contains(EnumFacing.UP)) {
			vertexbuffer.pos(x + d4, y + i, z + d5).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y + yOffset, z + d5).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + yOffset, z + d7).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + i, z + d7).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + i, z + d11).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + yOffset, z + d11).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + yOffset, z + d9).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + i, z + d9).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + i, z + d7).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + yOffset, z + d7).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + yOffset, z + d11).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + i, z + d11).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + i, z + d9).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + yOffset, z + d9).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y + yOffset, z + d5).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y + i, z + d5).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
		}
		if (sideList.contains(EnumFacing.SOUTH)) {
			vertexbuffer.pos(x + d4, y + d5, z + i).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y + d5, z + yOffset).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + d7, z + yOffset).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + d7, z + i).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + d11, z + i).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + d11, z + yOffset).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + d9, z + yOffset).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + d9, z + i).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + d7, z + i).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + d7, z + yOffset).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + d11, z + yOffset).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + d11, z + i).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + d9, z + i).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + d9, z + yOffset).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y + d5, z + yOffset).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y + d5, z + i).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
		}
		if (sideList.contains(EnumFacing.EAST)) {
			vertexbuffer.pos(x + i, y + d4, z + d5).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + yOffset, y + d4, z + d5).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + yOffset, y + d6, z + d7).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + i, y + d6, z + d7).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + i, y + d10, z + d11).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + yOffset, y + d10, z + d11).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + yOffset, y + d8, z + d9).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + i, y + d8, z + d9).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + i, y + d6, z + d7).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + yOffset, y + d6, z + d7).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + yOffset, y + d10, z + d11).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + i, y + d10, z + d11).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + i, y + d8, z + d9).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + yOffset, y + d8, z + d9).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + yOffset, y + d4, z + d5).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + i, y + d4, z + d5).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
		}
		if (sideList.contains(EnumFacing.DOWN)) {
			vertexbuffer.pos(x + d4, y - i + 1, z + d5).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y - yOffset + 1, z + d5).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y - yOffset + 1, z + d7).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y - i + 1, z + d7).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y - i + 1, z + d11).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y - yOffset + 1, z + d11).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y - yOffset + 1, z + d9).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y - i + 1, z + d9).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y - i + 1, z + d7).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y - yOffset + 1, z + d7).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y - yOffset + 1, z + d11).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y - i + 1, z + d11).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y - i + 1, z + d9).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y - yOffset + 1, z + d9).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y - yOffset + 1, z + d5).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y - i + 1, z + d5).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
		}
		if (sideList.contains(EnumFacing.NORTH)) {
			vertexbuffer.pos(x + d4, y + d5, z - i + 1).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y + d5, z - yOffset + 1).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + d7, z - yOffset + 1).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + d7, z - i + 1).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + d11, z - i + 1).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + d11, z - yOffset + 1).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + d9, z - yOffset + 1).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + d9, z - i + 1).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + d7, z - i + 1).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + d7, z - yOffset + 1).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + d11, z - yOffset + 1).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + d11, z - i + 1).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + d9, z - i + 1).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + d9, z - yOffset + 1).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y + d5, z - yOffset + 1).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y + d5, z - i + 1).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
		}
		if (sideList.contains(EnumFacing.WEST)) {
			vertexbuffer.pos(x - i + 1, y + d4, z + d5).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d4, z + d5).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d6, z + d7).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - i + 1, y + d6, z + d7).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - i + 1, y + d10, z + d11).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d10, z + d11).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d8, z + d9).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - i + 1, y + d8, z + d9).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - i + 1, y + d6, z + d7).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d6, z + d7).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d10, z + d11).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - i + 1, y + d10, z + d11).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - i + 1, y + d8, z + d9).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d8, z + d9).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d4, z + d5).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - i + 1, y + d4, z + d5).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
		}
		tessellator.draw();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.depthMask(false);
		d3 = 0.5D - glowRadius;
		d4 = 0.5D - glowRadius;
		d5 = 0.5D + glowRadius;
		d6 = 0.5D - glowRadius;
		d7 = 0.5D - glowRadius;
		d8 = 0.5D + glowRadius;
		d9 = 0.5D + glowRadius;
		d10 = 0.5D + glowRadius;
		d11 = 0.0D;
		d12 = 1.0D;
		d13 = -1.0D + d2;
		d14 = height * textureScale + d13;
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		if (sideList.contains(EnumFacing.UP)) {
			vertexbuffer.pos(x + d3, y + i, z + d4).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d3, y + yOffset, z + d4).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d5, y + yOffset, z + d6).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d5, y + i, z + d6).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d9, y + i, z + d10).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d9, y + yOffset, z + d10).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d7, y + yOffset, z + d8).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d7, y + i, z + d8).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d5, y + i, z + d6).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d5, y + yOffset, z + d6).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d9, y + yOffset, z + d10).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d9, y + i, z + d10).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d7, y + i, z + d8).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d7, y + yOffset, z + d8).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d3, y + yOffset, z + d4).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d3, y + i, z + d4).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
		}
		if (sideList.contains(EnumFacing.SOUTH)) {
			vertexbuffer.pos(x + d3, y + d4, z + i).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d3, y + d4, z + yOffset).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d5, y + d6, z + yOffset).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d5, y + d6, z + i).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d9, y + d10, z + i).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d9, y + d10, z + yOffset).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d7, y + d8, z + yOffset).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d7, y + d8, z + i).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d5, y + d6, z + i).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d5, y + d6, z + yOffset).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d9, y + d10, z + yOffset).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d9, y + d10, z + i).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d7, y + d8, z + i).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d7, y + d8, z + yOffset).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d3, y + d4, z + yOffset).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d3, y + d4, z + i).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
		}
		if (sideList.contains(EnumFacing.EAST)) {
			vertexbuffer.pos(x + i, y + d3, z + d4).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + yOffset, y + d3, z + d4).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + yOffset, y + d5, z + d6).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + i, y + d5, z + d6).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + i, y + d9, z + d10).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + yOffset, y + d9, z + d10).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + yOffset, y + d7, z + d8).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + i, y + d7, z + d8).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + i, y + d5, z + d6).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + yOffset, y + d5, z + d6).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + yOffset, y + d9, z + d10).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + i, y + d9, z + d10).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + i, y + d7, z + d8).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + yOffset, y + d7, z + d8).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + yOffset, y + d3, z + d4).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + i, y + d3, z + d4).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
		}
		if (sideList.contains(EnumFacing.DOWN)) {
			vertexbuffer.pos(x + d3, y - i + 1, z + d4).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d3, y - yOffset + 1, z + d4).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d5, y - yOffset + 1, z + d6).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d5, y - i + 1, z + d6).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d9, y - i + 1, z + d10).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d9, y - yOffset + 1, z + d10).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d7, y - yOffset + 1, z + d8).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d7, y - i + 1, z + d8).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d5, y - i + 1, z + d6).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d5, y - yOffset + 1, z + d6).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d9, y - yOffset + 1, z + d10).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d9, y - i + 1, z + d10).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d7, y - i + 1, z + d8).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d7, y - yOffset + 1, z + d8).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d3, y - yOffset + 1, z + d4).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d3, y - i + 1, z + d4).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
		}
		if (sideList.contains(EnumFacing.NORTH)) {
			vertexbuffer.pos(x + d3, y + d4, z - i + 1).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d3, y + d4, z - yOffset + 1).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d5, y + d6, z - yOffset + 1).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d5, y + d6, z - i + 1).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d9, y + d10, z - i + 1).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d9, y + d10, z - yOffset + 1).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d7, y + d8, z - yOffset + 1).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d7, y + d8, z - i + 1).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d5, y + d6, z - i + 1).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d5, y + d6, z - yOffset + 1).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d9, y + d10, z - yOffset + 1).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d9, y + d10, z - i + 1).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d7, y + d8, z - i + 1).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d7, y + d8, z - yOffset + 1).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d3, y + d4, z - yOffset + 1).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x + d3, y + d4, z - i + 1).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
		}
		if (sideList.contains(EnumFacing.WEST)) {
			vertexbuffer.pos(x - i + 1, y + d3, z + d4).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d3, z + d4).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d5, z + d6).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x - i + 1, y + d5, z + d6).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x - i + 1, y + d9, z + d10).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d9, z + d10).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d7, z + d8).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x - i + 1, y + d7, z + d8).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x - i + 1, y + d5, z + d6).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d5, z + d6).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d9, z + d10).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x - i + 1, y + d9, z + d10).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x - i + 1, y + d7, z + d8).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d7, z + d8).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d3, z + d4).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
			vertexbuffer.pos(x - i + 1, y + d3, z + d4).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
		}
		tessellator.draw();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.depthMask(true);
		GlStateManager.enableFog();
	}

	public static void renderBeamNoGlow(TileEntity tileEntity, float partialTicks, int length, @Nonnull int red, @Nonnull int blue, @Nonnull int green, @Nonnull int beamAlpha, double beamRadius, EnumFacing... sides) {
		if (tileEntity == null || tileEntity.getWorld() == null) {
			return;
		}
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/entity/beacon_beam.png"));
		List<EnumFacing> sideList = Arrays.asList(sides);
		length = length < 1 ? 1 : length;
		int height = length;
		float[] colors = new float[] {
				((float) red) / 255, ((float) green) / 255, ((float) blue) / 255
		};
		float a = ((float) beamAlpha) / 255;
		double yOffset = 0.5;
		//double partialTicks = 0.0D;
		BlockPos pos = tileEntity.getPos();
		double x = pos.getX() - TileEntityRendererDispatcher.staticPlayerX;
		double y = pos.getY() - TileEntityRendererDispatcher.staticPlayerY;
		double z = pos.getZ() - TileEntityRendererDispatcher.staticPlayerZ;
		double totalWorldTime = tileEntity.getWorld().getTotalWorldTime();
		double textureScale = 1.0;
		double i = yOffset + height;

		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.disableFog();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.glTexParameteri(3553, 10242, 10497);
		GlStateManager.glTexParameteri(3553, 10243, 10497);
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		GlStateManager.disableLighting();
		GlStateManager.depthMask(true);

		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		double d0 = totalWorldTime + partialTicks;
		double d1 = height < 0 ? d0 : -d0;
		double d2 = MathHelper.frac(d1 * 0.2D - MathHelper.floor_double(d1 * 0.1D));
		float f = colors[0];
		float f1 = colors[1];
		float f2 = colors[2];
		double d3 = d0 * 0.025D * -1.5D;
		double d4 = 0.5D + Math.cos(d3 + 2.356194490192345D) * beamRadius;
		double d5 = 0.5D + Math.sin(d3 + 2.356194490192345D) * beamRadius;
		double d6 = 0.5D + Math.cos(d3 + (Math.PI / 4D)) * beamRadius;
		double d7 = 0.5D + Math.sin(d3 + (Math.PI / 4D)) * beamRadius;
		double d8 = 0.5D + Math.cos(d3 + 3.9269908169872414D) * beamRadius;
		double d9 = 0.5D + Math.sin(d3 + 3.9269908169872414D) * beamRadius;
		double d10 = 0.5D + Math.cos(d3 + 5.497787143782138D) * beamRadius;
		double d11 = 0.5D + Math.sin(d3 + 5.497787143782138D) * beamRadius;
		double d12 = 0.0D;
		double d13 = 1.0D;
		double d14 = -1.0D + d2;
		double d15 = height * textureScale * (0.5D / beamRadius) + d14;
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		if (sideList.contains(EnumFacing.UP)) {
			vertexbuffer.pos(x + d4, y + i, z + d5).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y + yOffset, z + d5).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + yOffset, z + d7).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + i, z + d7).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + i, z + d11).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + yOffset, z + d11).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + yOffset, z + d9).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + i, z + d9).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + i, z + d7).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + yOffset, z + d7).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + yOffset, z + d11).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + i, z + d11).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + i, z + d9).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + yOffset, z + d9).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y + yOffset, z + d5).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y + i, z + d5).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
		}
		if (sideList.contains(EnumFacing.SOUTH)) {
			vertexbuffer.pos(x + d4, y + d5, z + i).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y + d5, z + yOffset).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + d7, z + yOffset).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + d7, z + i).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + d11, z + i).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + d11, z + yOffset).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + d9, z + yOffset).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + d9, z + i).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + d7, z + i).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + d7, z + yOffset).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + d11, z + yOffset).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + d11, z + i).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + d9, z + i).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + d9, z + yOffset).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y + d5, z + yOffset).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y + d5, z + i).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
		}
		if (sideList.contains(EnumFacing.EAST)) {
			vertexbuffer.pos(x + i, y + d4, z + d5).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + yOffset, y + d4, z + d5).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + yOffset, y + d6, z + d7).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + i, y + d6, z + d7).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + i, y + d10, z + d11).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + yOffset, y + d10, z + d11).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + yOffset, y + d8, z + d9).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + i, y + d8, z + d9).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + i, y + d6, z + d7).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + yOffset, y + d6, z + d7).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + yOffset, y + d10, z + d11).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + i, y + d10, z + d11).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + i, y + d8, z + d9).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + yOffset, y + d8, z + d9).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + yOffset, y + d4, z + d5).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + i, y + d4, z + d5).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
		}
		if (sideList.contains(EnumFacing.DOWN)) {
			vertexbuffer.pos(x + d4, y - i + 1, z + d5).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y - yOffset + 1, z + d5).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y - yOffset + 1, z + d7).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y - i + 1, z + d7).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y - i + 1, z + d11).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y - yOffset + 1, z + d11).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y - yOffset + 1, z + d9).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y - i + 1, z + d9).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y - i + 1, z + d7).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y - yOffset + 1, z + d7).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y - yOffset + 1, z + d11).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y - i + 1, z + d11).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y - i + 1, z + d9).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y - yOffset + 1, z + d9).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y - yOffset + 1, z + d5).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y - i + 1, z + d5).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
		}
		if (sideList.contains(EnumFacing.NORTH)) {
			vertexbuffer.pos(x + d4, y + d5, z - i + 1).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y + d5, z - yOffset + 1).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + d7, z - yOffset + 1).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + d7, z - i + 1).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + d11, z - i + 1).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + d11, z - yOffset + 1).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + d9, z - yOffset + 1).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + d9, z - i + 1).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + d7, z - i + 1).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d6, y + d7, z - yOffset + 1).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + d11, z - yOffset + 1).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d10, y + d11, z - i + 1).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + d9, z - i + 1).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d8, y + d9, z - yOffset + 1).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y + d5, z - yOffset + 1).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x + d4, y + d5, z - i + 1).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
		}
		if (sideList.contains(EnumFacing.WEST)) {
			vertexbuffer.pos(x - i + 1, y + d4, z + d5).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d4, z + d5).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d6, z + d7).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - i + 1, y + d6, z + d7).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - i + 1, y + d10, z + d11).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d10, z + d11).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d8, z + d9).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - i + 1, y + d8, z + d9).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - i + 1, y + d6, z + d7).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d6, z + d7).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d10, z + d11).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - i + 1, y + d10, z + d11).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - i + 1, y + d8, z + d9).tex(1.0D, d15).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d8, z + d9).tex(1.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - yOffset + 1, y + d4, z + d5).tex(0.0D, d14).color(f, f1, f2, a).endVertex();
			vertexbuffer.pos(x - i + 1, y + d4, z + d5).tex(0.0D, d15).color(f, f1, f2, a).endVertex();
		}
		tessellator.draw();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.enableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.enableFog();
	}

}
