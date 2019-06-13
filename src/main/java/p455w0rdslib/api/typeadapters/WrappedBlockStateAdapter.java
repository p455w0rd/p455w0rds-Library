package p455w0rdslib.api.typeadapters;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import net.minecraft.util.ResourceLocation;

/**
 *
 * From https://github.com/MightyPirates/BedrockOres/blob/master-MC1.12/src/main/java/li/cil/bedrockores/common/json/WrappedBlockStateAdapter.java
 *
 */
public class WrappedBlockStateAdapter implements JsonSerializer<WrappedBlockState>, JsonDeserializer<WrappedBlockState> {

	private static final String KEY_NAME = "name";
	private static final String KEY_PROPERTIES = "properties";

	// --------------------------------------------------------------------- //
	// JsonSerializer

	@Override
	public JsonElement serialize(final WrappedBlockState src, final Type typeOfSrc, final JsonSerializationContext context) {
		final JsonObject result = new JsonObject();
		result.add(KEY_NAME, context.serialize(src.getName()));
		final Map<String, String> properties = src.getProperties();
		if (properties != null && !properties.isEmpty()) {
			result.add(KEY_PROPERTIES, context.serialize(properties, Types.MAP_STRING_STRING));
		}
		return result;
	}

	// --------------------------------------------------------------------- //
	// JsonDeserializer

	@Override
	public WrappedBlockState deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		final JsonObject jsonObject = json.getAsJsonObject();
		final ResourceLocation name = context.deserialize(jsonObject.get(KEY_NAME), ResourceLocation.class);
		final Map<String, String> properties;
		if (jsonObject.has(KEY_PROPERTIES)) {
			properties = context.deserialize(jsonObject.get(KEY_PROPERTIES), Types.MAP_STRING_STRING);
		}
		else {
			properties = null;
		}
		return new WrappedBlockState(name, properties);
	}

	public static final class Types {

		public static final Type LIST_STRING;
		public static final Type MAP_STRING_STRING;

		private Types() {
		}

		static {
			LIST_STRING = new TypeToken<List<String>>() {
			}.getType();
			MAP_STRING_STRING = new TypeToken<Map<String, String>>() {
			}.getType();
		}
	}

}