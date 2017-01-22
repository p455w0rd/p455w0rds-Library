/*
 * This file is part of p455w0rd's Library.
 * Copyright (c) 2016, p455w0rd (aka TheRealp455w0rd), All rights reserved
 * unless
 * otherwise stated.
 *
 * p455w0rd's Library is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * p455w0rd's Library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * MIT License for more details.
 *
 * You should have received a copy of the MIT License
 * along with p455w0rd's Library. If not, see
 * <https://opensource.org/licenses/MIT>.
 */
package p455w0rdslib.util;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * This class is has methods which use ATs and/or Reflection<br>
 * to make dealing with private/private final mc methods<br>
 * easier.
 *
 * @author p455w0rd
 *
 */
public class MCPrivateUtils {

	public static void addResourcePack(IResourcePack pack) {
		List<Object> packList = ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), ReflectionUtils.determineSRG("defaultResourcePacks"));
		packList.add(pack);
		ReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.getMinecraft(), packList, ReflectionUtils.determineSRG("defaultResourcePacks"));
	}

	public static List<LayerRenderer<AbstractClientPlayer>> getLayerRenderers(RenderPlayer renderPlayer) {
		return ReflectionHelper.getPrivateValue(RenderLivingBase.class, renderPlayer, ReflectionUtils.determineSRG("layerRenderers"));
	}

	public static void setLayerRenderers(List<LayerRenderer<EntityPlayer>> layers, RenderPlayer renderPlayer) {
		ReflectionHelper.setPrivateValue(RenderLivingBase.class, renderPlayer, layers, ReflectionUtils.determineSRG("layerRenderers"));
	}

	public static int getTextureOffsetX(ModelRenderer modelRenderer) {
		return ReflectionHelper.getPrivateValue(ModelRenderer.class, modelRenderer, ReflectionUtils.determineSRG("textureOffsetX"));
	}

	public static int getTextureOffsetY(ModelRenderer modelRenderer) {
		return ReflectionHelper.getPrivateValue(ModelRenderer.class, modelRenderer, ReflectionUtils.determineSRG("textureOffsetY"));
	}

	public static TexturedQuad[] getQuadList(ModelBox modelBox) {
		return ReflectionHelper.getPrivateValue(ModelBox.class, modelBox, ReflectionUtils.determineSRG("quadList"));
	}

	public static DataParameter<Boolean> getEndermanScreaming(EntityEnderman enderman) {
		return ReflectionHelper.getPrivateValue(EntityEnderman.class, enderman, ReflectionUtils.determineSRG("SCREAMING"));
	}

	public static void setEndermanScreaming(EntityEnderman enderman, boolean isScreaming) {
		EntityDataManager dm = ReflectionHelper.getPrivateValue(Entity.class, enderman, ReflectionUtils.determineSRG("dataManager"));
		DataParameter<Boolean> screaming = getEndermanScreaming(enderman);
		dm.set(screaming, Boolean.valueOf(isScreaming));
		dm.setDirty(screaming);
		ReflectionHelper.setPrivateValue(Entity.class, enderman, dm, ReflectionUtils.determineSRG("dataManager"));
	}

}
