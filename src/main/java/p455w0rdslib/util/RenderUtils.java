package p455w0rdslib.util;

import java.awt.Color;

// import static net.minecraft.client.renderer.GlStateManager.*;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author p455w0rd
 *
 */
@SideOnly(Side.CLIENT)
public class RenderUtils {

	public static int highlightTextTime = 0, highlightTextYOffset = 0;
	public static String highlightText = "";
	public static float highlightTextScale = 1.0f;
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

	public static ItemModelMesher getMesher() {
		return getRenderItem().getItemModelMesher();
	}

	public static Minecraft mc() {
		return MCUtils.mc();
	}

	public static BlockModelShapes getBlockModelShapes() {
		return getBlockRendererDispatcher().getBlockModelShapes();
	}

	public static ItemRenderer getItemRenderer() {
		return mc().getItemRenderer();
	}

	public static RenderPlayer getRenderPlayer(final AbstractClientPlayer player) {
		return (RenderPlayer) getRenderManager().<AbstractClientPlayer>getEntityRenderObject(player);
	}

	public static RenderItem getRenderItem() {
		return mc().getRenderItem();
	}

	public static BlockRendererDispatcher getBlockRendererDispatcher() {
		return mc().getBlockRendererDispatcher();
	}

	public static RenderManager getRenderManager() {
		return mc().getRenderManager();
	}

	public static TextureManager getTextureManager() {
		return mc().getTextureManager();
	}

	public static FontRenderer getFontRenderer() {
		return mc().fontRenderer;
	}

	public static TextureManager getRenderEngine() {
		return mc().renderEngine;
	}

	public static TextureMap getBlocksTextureMap() {
		return mc().getTextureMapBlocks();
	}

	public static IReloadableResourceManager getResourceManager() {
		return (IReloadableResourceManager) mc().getResourceManager();
	}

	public static TextureAtlasSprite getSprite(final String spritePath) {
		return getBlocksTextureMap().getAtlasSprite(spritePath);
	}

	public static TextureAtlasSprite getSprite(final ResourceLocation location) {
		return getSprite(location.toString());
	}

	public static float getPartialTicks() {
		return mc().getRenderPartialTicks();
	}

	public static void bindTexture(final ResourceLocation location) {
		getRenderEngine().bindTexture(location);
	}

	public static void renderHighlightText(final int yOffset, final String text) {
		renderHighlightText(yOffset, text, 1.0F);
	}

	public static void renderHighlightText(final int yOffset, final String text, final float scale) {
		final ScaledResolution scaledRes = new ScaledResolution(mc());
		final Minecraft mc = Minecraft.getMinecraft();
		if (mc.playerController == null) {
			return;
		}
		final String s = TextFormatting.ITALIC + "" + text;

		int j = scaledRes.getScaledHeight() - yOffset;

		if (!mc.playerController.shouldDrawHUD()) {
			j += 14;
		}

		final int k = 255;
		GlStateManager.pushMatrix();
		GlStateManager.scale(scale, scale, scale);
		final float i = (scaledRes.getScaledWidth() - getFontRenderer().getStringWidth(text) * scale) / 2;
		getFontRenderer().drawString(s, i / scale, j / scale, 16777215 + (k << 24), true);
		GlStateManager.popMatrix();

	}

	public static void renderHighlightTextTimed(final int yOffset, final String text, final float scale, final int ticks) {
		if (highlightTextTime <= 0) {
			highlightTextTime = ticks;
			highlightText = text;
			highlightTextScale = scale;
			highlightTextYOffset = yOffset;
		}
	}

	public static void renderHighlightTextTimed() {
		if (highlightTextTime > 0 && !highlightText.isEmpty()) {
			renderHighlightText(highlightTextYOffset, highlightText, highlightTextScale);
			--highlightTextTime;
		}
		else {
			highlightTextTime = 0;
			highlightTextYOffset = 0;
			highlightText = "";
			highlightTextScale = 1.0f;
		}
	}

	public static void renderBeamHit(@Nonnull final ResourceLocation texture, final Vector3 pos, final float partialTicks, final float scale) {
		GlStateManager.pushMatrix();
		GlStateManager.glTexParameterf(3553, 10242, 10497.0F);
		GlStateManager.glTexParameterf(3553, 10243, 10497.0F);
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0F);
		//GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GlStateManager.disableCull();
		GlStateManager.enableTexture2D();
		//GlStateManager.depthMask(false);
		GlStateManager.color(0F, 1F, 0F, 1F);
		bindTexture(texture);
		final double iX = pos.x;
		final double iY = pos.y;
		final double iZ = pos.z;

		renderFacingQuad(iX, iY, iZ, partialTicks, scale, 0.0F, 0, 0, 1, 1);
		GlStateManager.disableBlend();
		//GlStateManager.enableAlpha();
		//GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.popMatrix();

	}

	public static void renderFacingQuad(final double px, final double py, final double pz, final float partialTicks, final float scale, final float angle, final double u, final double v, final double uLength, final double vLength) {
		final float arX = ActiveRenderInfo.getRotationX();
		final float arZ = ActiveRenderInfo.getRotationZ();
		final float arYZ = ActiveRenderInfo.getRotationYZ();
		final float arXY = ActiveRenderInfo.getRotationXY();
		final float arXZ = ActiveRenderInfo.getRotationXZ();

		Entity e = EntityUtils.getRenderViewEntity();
		if (e == null) {
			e = PlayerUtils.getPlayer();
		}
		final double iPX = e.prevPosX + (e.posX - e.prevPosX) * partialTicks;
		final double iPY = e.prevPosY + (e.posY - e.prevPosY) * partialTicks;
		final double iPZ = e.prevPosZ + (e.posZ - e.prevPosZ) * partialTicks;

		final Vector3 v1 = new Vector3(-arX * scale - arYZ * scale, -arXZ * scale, -arZ * scale - arXY * scale);
		final Vector3 v2 = new Vector3(-arX * scale + arYZ * scale, arXZ * scale, -arZ * scale + arXY * scale);
		final Vector3 v3 = new Vector3(arX * scale + arYZ * scale, arXZ * scale, arZ * scale + arXY * scale);
		final Vector3 v4 = new Vector3(arX * scale - arYZ * scale, -arXZ * scale, arZ * scale - arXY * scale);
		if (angle != 0.0F) {
			final Vector3 pvec = new Vector3(iPX, iPY, iPZ);
			final Vector3 tvec = new Vector3(px, py, pz);
			final Vector3 qvec = pvec.subtract(tvec).normalize();
			final Vector3.Quat q = Vector3.Quat.buildQuatFrom3DVector(qvec, angle);
			q.rotateWithMagnitude(v1);
			q.rotateWithMagnitude(v2);
			q.rotateWithMagnitude(v3);
			q.rotateWithMagnitude(v4);
		}
		final Tessellator t = Tessellator.getInstance();
		final BufferBuilder vb = t.getBuffer();
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.pos(px + v1.getX() - iPX, py + v1.getY() - iPY, pz + v1.getZ() - iPZ).tex(u, v + vLength).endVertex();
		vb.pos(px + v2.getX() - iPX, py + v2.getY() - iPY, pz + v2.getZ() - iPZ).tex(u + uLength, v + vLength).endVertex();
		vb.pos(px + v3.getX() - iPX, py + v3.getY() - iPY, pz + v3.getZ() - iPZ).tex(u + uLength, v).endVertex();
		vb.pos(px + v4.getX() - iPX, py + v4.getY() - iPY, pz + v4.getZ() - iPZ).tex(u, v).endVertex();
		t.draw();
	}

	public static double interpolate(final double oldP, final double newP, final float partialTicks) {
		if (oldP == newP) {
			return oldP;
		}
		return oldP + (newP - oldP) * partialTicks;
	}

	public static void renderCircleBeamPoint2Point(final Vector3 sourcePos, final Vector3 destPos, final float partialTicks, @Nonnull final int red, @Nonnull final int green, @Nonnull final int blue, @Nonnull final int alpha, final double size, @Nullable final ResourceLocation texture) {
		Entity entity = EntityUtils.getRenderViewEntity();
		if (entity == null) {
			entity = PlayerUtils.getPlayer();
		}
		final float[] colors = new float[] {
				(float) red / 255, (float) green / 255, (float) blue / 255, (float) alpha / 255
		};

		float tr = 1F;
		tr *= 0.6;
		tr *= colors[3];

		GlStateManager.pushMatrix();

		final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
		final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
		final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
		GlStateManager.translate(-x, -y, -z);
		GlStateManager.disableLighting();
		//GlStateManager.color(colors[0] * tr, colors[1] * tr, colors[2] * tr, colors[3] * tr);
		GlStateManager.color(colors[0] * tr, colors[1] * tr, colors[2] * tr, 1F);
		GlStateManager.enableBlend();
		GlStateManager.disableCull();
		//GlStateManager.depthMask(false);
		//GlStateManager.disableAlpha();
		//GlStateManager.blendFunc(SourceFactor.ONE, DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.blendFunc(SourceFactor.ONE, DestFactor.ONE_MINUS_CONSTANT_ALPHA);
		bindTexture(texture == null ? TileEntityBeaconRenderer.TEXTURE_BEACON_BEAM : texture);

		for (int i = 0; i < 180; i++) {
			renderCurrentTextureAroundAxis(Math.toRadians(i), sourcePos, destPos, size);
		}

		//renderCurrentTextureAroundAxis(Math.toRadians(120F), sourcePos, destPos, size);
		//renderCurrentTextureAroundAxis(Math.toRadians(240F), sourcePos, destPos, size);

		GlStateManager.enableAlpha();
		GlStateManager.color(1F, 1F, 1F, 1F);
		//GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	private static void renderCurrentTextureAroundAxis(final double angle, final Vector3 sourcePos, final Vector3 destPos, final double size) {
		final Vector3 aim = destPos.copy().subtract(sourcePos);
		final Vector3 aimPerp = aim.copy().perpendicular().normalize();
		final Vector3 perp = aimPerp.copy().rotate(angle, aim).normalize();
		final Vector3 perpFrom = perp.copy().multiply(size);
		final Vector3 perpTo = perp.multiply(size);

		final Tessellator tes = Tessellator.getInstance();
		final BufferBuilder buf = tes.getBuffer();
		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		final double u = 0;
		final double v = 0;
		final double uWidth = 1;
		final double vHeight = 16;

		Vector3 vec = sourcePos.copy().add(perpFrom.copy().multiply(-1));
		buf.pos(vec.getX(), vec.getY(), vec.getZ()).tex(u, v + vHeight).endVertex();
		vec = sourcePos.copy().add(perpFrom);
		buf.pos(vec.getX(), vec.getY(), vec.getZ()).tex(u + uWidth, v + vHeight).endVertex();
		vec = destPos.copy().add(perpTo);
		buf.pos(vec.getX(), vec.getY(), vec.getZ()).tex(u + uWidth, v + vHeight).endVertex();
		vec = destPos.copy().add(perpTo.copy().multiply(-1));
		buf.pos(vec.getX(), vec.getY(), vec.getZ()).tex(u, v).endVertex();

		tes.draw();
	}

	public static Vector3[] sideVec = new Vector3[] {
			new Vector3(0, -1, 0), new Vector3(0, 1, 0), new Vector3(0, 0, -1), new Vector3(0, 0, 1), new Vector3(-1, 0, 0), new Vector3(1, 0, 0)
	};
	public static Vector3[] sidePos = new Vector3[] {
			new Vector3(0.5, 0, 0.5), new Vector3(0.5, 1, 0.5), new Vector3(0.5, 0.5, 0), new Vector3(0.5, 0.5, 1), new Vector3(0, 0.5, 0.5), new Vector3(1, 0.5, 0.5)
	};

	public static void renderSpiral(final TextureAtlasSprite tex, final int src, final int dst, final double start, final double end, final double time, final double theta0, final double x, final double y, final double z) {

		final Tessellator tes = Tessellator.getInstance();
		final BufferBuilder vertexBuffer = tes.getBuffer();
		vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		//BufferBuilder vertexBuffer = ccrs.startDrawing(7, DefaultVertexFormats.POSITION_TEX);
		vertexBuffer.setTranslation(x, y, z);

		Vector3[] last = new Vector3[] {
				new Vector3(), new Vector3(), new Vector3(), new Vector3()
		};
		Vector3[] next = new Vector3[] {
				new Vector3(), new Vector3(), new Vector3(), new Vector3()
		};
		final double tess = 0.05;

		final Vector3 a = getPerp(src, dst);
		final boolean rev = sum(a.copy().crossProduct(getPathNormal(src, dst, 0))) != sum(sideVec[src]);

		for (double di = end; di <= start; di += tess) {
			final Vector3 b = getPathNormal(src, dst, di);
			final Vector3 c = getPath(src, dst, di);

			if (rev) {
				b.negate();
			}

			final double r = (2 * di - time / 10 + theta0 + dst / 6) * 2 * Math.PI;
			final double sz = 0.1;
			final Vector3 p = c.add(a.copy().multiply(MathUtils.sin((float) r) * sz)).add(b.copy().multiply(MathUtils.cos((float) r) * sz));

			final double s1 = 0.02;
			final double s2 = -0.02;
			next[0].set(p).add(a.x * s1 + b.x * s1, a.y * s1 + b.y * s1, a.z * s1 + b.z * s1);
			next[1].set(p).add(a.x * s2 + b.x * s1, a.y * s2 + b.y * s1, a.z * s2 + b.z * s1);
			next[2].set(p).add(a.x * s2 + b.x * s2, a.y * s2 + b.y * s2, a.z * s2 + b.z * s2);
			next[3].set(p).add(a.x * s1 + b.x * s2, a.y * s1 + b.y * s2, a.z * s1 + b.z * s2);

			if (di > end) {
				final double u1 = tex.getInterpolatedU(Math.abs(di) * 16);
				final double u2 = tex.getInterpolatedU(Math.abs(di - tess) * 16);
				for (int i = 0; i < 4; i++) {
					final int j = (i + 1) % 4;
					final Vector3 axis = next[j].copy().subtract(next[i]);
					final double v1 = tex.getInterpolatedV(Math.abs(next[i].scalarProject(axis)) * 16);
					final double v2 = tex.getInterpolatedV(Math.abs(next[j].scalarProject(axis)) * 16);

					vertexBuffer.pos(next[i].x, next[i].y, next[i].z).tex(u1, v1).endVertex();
					vertexBuffer.pos(next[j].x, next[j].y, next[j].z).tex(u1, v2).endVertex();
					vertexBuffer.pos(last[j].x, last[j].y, last[j].z).tex(u2, v2).endVertex();
					vertexBuffer.pos(last[i].x, last[i].y, last[i].z).tex(u2, v1).endVertex();
				}
			}

			final Vector3[] tmp = last;
			last = next;
			next = tmp;
		}

		tes.draw();
		vertexBuffer.setTranslation(0, 0, 0);
	}

	private static double sum(final Vector3 v) {
		return v.x + v.y + v.z;
	}

	public static Vector3 getPerp(final int src, final int dst) {
		if ((src ^ 1) == dst) {
			return sideVec[(src + 2) % 6].copy();
		}

		for (int i = 0; i < 3; i++) {
			if (i != src / 2 && i != dst / 2) {
				return sideVec[i * 2].copy();
			}
		}

		return null;
	}

	public static Vector3 getPath(final int src, final int dst, final double d) {
		Vector3 v;
		if ((src ^ 1) == dst)//opposite
		{
			v = sideVec[src ^ 1].copy().multiply(d);
		}
		else {
			final Vector3 vsrc = sideVec[src ^ 1];
			final Vector3 vdst = sideVec[dst ^ 1];
			final Vector3 a = vsrc.copy().multiply(5 / 16D);
			final Vector3 b = vdst.copy().multiply(6 / 16D);
			final double sind = MathUtils.sin((float) (d * Math.PI / 2));
			final double cosd = MathUtils.cos((float) (d * Math.PI / 2));
			v = a.multiply(sind).add(b.multiply(cosd - 1)).add(vsrc.copy().multiply(3 / 16D));
		}
		return v.add(sidePos[src]);
	}

	private static Vector3 getPathNormal(final int srcSide, final int dstSide, final double d) {
		if ((srcSide ^ 1) == dstSide) {
			return sideVec[(srcSide + 4) % 6].copy();
		}

		final double sind = MathUtils.sin((float) (d * Math.PI / 2));
		final double cosd = MathUtils.cos((float) (d * Math.PI / 2));

		final Vector3 vsrc = sideVec[srcSide ^ 1].copy();
		final Vector3 vdst = sideVec[dstSide ^ 1].copy();

		return vsrc.multiply(sind).add(vdst.multiply(cosd)).normalize();
	}

	/**
	 * vanilla beamRadius = 0.2D
	 * vanilla glowRadius = 0.25D
	 */
	public static void renderBeam(final TileEntity tileEntity, final float partialTicks, double length, @Nonnull final int red, @Nonnull final int blue, @Nonnull final int green, final double beamRadius, final double glowRadius, final EnumFacing... sides) {
		if (tileEntity == null || tileEntity.getWorld() == null) {
			return;
		}
		length = length < 1 ? 1 : length;
		final List<EnumFacing> sideList = Arrays.asList(sides);
		final double height = length;
		final float[] colors = new float[] {
				(float) red / 255, (float) green / 255, (float) blue / 255
		};
		final float a = 1.0F;
		final double yOffset = 0.5D;
		final BlockPos pos = tileEntity.getPos();
		final double x = pos.getX() - TileEntityRendererDispatcher.staticPlayerX;
		final double y = pos.getY() - TileEntityRendererDispatcher.staticPlayerY;
		final double z = pos.getZ() - TileEntityRendererDispatcher.staticPlayerZ;
		final double totalWorldTime = tileEntity.getWorld().getTotalWorldTime();
		final double textureScale = 1.0;
		final double i = yOffset + height;
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
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder vertexbuffer = tessellator.getBuffer();
		final double d0 = totalWorldTime + partialTicks;
		final double d1 = height < 0 ? d0 : -d0;
		final double d2 = MathHelper.frac(d1 * 0.2D - MathUtils.floor(d1 * 0.1D));
		final float f = colors[0];
		final float f1 = colors[1];
		final float f2 = colors[2];
		double d3 = d0 * 0.025D * -1.5D;
		double d4 = 0.5D + Math.cos(d3 + 2.356194490192345D) * beamRadius;
		double d5 = 0.5D + Math.sin(d3 + 2.356194490192345D) * beamRadius;
		double d6 = 0.5D + Math.cos(d3 + Math.PI / 4D) * beamRadius;
		double d7 = 0.5D + Math.sin(d3 + Math.PI / 4D) * beamRadius;
		double d8 = 0.5D + Math.cos(d3 + 3.9269908169872414D) * beamRadius;
		double d9 = 0.5D + Math.sin(d3 + 3.9269908169872414D) * beamRadius;
		double d10 = 0.5D + Math.cos(d3 + 5.497787143782138D) * beamRadius;
		double d11 = 0.5D + Math.sin(d3 + 5.497787143782138D) * beamRadius;
		//double d12 = 0.0D;
		double d13 = 1.0D;
		double d14 = -1.0D + d2;
		final double d15 = height * textureScale * (0.5D / beamRadius) + d14;
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
		//d12 = 1.0D;
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

	public static void renderBeamNoGlow(final TileEntity tileEntity, final float partialTicks, int length, @Nonnull final int red, @Nonnull final int blue, @Nonnull final int green, @Nonnull final int beamAlpha, final double beamRadius, final EnumFacing... sides) {
		if (tileEntity == null || tileEntity.getWorld() == null) {
			return;
		}
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/entity/beacon_beam.png"));
		final List<EnumFacing> sideList = Arrays.asList(sides);
		length = length < 1 ? 1 : length;
		final int height = length;
		final float[] colors = new float[] {
				(float) red / 255, (float) green / 255, (float) blue / 255
		};
		final float a = (float) beamAlpha / 255;
		final double yOffset = 0.5;
		//double partialTicks = 0.0D;
		final BlockPos pos = tileEntity.getPos();
		final double x = pos.getX() - TileEntityRendererDispatcher.staticPlayerX;
		final double y = pos.getY() - TileEntityRendererDispatcher.staticPlayerY;
		final double z = pos.getZ() - TileEntityRendererDispatcher.staticPlayerZ;
		final double totalWorldTime = tileEntity.getWorld().getTotalWorldTime();
		final double textureScale = 1.0;
		final double i = yOffset + height;

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
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder vertexbuffer = tessellator.getBuffer();
		final double d0 = totalWorldTime + partialTicks;
		final double d1 = height < 0 ? d0 : -d0;
		final double d2 = MathHelper.frac(d1 * 0.2D - MathUtils.floor(d1 * 0.1D));
		final float f = colors[0];
		final float f1 = colors[1];
		final float f2 = colors[2];
		final double d3 = d0 * 0.025D * -1.5D;
		final double d4 = 0.5D + Math.cos(d3 + 2.356194490192345D) * beamRadius;
		final double d5 = 0.5D + Math.sin(d3 + 2.356194490192345D) * beamRadius;
		final double d6 = 0.5D + Math.cos(d3 + Math.PI / 4D) * beamRadius;
		final double d7 = 0.5D + Math.sin(d3 + Math.PI / 4D) * beamRadius;
		final double d8 = 0.5D + Math.cos(d3 + 3.9269908169872414D) * beamRadius;
		final double d9 = 0.5D + Math.sin(d3 + 3.9269908169872414D) * beamRadius;
		final double d10 = 0.5D + Math.cos(d3 + 5.497787143782138D) * beamRadius;
		final double d11 = 0.5D + Math.sin(d3 + 5.497787143782138D) * beamRadius;
		//double d12 = 0.0D;
		//double d13 = 1.0D;
		final double d14 = -1.0D + d2;
		final double d15 = height * textureScale * (0.5D / beamRadius) + d14;
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

	public static int renderTextureSize = 128;
	public static int framebufferID = -1;
	public static int depthbufferID = -1;
	public static int textureID = -1;

	private static IntBuffer lastViewport;
	private static int lastTexture;
	private static int lastFramebuffer;

	public static void pushFBO() {
		pushFBO(512);
	}

	public static void pushFBO(final int size) {
		renderTextureSize = size;
		GL30.glDeleteFramebuffers(framebufferID);
		GL11.glDeleteTextures(textureID);
		GL30.glDeleteRenderbuffers(depthbufferID);

		framebufferID = GL30.glGenFramebuffers();
		textureID = GL11.glGenTextures();
		final int currentFramebuffer = GL11.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING);
		final int currentTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebufferID);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, renderTextureSize, renderTextureSize, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, currentTexture);
		depthbufferID = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthbufferID);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, renderTextureSize, renderTextureSize);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthbufferID);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, textureID, 0);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, currentFramebuffer);

		lastFramebuffer = GL11.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebufferID);
		lastViewport = GLAllocation.createDirectIntBuffer(16);
		GL11.glGetInteger(GL11.GL_VIEWPORT, lastViewport);
		GL11.glViewport(0, 0, renderTextureSize, renderTextureSize);
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		lastTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		GlStateManager.clearColor(0, 0, 0, 0);
		GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GlStateManager.enableDepth();
		//GlStateManager.depthFunc(GL11.GL_ALWAYS);
		GlStateManager.enableLighting();
		GlStateManager.enableRescaleNormal();
	}

	public static void popFBO() {
		GlStateManager.disableDepth();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableLighting();
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.popMatrix();
		GL11.glViewport(lastViewport.get(0), lastViewport.get(1), lastViewport.get(2), lastViewport.get(3));
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, lastFramebuffer);
		GlStateManager.bindTexture(lastTexture);
	}

	public static void saveImage() {
		try {
			ImageUtils.IMAGE_DIR.mkdir();
			final File imageFile = getTimestampedPNGFile(ImageUtils.IMAGE_DIR);
			GlStateManager.bindTexture(textureID);
			GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			final int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
			final int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
			final IntBuffer texture = BufferUtils.createIntBuffer(width * height);
			GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, texture);
			final int[] texture_array = new int[width * height];
			texture.get(texture_array);
			final BufferedImage image = new BufferedImage(renderTextureSize, renderTextureSize, BufferedImage.TYPE_INT_ARGB);
			image.setRGB(0, 0, renderTextureSize, renderTextureSize, texture_array, 0, width);
			final AffineTransform flip = AffineTransform.getScaleInstance(1, -1);
			flip.translate(0, -renderTextureSize);
			final BufferedImage flipped = new AffineTransformOp(flip, null).filter(image, null);
			ImageIO.write(flipped, "png", imageFile);
		}
		catch (final IOException e) {
		}
	}

	private static File getTimestampedPNGFile(final File saveDir) {
		final String s = DATE_FORMAT.format(new Date()).toString();
		int i = 1;
		while (true) {
			final File file1 = new File(saveDir, s + (i == 1 ? "" : "_" + i) + ".png");
			if (!file1.exists()) {
				return file1;
			}
			++i;
		}
	}

	public static void renderEntity(final String entityName, final NBTTagCompound entityNBT, final int x, final int y) {
		if (entityName != null && !entityName.isEmpty()) {
			Entity entity = null;
			if (entityNBT != null) {
				entity = EntityList.createEntityFromNBT(entityNBT, EasyMappings.world());
			}
			else {
				final int id = EntityList.getID(EntityList.getClass(new ResourceLocation(entityName)));
				final Class<? extends Entity> clazz = EntityList.getClassFromID(id);
				try {
					entity = clazz.getConstructor(World.class).newInstance(EasyMappings.world());
				}
				catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				}
			}
			if (entity != null) {
				renderEntity(x, y, entity);
			}
		}
	}

	private static void renderEntity(final int x, final int y, final Entity entity) {
		renderEntity(entity, x, y, entity.height);
	}

	public static void renderEntity(final Entity entity, final int xPos, final int yPos, final float scale) {
		renderEntity(entity, xPos, yPos, scale, 0.0f);
	}

	public static void renderLivingEntity(final int posX, final int posY, final int scale, final float mouseX, final float mouseY, final EntityLivingBase ent) {
		renderLivingEntity(posX, posY, scale, mouseX, mouseY, ent, false);
	}

	public static void renderLivingEntity(final int posX, final int posY, final int scale, final float mouseX, final float mouseY, final EntityLivingBase ent, final boolean useMouseRot) {
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate(posX, posY, 50.0F);
		GlStateManager.scale(-scale, scale, scale);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		if (useMouseRot) {
			GlStateManager.rotate(-((float) Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
			ent.renderYawOffset = (float) Math.atan(mouseX / 40.0F) * 20.0F;
			ent.rotationYaw = (float) Math.atan(mouseX / 40.0F) * 40.0F;
			ent.rotationPitch = -((float) Math.atan(mouseY / 40.0F)) * 20.0F;
		}
		else {
			GlStateManager.rotate(-((float) Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
			ent.renderYawOffset = mouseX;
			ent.rotationYaw = mouseX;
			ent.rotationPitch = 0;
		}
		ent.rotationYawHead = ent.rotationYaw;
		ent.prevRotationYawHead = ent.rotationYaw;
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		rendermanager.setRenderShadow(true);
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public static void renderEntity(final Entity entity, final int xPos, final int yPos, final float scale, final float rot) {
		GlStateManager.pushMatrix();
		GlStateManager.color(1f, 1f, 1f);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate(xPos + 8, yPos + 24, 50F);
		GlStateManager.scale(-scale, scale, scale);
		GlStateManager.rotate(180F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(135F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(rot, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
		entity.rotationPitch = 0.0F;
		GlStateManager.translate(0.0F, (float) entity.getYOffset(), 0.0F);
		Minecraft.getMinecraft().getRenderManager().playerViewY = 180F;
		try {
			Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		}
		catch (final Exception e) {
		}
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableLighting();
		GlStateManager.popMatrix();
		GlStateManager.enableDepth();
		GlStateManager.disableColorMaterial();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public static void renderScaledItemStack(final ItemStack stack, final int x, final int y, final float scale) {
		renderScaledItemStack(stack, x, y, scale);
	}

	public static void renderScaledItemStack(final ItemStack stack, final int x, final int y, final float scale, float rotation) {//broken
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.translate(x * -5 / scale, y * -5 / scale, 0.0);
		GlStateManager.scale(scale, scale, 1.0F);

		GlStateManager.rotate(rotation, 0.0f, 1.0f, 0.0f);
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableDepth();
		getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
		GlStateManager.rotate(--rotation, 0.0f, 1.0f, 0.0f);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popMatrix();
	}

	public static void drawTextRGBA(final FontRenderer font, final String s, final int x, final int y, final int r, final int g, final int b, final int a) {
		font.drawString(s, x, y, (a << 24) + (r << 16) + (g << 8) + b);
	}

	public static Vec3i hexToRGB(final int hex) {
		return new Vec3i(hex >> 16 & 0xFF, hex >> 8 & 0xFF, hex >> 0 & 0xFF);
	}

	public static Quat hexToRGBA(final int hex) {
		final Color c = new Color(hex);
		return new Quat(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}

}
