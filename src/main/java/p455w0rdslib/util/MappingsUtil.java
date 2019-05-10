package p455w0rdslib.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

import p455w0rdslib.asm.FMLPlugin;

public class MappingsUtil {
	//private static Map<String, String> fields;
	//private static Map<String, String> methods;

	private static Map<String, String> methods_mcp2srg = new HashMap<>();
	private static Map<String, String> methods_srg2mcp = new HashMap<>();
	private static Map<String, String> fields_mcp2srg = new HashMap<>();
	private static Map<String, String> fields_srg2mcp = new HashMap<>();

	private static List<String> mcpMethods = new ArrayList<>();
	private static List<String> srgMethods = new ArrayList<>();
	private static List<String> mcpFields = new ArrayList<>();
	private static List<String> srgFields = new ArrayList<>();

	static {
		if (mcp()) {
			readMethodMappings();
			readFieldMappings();
		}
	}

	private static final String getMappingsDir() {
		try {
			final Class<?> gradleClass = Class.forName("net.minecraftforge.gradle.GradleStartCommon");
			final Field dirField = gradleClass.getDeclaredField("CSV_DIR");
			dirField.setAccessible(true);
			final File mappingDir = (File) dirField.get(null);
			return mappingDir.getAbsolutePath() + "/";
		}
		catch (final Exception e) {
			e.printStackTrace();
			System.out.println("[p455w0rdc0re] [ERROR] Error getting mappings from Gradlew SRG, falling back to mcp folder.");
			return "./../mcp/";
		}
	}

	public static final boolean mcp() {
		return FMLPlugin.isDeobf;
	}

	public static final String getField(final String name) {
		if (mcp()) {
			if (isSRG(name)) {
				return fields_srg2mcp.get(name);
			}
		}
		else {
			if (isMCP(name)) {
				return fields_mcp2srg.get(name);
			}
		}
		return name;
	}

	public static final String getMethod(final String name) {
		if (mcp()) {
			if (isSRG(name)) {
				return methods_srg2mcp.get(name);
			}
		}
		else {
			if (isMCP(name)) {
				return methods_mcp2srg.get(name);
			}
		}
		return name;
	}

	private static boolean isSRG(final String str) {
		return srgMethods.contains(str) || srgFields.contains(str);
	}

	private static boolean isMCP(final String str) {
		return mcpMethods.contains(str) || mcpFields.contains(str);
	}

	private static void readMethodMappings() {
		final Map<String, String> map = readMappings(new File(getMappingsDir() + "methods.csv"));
		for (final Map.Entry<String, String> entry : map.entrySet()) {
			srgMethods.add(entry.getKey());
			mcpMethods.add(entry.getValue());
			methods_srg2mcp.put(entry.getKey(), entry.getValue());
			methods_mcp2srg.put(entry.getValue(), entry.getKey());
		}
	}

	private static void readFieldMappings() {
		final Map<String, String> map = readMappings(new File(getMappingsDir() + "fields.csv"));
		for (final Map.Entry<String, String> entry : map.entrySet()) {
			srgFields.add(entry.getKey());
			mcpFields.add(entry.getValue());
			fields_srg2mcp.put(entry.getKey(), entry.getValue());
			fields_mcp2srg.put(entry.getValue(), entry.getKey());
		}
	}

	private static final Map<String, String> readMappings(final File file) {
		if (!file.isFile()) {
			throw new RuntimeException("Couldn't find MCP mappings.");
		}
		try {
			return Files.readLines(file, Charsets.UTF_8, new MCPFileParser());
		}
		catch (final IOException e) {
			throw new RuntimeException("Couldn't read SRG->MCP mappings", e);
		}
	}

	private static class MCPFileParser implements LineProcessor<Map<String, String>> {
		private static final Splitter splitter = Splitter.on(',').trimResults();
		private final Map<String, String> map = Maps.newHashMap();
		private boolean foundFirst;

		@Override
		public boolean processLine(final String line) throws IOException {
			if (!foundFirst) {
				foundFirst = true;
				return true;
			}

			final Iterator<String> splitted = splitter.split(line).iterator();
			try {
				final String srg = splitted.next();
				final String mcp = splitted.next();
				if (!map.containsKey(srg)) {
					map.put(srg, mcp);
				}
			}
			catch (final NoSuchElementException e) {
				throw new IOException("Invalid Mappings file!", e);
			}

			return true;
		}

		@Override
		public Map<String, String> getResult() {
			return ImmutableMap.copyOf(map);
		}
	}

}