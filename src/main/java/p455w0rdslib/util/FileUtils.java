package p455w0rdslib.util;

import java.io.IOException;
import java.io.InputStream;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * @author p455w0rd
 *
 */
public class FileUtils {

	public static String readAsset(final String modid, final String fileName) {
		try {
			final ByteSource bs = new ByteSource() {
				@Override
				public InputStream openStream() throws IOException {
					return Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(modid, fileName)).getInputStream();
				}
			};
			return bs.asCharSource(Charsets.UTF_8).read();
		}
		catch (final IOException e) {
			throw new RuntimeException("Failed to read " + fileName, e);
		}
	}

}
