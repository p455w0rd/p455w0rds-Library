package p455w0rdslib.util;

/**
 * @author p455w0rd
 *
 */
@SuppressWarnings("deprecation")
public class ReflectionUtils {

	/*public static Map<String, String> SRGList() {
		final Map<String, String> list = Maps.newHashMap();
		list.put("defaultResourcePacks", "field_110449_ao");
		list.put("layerRenderers", "field_177097_h");
		list.put("textureOffsetX", "field_78803_o");
		list.put("textureOffsetY", "field_78813_p");
		list.put("quadList", "field_78254_i");
		list.put("SCREAMING", "field_184719_bw");
		list.put("dataManager", "field_70180_af");
		list.put("capabilities", "capabilities");
		list.put("itemRender", "field_146296_j");
		list.put("dragSplitting", "field_147007_t");
		list.put("dragSplittingSlots", "field_147008_s");
		list.put("dragSplittingLimit", "field_146987_F");
		list.put("clickedSlot", "field_147005_v");
		list.put("draggedStack", "field_147012_x");
		list.put("isRightMouseClick", "field_147004_w");
		list.put("dragSplittingRemnant", "field_146996_I");
		list.put("item", "field_151002_e");
		list.put("xSize", "field_146999_f");
		list.put("ySize", "field_147000_g");
		list.put("rainfall", "field_76751_G");
		list.put("enableRain", "field_76765_S");
		list.put("lastDamageSource", "field_189750_bF");
		list.put("lastDamageStamp", "field_189751_bG");
		list.put("unloadedEntityList", "field_72997_g");
		list.put("lastPortalPos", "field_181016_an");
		list.put("lastPortalVec", "field_181017_ao");
		list.put("teleportDirection", "field_181018_ap");
		list.put("entitiesById", "field_175729_l");
		return list;
	}
	
	public static Map<Class<?>, String> zLevel() {
		final Map<Class<?>, String> list = Maps.newHashMap();
		list.put(Gui.class, "field_73735_i");
		list.put(RenderItem.class, "field_77023_b");
		return list;
	}
	
	public static String getSRG(final String deobf) {
		return SRGList().containsKey(deobf) ? SRGList().get(deobf) : "";
	}
	
	public static String determineSRG(final String string) {
		return MCUtils.isDeobf() ? string : getSRG(string);
	}
	
	public static String determineZLevelSRG(final String string, final Class<?> clazz) {
		return MCUtils.isDeobf() ? string : zLevel().containsKey(clazz) ? zLevel().get(clazz) : "";
	}
	
	public static <T> MethodHandle findMethod(final Class<T> clazz, final String methodName, final String obfMethodName, final Class<?>... methodTypes) {
		final Method method = ReflectionHelper.findMethod(clazz, methodName, obfMethodName, methodTypes);
		try {
			return MethodHandles.lookup().unreflect(method);
		}
		catch (final IllegalAccessException e) {
			throw new ReflectionHelper.UnableToFindMethodException(e);
		}
	}
	
	public static MethodHandle findFieldGetter(final Class<?> clazz, final String... fieldNames) {
		final Field field = ReflectionHelper.findField(clazz, fieldNames);
		try {
			return MethodHandles.lookup().unreflectGetter(field);
		}
		catch (final IllegalAccessException e) {
			throw new ReflectionHelper.UnableToAccessFieldException(fieldNames, e);
		}
	}
	
	public static MethodHandle findFieldSetter(final Class<?> clazz, final String... fieldNames) {
		final Field field = ReflectionHelper.findField(clazz, fieldNames);
		try {
			return MethodHandles.lookup().unreflectSetter(field);
		}
		catch (final IllegalAccessException e) {
			throw new ReflectionHelper.UnableToAccessFieldException(fieldNames, e);
		}
	}*/

}