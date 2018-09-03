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
package p455w0rdslib.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import p455w0rdslib.LibConfig;
import p455w0rdslib.LibEntities;
import p455w0rdslib.LibEvents;
import p455w0rdslib.capabilities.CapabilityChunkLoader;
import p455w0rdslib.handlers.ProcessHandler;

/**
 * @author p455w0rd
 *
 */
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		LibConfig.init();
		//ForgeChunkManager.setForcedChunkLoadingCallback(P455w0rdsLib.INSTANCE, Callback.getInstance());
		CapabilityChunkLoader.init();
		if (FMLCommonHandler.instance().getSide().isServer()) {
			ProcessHandler.init();
		}
	}

	public void init(FMLInitializationEvent e) {
		LibEntities.init();
		MinecraftForge.EVENT_BUS.register(new LibEvents());
	}

	public boolean isSMP() {
		return FMLCommonHandler.instance().getMinecraftServerInstance() != null && FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer();
	}

	public World getWorld() {
		return null;
	}

	public EntityPlayer getPlayer() {
		return null;
	}

	public EntityPlayer getPlayer(MessageContext context) {
		return null;
	}

	public boolean isClientSide() {
		return FMLCommonHandler.instance().getSide() == Side.CLIENT;
	}

	public Object getServer() {
		return FMLCommonHandler.instance().getMinecraftServerInstance();
	}

}
