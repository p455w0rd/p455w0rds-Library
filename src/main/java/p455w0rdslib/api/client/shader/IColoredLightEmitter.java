package p455w0rdslib.api.client.shader;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IColoredLightEmitter {

	@SideOnly(Side.CLIENT)
	default void emitLight(final List<Light> lights, final Entity entity) {
	}

	@SideOnly(Side.CLIENT)
	default void emitLight(final List<Light> lights, final TileEntity tile) {
	}

}
