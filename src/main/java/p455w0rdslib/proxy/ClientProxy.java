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
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import p455w0rdslib.LibKeyBindings;
import p455w0rdslib.handlers.ProcessHandlerClient;

/**
 * @author p455w0rd
 *
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(final FMLPreInitializationEvent e) {
		super.preInit(e);
		ProcessHandlerClient.init();
		LibKeyBindings.register();
	}

	@Override
	public World getWorld() {
		return FMLClientHandler.instance().getWorldClient();
	}

	@Override
	public EntityPlayer getPlayer() {
		return FMLClientHandler.instance().getClientPlayerEntity();
	}

	@Override
	public EntityPlayer getPlayer(final MessageContext context) {
		return context.side.isClient() ? getPlayer() : null;
	}

	@Override
	public boolean isClientSide() {
		return true;
	}
}
