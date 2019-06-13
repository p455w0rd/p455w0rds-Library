package p455w0rdslib;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * @author p455w0rd
 *
 */
public class LibKeyBindings {

	private static final String CATEGORY = "key.categories.p455w0rdslib";
	public static final KeyBinding TOGGLE_SHADERS = new KeyBinding("key.toggle_shaders", Keyboard.CHAR_NONE, CATEGORY);

	public static void register() {
		ClientRegistry.registerKeyBinding(TOGGLE_SHADERS);
	}

}
