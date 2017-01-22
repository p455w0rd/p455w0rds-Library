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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import p455w0rdslib.P455w0rdsLib;

/**
 * @author p455w0rd
 *
 */
public class ProxiedUtils {

	public static boolean isSMP() {
		return P455w0rdsLib.PROXY.isSMP();
	}

	public static World getWorld() {
		return P455w0rdsLib.PROXY.getWorld();
	}

	public static EntityPlayer getPlayer() {
		return P455w0rdsLib.PROXY.getPlayer();
	}

	public static boolean isClientSide() {
		return P455w0rdsLib.PROXY.isClientSide();
	}

	public static MinecraftServer getServer() {
		return (MinecraftServer) P455w0rdsLib.PROXY.getServer();
	}

}
