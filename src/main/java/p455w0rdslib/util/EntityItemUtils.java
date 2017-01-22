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

import net.minecraft.entity.item.EntityItem;

/**
 * @author p455w0rd
 *
 */
public class EntityItemUtils {

	public static boolean canPickup(EntityItem item) {
		return !item.cannotPickup();
	}

	public static String getThrowerName(EntityItem item) {
		return item.getThrower();
	}

	public static String getOwnerName(EntityItem item) {
		return item.getOwner();
	}

	public static void setThrowerName(EntityItem item, String thrower) {
		item.setThrower(thrower);
	}

	public static void setOwnerName(EntityItem item, String owner) {
		item.setOwner(owner);
	}
}
