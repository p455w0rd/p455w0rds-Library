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

import java.util.*;

import com.google.common.collect.BiMap;
import com.google.common.collect.ListMultimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry.EntityRegistration;
import net.minecraftforge.registries.IRegistryDelegate;

/**
 * This class is has methods which use ATs and/or Reflection<br>
 * to make dealing with private/private final mc methods<br>
 * easier.
 *
 * @author p455w0rd
 *
 */
public class MCPrivateUtils {

	public static void addResourcePack(final IResourcePack pack) {
		final List<Object> packList = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), ReflectionUtils.determineSRG("defaultResourcePacks"));
		packList.add(pack);
		ObfuscationReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.getMinecraft(), packList, ReflectionUtils.determineSRG("defaultResourcePacks"));
	}

	public static List<LayerRenderer<? extends EntityLivingBase>> getLayerRenderers(final RenderLivingBase<?> renderPlayer) {
		return ObfuscationReflectionHelper.getPrivateValue(RenderLivingBase.class, renderPlayer, ReflectionUtils.determineSRG("layerRenderers"));
	}

	public static void setLayerRenderers(final List<LayerRenderer<EntityPlayer>> layers, final RenderPlayer renderPlayer) {
		ObfuscationReflectionHelper.setPrivateValue(RenderLivingBase.class, renderPlayer, layers, ReflectionUtils.determineSRG("layerRenderers"));
	}

	public static int getTextureOffsetX(final ModelRenderer modelRenderer) {
		return ObfuscationReflectionHelper.getPrivateValue(ModelRenderer.class, modelRenderer, ReflectionUtils.determineSRG("textureOffsetX"));
	}

	public static int getTextureOffsetY(final ModelRenderer modelRenderer) {
		return ObfuscationReflectionHelper.getPrivateValue(ModelRenderer.class, modelRenderer, ReflectionUtils.determineSRG("textureOffsetY"));
	}

	public static TexturedQuad[] getQuadList(final ModelBox modelBox) {
		return ObfuscationReflectionHelper.getPrivateValue(ModelBox.class, modelBox, ReflectionUtils.determineSRG("quadList"));
	}

	public static DataParameter<Boolean> getEndermanScreaming(final EntityEnderman enderman) {
		return ObfuscationReflectionHelper.getPrivateValue(EntityEnderman.class, enderman, ReflectionUtils.determineSRG("SCREAMING"));
	}

	public static void setEndermanScreaming(final EntityEnderman enderman, final boolean isScreaming) {
		final EntityDataManager dm = ObfuscationReflectionHelper.getPrivateValue(Entity.class, enderman, ReflectionUtils.determineSRG("dataManager"));
		final DataParameter<Boolean> screaming = getEndermanScreaming(enderman);
		dm.set(screaming, Boolean.valueOf(isScreaming));
		dm.setDirty(screaming);
		ObfuscationReflectionHelper.setPrivateValue(Entity.class, enderman, dm, ReflectionUtils.determineSRG("dataManager"));
	}

	public static float getRenderItemZLevel(final RenderItem ri) {
		return ObfuscationReflectionHelper.getPrivateValue(RenderItem.class, ri, ReflectionUtils.determineZLevelSRG("zLevel", RenderItem.class));
	}

	public static void setRenderItemZLevel(final RenderItem ri, final float zLevel) {
		ObfuscationReflectionHelper.setPrivateValue(RenderItem.class, ri, zLevel, ReflectionUtils.determineZLevelSRG("zLevel", RenderItem.class));
	}

	public static float getGuiZLevel(final Gui gui) {
		return ObfuscationReflectionHelper.getPrivateValue(Gui.class, gui, ReflectionUtils.determineZLevelSRG("zLevel", Gui.class));
	}

	public static void setGuiZLevel(final Gui gui, final float zLevel) {
		ObfuscationReflectionHelper.setPrivateValue(Gui.class, gui, zLevel, ReflectionUtils.determineZLevelSRG("zLevel", Gui.class));
	}

	public static RenderItem getGuiScreenRenderItem(final GuiScreen gui) {
		return ObfuscationReflectionHelper.getPrivateValue(GuiScreen.class, gui, ReflectionUtils.determineSRG("itemRender"));
	}

	public static void setGuiScreenRendererZLevel(final GuiScreen gui, final float zLevel) {
		ObfuscationReflectionHelper.setPrivateValue(RenderItem.class, getGuiScreenRenderItem(gui), zLevel, ReflectionUtils.determineZLevelSRG("zLevel", RenderItem.class));
	}

	public static boolean getGuiDragSplitting(final GuiContainer gui) {
		return ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, ReflectionUtils.determineSRG("dragSplitting"));
	}

	public static Set<Slot> getGuiDragSplittingSlots(final GuiContainer gui) {
		return ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, ReflectionUtils.determineSRG("dragSplittingSlots"));
	}

	public static int getGuiDragSplittingLimit(final GuiContainer gui) {
		return ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, ReflectionUtils.determineSRG("dragSplittingLimit"));
	}

	public static Slot getGuiClickedSlot(final GuiContainer gui) {
		return ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, ReflectionUtils.determineSRG("clickedSlot"));
	}

	public static ItemStack getGuiDraggedStack(final GuiContainer gui) {
		return ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, ReflectionUtils.determineSRG("draggedStack"));
	}

	public static boolean getGuiIsRightMouseClick(final GuiContainer gui) {
		return ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, ReflectionUtils.determineSRG("isRightMouseClick"));
	}

	public static int getGuiDragSplittingRemnant(final GuiContainer gui) {
		return ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, ReflectionUtils.determineSRG("dragSplittingRemnant"));
	}

	public static void setGuiDragSplittingRemnant(final GuiContainer gui, final int amount) {
		ObfuscationReflectionHelper.setPrivateValue(GuiContainer.class, gui, amount, ReflectionUtils.determineSRG("dragSplittingRemnant"));
	}

	public static CapabilityDispatcher getItemStackCapabilities(final ItemStack stack) {
		return ObfuscationReflectionHelper.getPrivateValue(ItemStack.class, stack, "capabilities");
	}

	public static void setItemStackCapabilities(final ItemStack stack, final CapabilityDispatcher dispatcher) {
		ObfuscationReflectionHelper.setPrivateValue(ItemStack.class, stack, dispatcher, "capabilities");
	}

	public static NBTTagCompound getItemStackCapNBT(final ItemStack stack) {
		return ObfuscationReflectionHelper.getPrivateValue(ItemStack.class, stack, "capNBT");
	}

	public static void setItemStackItem(final ItemStack stack, final Item item) {
		ObfuscationReflectionHelper.setPrivateValue(ItemStack.class, stack, item, ReflectionUtils.determineSRG("item"));
	}

	public static void setItemStackDelegate(final ItemStack stack, final IRegistryDelegate<Item> delegate) {
		ObfuscationReflectionHelper.setPrivateValue(ItemStack.class, stack, delegate, "delegate");
	}

	public static int getGuiContainerXSize(final GuiContainer gui) {
		return ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, ReflectionUtils.determineSRG("xSize"));
	}

	public static int getGuiContainerYSize(final GuiContainer gui) {
		return ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, ReflectionUtils.determineSRG("ySize"));
	}

	public static float getRainfall(final Biome biome) {
		return ObfuscationReflectionHelper.getPrivateValue(Biome.class, biome, ReflectionUtils.determineSRG("rainfall"));
	}

	public static void setRainfall(final Biome biome, final float rainfall) {
		ObfuscationReflectionHelper.setPrivateValue(Biome.class, biome, rainfall, ReflectionUtils.determineSRG("rainfall"));
	}

	public static boolean isRainEnabled(final Biome biome) {
		return ObfuscationReflectionHelper.getPrivateValue(Biome.class, biome, ReflectionUtils.determineSRG("enableRain"));
	}

	public static void setRainEnabled(final Biome biome, final boolean enableRain) {
		ObfuscationReflectionHelper.setPrivateValue(Biome.class, biome, enableRain, ReflectionUtils.determineSRG("enableRain"));
	}

	public static BiMap<Class<? extends Entity>, EntityRegistration> getEntityClassRegistry() {
		return ObfuscationReflectionHelper.getPrivateValue(EntityRegistry.class, EntityRegistry.instance(), "entityClassRegistrations");
	}

	public static Map<String, ModContainer> getEntityNameRegistry() {
		return ObfuscationReflectionHelper.getPrivateValue(EntityRegistry.class, EntityRegistry.instance(), "entityNames");
	}

	public static ListMultimap<ModContainer, EntityRegistration> getEntityRegistration() {
		return ObfuscationReflectionHelper.getPrivateValue(EntityRegistry.class, EntityRegistry.instance(), "entityRegistrations");
	}

	public static void setLastDamageSource(final EntityLivingBase entity, final DamageSource source) {
		ObfuscationReflectionHelper.setPrivateValue(EntityLivingBase.class, entity, source, ReflectionUtils.determineSRG("lastDamageSource"));
	}

	public static void setLastDamageStamp(final EntityLivingBase entity, final long time) {
		ObfuscationReflectionHelper.setPrivateValue(EntityLivingBase.class, entity, time, ReflectionUtils.determineSRG("lastDamageStamp"));
	}

	public static List<Entity> getUnloadedEntityList(final World world) {
		return ObfuscationReflectionHelper.getPrivateValue(World.class, world, ReflectionUtils.determineSRG("unloadedEntityList"));
	}

	public static BlockPos getEntityLastPortalPos(final Entity entity) {
		return ObfuscationReflectionHelper.getPrivateValue(Entity.class, entity, ReflectionUtils.determineSRG("lastPortalPos"));
	}

	public static void setEntityLastPortalPos(final Entity entity, final BlockPos pos) {
		ObfuscationReflectionHelper.setPrivateValue(Entity.class, entity, pos, ReflectionUtils.determineSRG("lastPortalPos"));
	}

	public static Vec3d getEntityLastPortalVec(final Entity entity) {
		return ObfuscationReflectionHelper.getPrivateValue(Entity.class, entity, ReflectionUtils.determineSRG("lastPortalVec"));
	}

	public static void setEntityLastPortalVec(final Entity entity, final Vec3d vector) {
		ObfuscationReflectionHelper.setPrivateValue(Entity.class, entity, vector, ReflectionUtils.determineSRG("lastPortalVec"));
	}

	public static EnumFacing getEntityTeleportDirection(final Entity entity) {
		return ObfuscationReflectionHelper.getPrivateValue(Entity.class, entity, ReflectionUtils.determineSRG("teleportDirection"));
	}

	public static void setEntityTeleportDirection(final Entity entity, final EnumFacing direction) {
		ObfuscationReflectionHelper.setPrivateValue(Entity.class, entity, direction, ReflectionUtils.determineSRG("teleportDirection"));
	}

	public static IntHashMap<Entity> getEntitiesForWorld(final World world) {
		return ObfuscationReflectionHelper.getPrivateValue(World.class, world, ReflectionUtils.determineSRG("entitiesById"));
	}

}
