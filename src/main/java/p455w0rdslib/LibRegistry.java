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
package p455w0rdslib;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;

/**
 * @author p455w0rd
 *
 */
public class LibRegistry {

	private static Map<UUID, String> NAME_REGISTRY = Maps.newHashMap();
	private static Map<String, UUID> UUID_REGISTRY = Maps.newHashMap();
	private static Map<String, ItemStack> SKULL_REGISTRY = Maps.newHashMap();

	public static Map<String, ItemStack> getSkullRegistry() {
		return SKULL_REGISTRY;
	}

	public static Map<UUID, String> getNameRegistry() {
		return NAME_REGISTRY;
	}

	public static Map<String, UUID> getUUIDRegistry() {
		return UUID_REGISTRY;
	}

	public static String getPlayerName(UUID uuid) {
		return NAME_REGISTRY.get(uuid);
	}

	public static boolean registerName(UUID uuid, String name) {
		boolean hasChanged = false;
		if (!NAME_REGISTRY.containsKey(uuid)) {
			NAME_REGISTRY.put(uuid, name);
			hasChanged = true;
		}
		if (!UUID_REGISTRY.containsKey(name)) {
			UUID_REGISTRY.put(name, uuid);
		}
		return hasChanged;
	}

	public static void registerUUID(String name, UUID uuid) {
		registerName(uuid, name);
	}

	public static void clearNameRegistry() {
		NAME_REGISTRY = new HashMap<UUID, String>();
		UUID_REGISTRY = new HashMap<String, UUID>();
	}
}
