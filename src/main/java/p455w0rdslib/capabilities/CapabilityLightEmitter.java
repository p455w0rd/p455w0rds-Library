package p455w0rdslib.capabilities;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import p455w0rdslib.LibGlobals;
import p455w0rdslib.api.client.shader.Light;
import p455w0rdslib.util.CapabilityUtils.EmptyStorage;

/**
 * @author p455w0rd
 *
 */
public class CapabilityLightEmitter {

	public static final ResourceLocation CAP_LOCATION = new ResourceLocation(LibGlobals.MODID, "lights_cap");

	@CapabilityInject(ILightEmitter.class)
	public static Capability<ILightEmitter> LIGHT_EMITTER_CAPABILITY = null;

	private static final IStorage<ILightEmitter> STORAGE = new EmptyStorage<>();

	public static void register() {
		CapabilityManager.INSTANCE.register(ILightEmitter.class, STORAGE, DummyLightEmitter::new);
	}

	private static class DummyLightEmitter implements ILightEmitter {

		@Override
		public void emitLight(final List<Light> lights) {
		}

	}

	public static class EntityLightEmitter implements ILightEmitter {

		Entity entity;

		public EntityLightEmitter(final Entity entity) {
			this.entity = entity;
		}

		@Override
		public void emitLight(final List<Light> lights) {
		}

	}

	public static class EntityProvider implements ICapabilityProvider {

		ILightEmitter emitter;

		public EntityProvider(final Entity entity) {
			emitter = new EntityLightEmitter(entity);
		}

		@Override
		public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {
			return capability == LIGHT_EMITTER_CAPABILITY;
		}

		@Override
		public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
			return hasCapability(capability, facing) ? LIGHT_EMITTER_CAPABILITY.cast(emitter) : null;
		}

	}

	/*public static class StackProvider implements ICapabilityProvider {

		ILightEmitter emitter;

		public StackProvider(final ItemStack stack) {
			emitter = lights -> stack.getCapability(CapabilityLightEmitter.LIGHT_EMITTER_CAPABILITY, null).emitLight(lights);
		}

		@Override
		public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {
			return capability == LIGHT_EMITTER_CAPABILITY;
		}

		@Override
		public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
			return hasCapability(capability, facing) ? LIGHT_EMITTER_CAPABILITY.cast(emitter) : null;
		}

	}*/

	public interface ILightEmitter {

		void emitLight(List<Light> lights);

	}

}
