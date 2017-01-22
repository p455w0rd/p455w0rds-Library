package p455w0rdslib.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * @author p455w0rd
 *
 */
public class ReflectionUtils {

	public static Map<String, String> SRGList() {
		Map<String, String> list = Maps.newHashMap();
		list.put("defaultResourcePacks", "field_110449_ao");
		list.put("layerRenderers", "field_177097_h");
		list.put("textureOffsetX", "field_78803_o");
		list.put("textureOffsetY", "field_78813_p");
		list.put("quadList", "field_78254_i");
		list.put("SCREAMING", "field_184719_bw");
		list.put("dataManager", "field_70180_af");
		list.put("capabilities", "capabilities");
		return list;
	}

	public static String getSRG(String deobf) {
		return SRGList().containsKey(deobf) ? SRGList().get(deobf) : "";
	}

	public static String determineSRG(String string) {
		return MCUtils.isDeobf() ? string : getSRG(string);
	}

	public static <T> MethodHandle findMethod(Class<T> clazz, String[] methodNames, Class<?>... methodTypes) {
		Method method = ReflectionHelper.findMethod(clazz, null, methodNames, methodTypes);
		try {
			return MethodHandles.lookup().unreflect(method);
		}
		catch (IllegalAccessException e) {
			throw new ReflectionHelper.UnableToFindMethodException(methodNames, e);
		}
	}

	public static MethodHandle findFieldGetter(Class<?> clazz, String... fieldNames) {
		Field field = ReflectionHelper.findField(clazz, fieldNames);
		try {
			return MethodHandles.lookup().unreflectGetter(field);
		}
		catch (IllegalAccessException e) {
			throw new ReflectionHelper.UnableToAccessFieldException(fieldNames, e);
		}
	}

	public static MethodHandle findFieldSetter(Class<?> clazz, String... fieldNames) {
		Field field = ReflectionHelper.findField(clazz, fieldNames);
		try {
			return MethodHandles.lookup().unreflectSetter(field);
		}
		catch (IllegalAccessException e) {
			throw new ReflectionHelper.UnableToAccessFieldException(fieldNames, e);
		}
	}

}