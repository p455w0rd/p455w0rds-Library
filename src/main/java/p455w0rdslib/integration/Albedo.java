package p455w0rdslib.integration;

import elucent.albedo.event.GatherLightsEvent;
import elucent.albedo.lighting.DefaultLightProvider;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.FMLCommonHandler;
import p455w0rdslib.LibGlobals.Mods;
import p455w0rdslib.capabilities.CapabilityLightEmitter;

/**
 * @author p455w0rd
 *
 */
public class Albedo {

	public static boolean albedoCapCheck(final Capability<?> capability) {
		return FMLCommonHandler.instance().getSide().isClient() && Mods.ALBEDO.isLoaded() && isAlbedoCap(capability);
	}

	private static boolean isAlbedoCap(final Capability<?> capability) {
		return capability == elucent.albedo.Albedo.LIGHT_PROVIDER_CAPABILITY;
	}

	public static <T> T getVanillaStackCapability(final ItemStack stack) {
		if (CapabilityLightEmitter.getColorForStack(stack).getLeft() != 0x0) {
			return elucent.albedo.Albedo.LIGHT_PROVIDER_CAPABILITY.cast(new DefaultLightProvider() {
				@Override
				public void gatherLights(final GatherLightsEvent event, final Entity entity) {
					event.add(elucent.albedo.lighting.Light.builder().pos(entity.posX, entity.posY - 0.5d, entity.posZ).color(CapabilityLightEmitter.getColorForStack(stack).getLeft(), true).radius(CapabilityLightEmitter.getColorForStack(stack).getRight().getRight()).build());
				}
			});
		}
		return null;
	}

	public static ICapabilityProvider getVanillaStackProvider(final ItemStack stack) {

		return new ICapabilityProvider() {

			@Override
			public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {
				return albedoCapCheck(capability);
			}

			@Override
			public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
				return hasCapability(capability, null) ? getVanillaStackCapability(stack) : null;
			}

		};

	}

	public static ICapabilityProvider getEmptyProvider() {
		return new ICapabilityProvider() {

			@Override
			public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {
				return albedoCapCheck(capability);
			}

			@Override
			public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
				return hasCapability(capability, facing) ? getEmptyCapability() : null;
			}

		};
	}

	public static <T> T getEmptyCapability() {
		return elucent.albedo.Albedo.LIGHT_PROVIDER_CAPABILITY.cast(new DefaultLightProvider());
	}

}
