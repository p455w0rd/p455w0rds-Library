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

import java.lang.invoke.MethodHandle;
import java.util.Map;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;

/**
 * @author p455w0rd
 *
 */
public class PlayerTextureUtils {

	private static final MethodHandle GET_PLAYER_INFO = ReflectionUtils.findMethod(AbstractClientPlayer.class, "getPlayerInfo", "func_175155_b", new Class[0]);
	private static final MethodHandle GET_PLAYER_TEXTURES = ReflectionUtils.findFieldGetter(NetworkPlayerInfo.class, "playerTextures", "field_187107_a");

	private static NetworkPlayerInfo getPlayerInfo(AbstractClientPlayer player) {
		try {
			return (NetworkPlayerInfo) GET_PLAYER_INFO.invoke(player);
		}
		catch (Throwable e) {
		}
		return null;
	}

	private static Map<Type, ResourceLocation> getPlayerTextures(AbstractClientPlayer player) {
		try {
			return (Map<Type, ResourceLocation>) GET_PLAYER_TEXTURES.invoke(getPlayerInfo(player));
		}
		catch (Throwable e) {
		}
		return null;
	}

	public static ResourceLocation getCape(AbstractClientPlayer player) {
		return getPlayerTextures(player).get(MinecraftProfileTexture.Type.CAPE);
	}

	public static ResourceLocation getElytra(AbstractClientPlayer player) {
		return getPlayerTextures(player).get(MinecraftProfileTexture.Type.ELYTRA);
	}

	public static ResourceLocation getSkin(AbstractClientPlayer player) {
		return getPlayerTextures(player).get(MinecraftProfileTexture.Type.SKIN);
	}

	public static void setCape(AbstractClientPlayer player, ResourceLocation texture) {
		getPlayerTextures(player).put(MinecraftProfileTexture.Type.CAPE, texture);
	}

	public static void setElytra(AbstractClientPlayer player, ResourceLocation texture) {
		getPlayerTextures(player).put(MinecraftProfileTexture.Type.ELYTRA, texture);
	}

	public static void setSkin(AbstractClientPlayer player, ResourceLocation texture) {
		getPlayerTextures(player).put(MinecraftProfileTexture.Type.SKIN, texture);
	}

	public static boolean hasCape(AbstractClientPlayer player) {
		return getCape(player) != null;
	}

	public static boolean hasElytra(AbstractClientPlayer player) {
		return getElytra(player) != null;
	}

	public static boolean hasSkin(AbstractClientPlayer player) {
		return getSkin(player) != null;
	}

}
