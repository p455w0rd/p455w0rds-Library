package p455w0rdslib.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import net.minecraft.launchwrapper.Launch;

/**
 * Safe class writer.
 * The way COMPUTE_FRAMES works may require loading additional classes. This can cause ClassCircularityErrors.
 * The override for getCommonSuperClass will ensure that COMPUTE_FRAMES works properly by using the right ClassLoader.
 * <p>
 * Code from: https://github.com/JamiesWhiteShirt/clothesline/blob/master/src/core/java/com/jamieswhiteshirt/clothesline/core/SafeClassWriter.java
 */
public class SafeClassWriter extends ClassWriter {
	public SafeClassWriter(final int flags) {
		super(flags);
	}

	public SafeClassWriter(final ClassReader classReader, final int flags) {
		super(classReader, flags);
	}

	@Override
	protected String getCommonSuperClass(final String type1, final String type2) {
		Class<?> c, d;
		final ClassLoader classLoader = Launch.classLoader;
		try {
			c = Class.forName(type1.replace('/', '.'), false, classLoader);
			d = Class.forName(type2.replace('/', '.'), false, classLoader);
		}
		catch (final Exception e) {
			throw new RuntimeException(e.toString());
		}
		if (c.isAssignableFrom(d)) {
			return type1;
		}
		if (d.isAssignableFrom(c)) {
			return type2;
		}
		if (c.isInterface() || d.isInterface()) {
			return "java/lang/Object";
		}
		else {
			do {
				c = c.getSuperclass();
			}
			while (!c.isAssignableFrom(d));
			return c.getName().replace('.', '/');
		}
	}

}