package p455w0rdslib.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;
import com.google.common.io.Resources;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import p455w0rdslib.LibGlobals;
import p455w0rdslib.LibRegistry;

/**
 * Utility for fetching online player names and UUIDs<br>
 * Calls performed on separate thread
 *
 * @author p455w0rd
 *
 */
public class PlayerUUIDUtils {

	public static String getPlayerName(final UUID uuid) throws InterruptedException, ExecutionException {
		return LibGlobals.THREAD_POOL.submit(() -> fetchPlayerName(uuid)).get();
	};

	public static UUID getPlayerUUID(final String name) throws InterruptedException, ExecutionException {
		return LibGlobals.THREAD_POOL.submit(() -> fetchPlayerUUID(name)).get();
	};

	public static EntityPlayer getPlayerFromWorld(World world, UUID player) {
		if (player == null || world == null) {
			return null;
		}
		return world.getPlayerEntityByUUID(player);
	}

	private static String fetchPlayerName(final UUID uuid) throws IOException {
		if (LibRegistry.getNameRegistry().containsKey(uuid)) {
			return LibRegistry.getNameRegistry().get(uuid);
		}
		if (!ProxiedUtils.isSMP()) {
			if (ProxiedUtils.isClientSide() && ProxiedUtils.getWorld() != null && ProxiedUtils.getPlayer() != null) {
				String name = ProxiedUtils.getPlayer().getName();
				LibRegistry.registerName(uuid, name);
				return name;
			}
		}
		String USERNAME_API_URL = "https://api.mojang.com/user/profiles/%s/names";
		CharMatcher DASH_MATCHER = CharMatcher.is('-');
		String uuidString = DASH_MATCHER.removeFrom(uuid.toString());
		try (BufferedReader reader = Resources.asCharSource(new URL(String.format(USERNAME_API_URL, uuidString)), StandardCharsets.UTF_8).openBufferedStream()) {
			JsonReader json = new JsonReader(reader);
			json.beginArray();

			String name = null;
			long when = 0;

			while (json.hasNext()) {
				String nameObj = null;
				long timeObj = 0;
				json.beginObject();
				while (json.hasNext()) {
					String key = json.nextName();
					switch (key) {
					case "name":
						nameObj = json.nextString();
						break;
					case "changedToAt":
						timeObj = json.nextLong();
						break;
					default:
						json.skipValue();
						break;
					}
				}
				json.endObject();

				if (nameObj != null && timeObj >= when) {
					name = nameObj;
				}
			}

			json.endArray();
			json.close();
			name = name == null ? "" : name;
			LibRegistry.registerName(uuid, name);
			return name;
		}
	}

	private static UUID fetchPlayerUUID(final String name) {
		if (LibRegistry.getUUIDRegistry().containsKey(name)) {
			return LibRegistry.getUUIDRegistry().get(name);
		}
		if (!ProxiedUtils.isSMP()) {
			if (ProxiedUtils.isClientSide() && ProxiedUtils.getWorld() != null && ProxiedUtils.getPlayer() != null) {
				UUID uuid = ProxiedUtils.getPlayer().getUniqueID();
				LibRegistry.registerUUID(name, uuid);
				return uuid;
			}
		}
		if (!Strings.isNullOrEmpty(name)) {
			try {
				URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setUseCaches(false);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				JsonObject profile = (JsonObject) (new JsonParser()).parse(new InputStreamReader(connection.getInputStream()));
				UUID uuid = UUID.fromString(fullUUID(profile.get("id").toString()));
				LibRegistry.registerUUID(name, uuid);
				return uuid;
			}
			catch (Exception e) {
			}
		}
		return null;
	}

	private static String fullUUID(String uuid) {
		uuid = uuid.replaceAll("[^a-zA-Z0-9]", "");
		uuid = (uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32));
		return uuid;
	}

}
