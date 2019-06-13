package p455w0rdslib.handlers;

import java.util.*;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

/**
 * @author p455w0rd
 *
 */

public class BrightnessHandler {

	private static Map<TileEntity, BrightnessHandler> TILE_BRIGHTNESS_HANDLERS = new HashMap<>();
	private static Map<Entity, BrightnessHandler> ENTITY_BRIGHTNESS_HANDLERS = new HashMap<>();

	public static final int[] STEPS_MOST = new int[] {
			850, 700, 775, 900, 800
	};
	public static final int[] STEPS_LEAST = new int[] {
			500, 475, 625, 700, 575
	};

	public static final float[] RADIUS_MOST = new float[] {
			2.0f, 2.5f, 2.1f, 1.9f, 2.5f
	};

	public static final float[] RADIUS_LEAST = new float[] {
			1.85f, 2.0f, 1.9f, 1.75f, 2.2f
	};

	private int brightness = 0;
	private boolean brightnessDir = false;
	private int step = 0;
	private boolean initLight = false;
	private final Random r = new Random();

	public BrightnessHandler() {
		step = r.nextInt(4);
		tick();
	}

	public static void tickAllHandlers() {
		for (final Map.Entry<TileEntity, BrightnessHandler> tileHandler : TILE_BRIGHTNESS_HANDLERS.entrySet()) {
			tileHandler.getValue().tick();
		}
		for (final Map.Entry<Entity, BrightnessHandler> entityHandler : ENTITY_BRIGHTNESS_HANDLERS.entrySet()) {
			entityHandler.getValue().tick();
		}
	}

	public static BrightnessHandler getBrightness(final TileEntity tile) {
		if (!TILE_BRIGHTNESS_HANDLERS.containsKey(tile)) {
			TILE_BRIGHTNESS_HANDLERS.put(tile, new BrightnessHandler());
		}
		return TILE_BRIGHTNESS_HANDLERS.get(tile);
	}

	public static BrightnessHandler getBrightness(final Entity entity) {
		if (!ENTITY_BRIGHTNESS_HANDLERS.containsKey(entity)) {
			ENTITY_BRIGHTNESS_HANDLERS.put(entity, new BrightnessHandler());
		}
		return ENTITY_BRIGHTNESS_HANDLERS.get(entity);
	}

	public int value() {
		//tick();
		return brightness;
	}

	public void tick() {
		if (!initLight) {
			step = r.nextInt(4);
			initLight = true;
		}
		if (brightnessDir) {
			brightness += 30;
			if (brightness > STEPS_MOST[step]) {
				brightnessDir = !brightnessDir;
				step++;
				if (step > 4) {
					step = 0;
				}
			}
		}
		else {
			brightness -= 30;
			if (brightness < STEPS_LEAST[step]) {
				brightnessDir = !brightnessDir;
				step++;
				if (step > 4) {
					step = 0;
				}
			}
		}
	}

	public void reset() {
		brightness = 0;
		brightnessDir = false;
		initLight = false;
	}

}
