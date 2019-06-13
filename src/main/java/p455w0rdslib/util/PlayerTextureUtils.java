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

import java.util.Map;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;

/**
 * @author p455w0rd
 *
 */
public class PlayerTextureUtils {

	private static NetworkPlayerInfo getPlayerInfo(final AbstractClientPlayer player) {
		return Minecraft.getMinecraft().getConnection().getPlayerInfo(player.getUniqueID());
	}

	private static Map<Type, ResourceLocation> getPlayerTextures(final AbstractClientPlayer player) {
		return getPlayerInfo(player).playerTextures;
	}

	public static ResourceLocation getCape(final AbstractClientPlayer player) {
		return getPlayerTextures(player).get(MinecraftProfileTexture.Type.CAPE);
	}

	public static ResourceLocation getElytra(final AbstractClientPlayer player) {
		return getPlayerTextures(player).get(MinecraftProfileTexture.Type.ELYTRA);
	}

	public static ResourceLocation getSkin(final AbstractClientPlayer player) {
		return getPlayerTextures(player).get(MinecraftProfileTexture.Type.SKIN);
	}

	public static void setCape(final AbstractClientPlayer player, final ResourceLocation texture) {
		getPlayerTextures(player).put(MinecraftProfileTexture.Type.CAPE, texture);
	}

	public static void setElytra(final AbstractClientPlayer player, final ResourceLocation texture) {
		getPlayerTextures(player).put(MinecraftProfileTexture.Type.ELYTRA, texture);
	}

	public static void setSkin(final AbstractClientPlayer player, final ResourceLocation texture) {
		getPlayerTextures(player).put(MinecraftProfileTexture.Type.SKIN, texture);
	}

	public static boolean hasCape(final AbstractClientPlayer player) {
		return getCape(player) != null;
	}

	public static boolean hasElytra(final AbstractClientPlayer player) {
		return getElytra(player) != null;
	}

	public static boolean hasSkin(final AbstractClientPlayer player) {
		return getSkin(player) != null;
	}

}
