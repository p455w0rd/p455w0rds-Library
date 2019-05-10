package p455w0rdslib.util;

import java.lang.reflect.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockPressurePlate.Sensitivity;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.gen.structure.StructureStrongholdPieces.Stronghold.Door;
import net.minecraftforge.classloading.FMLForgePlugin;
import net.minecraftforge.fml.common.EnhancedRuntimeException;
import p455w0rdslib.LibGlobals;

/**
 * @author p455w0rd
 *
 */
public class EnumUtils {
	private static Object reflectionFactory = null;
	private static Method newConstructorAccessor = null;
	private static Method newInstance = null;
	private static Method newFieldAccessor = null;
	private static Method fieldAccessorSet = null;
	private static boolean isSetup = false;

	//Some enums are decompiled with extra arguments, so lets check for that
	private static Class<?>[][] commonTypes = {
			{
					EnumAction.class
			}, {
					ArmorMaterial.class, String.class, int.class, int[].class, int.class, SoundEvent.class, float.class
			}, {
					EnumArt.class, String.class, int.class, int.class, int.class, int.class
			}, {
					EnumCreatureAttribute.class
			}, {
					EnumCreatureType.class, Class.class, int.class, Material.class, boolean.class, boolean.class
			}, {
					Door.class
			}, {
					EnumEnchantmentType.class
			}, {
					Sensitivity.class
			}, {
					RayTraceResult.Type.class
			}, {
					EnumSkyBlock.class, int.class
			}, {
					SleepResult.class
			}, {
					ToolMaterial.class, int.class, int.class, float.class, float.class, int.class
			}, {
					EnumRarity.class, TextFormatting.class, String.class
			}
	};

	public static EnumAction addAction(final String name) {
		return addEnum(EnumAction.class, name);
	}

	public static ArmorMaterial addArmorMaterial(final String name, final String textureName, final int durability, final int[] reductionAmounts, final int enchantability, final SoundEvent soundOnEquip, final float toughness) {
		return addEnum(ArmorMaterial.class, name, textureName, durability, reductionAmounts, enchantability, soundOnEquip, toughness);
	}

	public static EnumArt addArt(final String name, final String tile, final int sizeX, final int sizeY, final int offsetX, final int offsetY) {
		return addEnum(EnumArt.class, name, tile, sizeX, sizeY, offsetX, offsetY);
	}

	public static EnumCreatureAttribute addCreatureAttribute(final String name) {
		return addEnum(EnumCreatureAttribute.class, name);
	}

	public static EnumCreatureType addCreatureType(final String name, final Class<?> typeClass, final int maxNumber, final Material material, final boolean peaceful, final boolean animal) {
		return addEnum(EnumCreatureType.class, name, typeClass, maxNumber, material, peaceful, animal);
	}

	public static Door addDoor(final String name) {
		return addEnum(Door.class, name);
	}

	public static EnumEnchantmentType addEnchantmentType(final String name) {
		return addEnum(EnumEnchantmentType.class, name);
	}

	public static Sensitivity addSensitivity(final String name) {
		return addEnum(Sensitivity.class, name);
	}

	public static RayTraceResult.Type addMovingObjectType(final String name) {
		return addEnum(RayTraceResult.Type.class, name);
	}

	public static EnumSkyBlock addSkyBlock(final String name, final int lightValue) {
		return addEnum(EnumSkyBlock.class, name, lightValue);
	}

	public static SleepResult addStatus(final String name) {
		return addEnum(SleepResult.class, name);
	}

	public static ToolMaterial addToolMaterial(final String name, final int harvestLevel, final int maxUses, final float efficiency, final float damage, final int enchantability) {
		return addEnum(ToolMaterial.class, name, harvestLevel, maxUses, efficiency, damage, enchantability);
	}

	public static EnumRarity addRarity(final String name, final TextFormatting color, final String displayName) {
		return addEnum(EnumRarity.class, name, color, displayName);
	}

	private static void setup() {
		if (isSetup) {
			return;
		}

		try {
			final Method getReflectionFactory = Class.forName("sun.reflect.ReflectionFactory").getDeclaredMethod("getReflectionFactory");
			reflectionFactory = getReflectionFactory.invoke(null);
			newConstructorAccessor = Class.forName("sun.reflect.ReflectionFactory").getDeclaredMethod("newConstructorAccessor", Constructor.class);
			newInstance = Class.forName("sun.reflect.ConstructorAccessor").getDeclaredMethod("newInstance", Object[].class);
			newFieldAccessor = Class.forName("sun.reflect.ReflectionFactory").getDeclaredMethod("newFieldAccessor", Field.class, boolean.class);
			fieldAccessorSet = Class.forName("sun.reflect.FieldAccessor").getDeclaredMethod("set", Object.class, Object.class);
		}
		catch (final Exception e) {
			e.printStackTrace();
		}

		isSetup = true;
	}

	/*
	 * Everything below this is found at the site below, and updated to be able to compile in Eclipse/Java 1.6+
	 * Also modified for use in decompiled code.
	 * Found at: http://niceideas.ch/roller2/badtrash/entry/java_create_enum_instances_dynamically
	 */
	private static Object getConstructorAccessor(final Class<?> enumClass, final Class<?>[] additionalParameterTypes) throws Exception {
		final Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
		parameterTypes[0] = String.class;
		parameterTypes[1] = int.class;
		System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
		return newConstructorAccessor.invoke(reflectionFactory, enumClass.getDeclaredConstructor(parameterTypes));
	}

	private static <T extends Enum<?>> T makeEnum(final Class<T> enumClass, final String value, final int ordinal, final Class<?>[] additionalTypes, final Object[] additionalValues) throws Exception {
		final Object[] params = new Object[additionalValues.length + 2];
		params[0] = value;
		params[1] = ordinal;
		System.arraycopy(additionalValues, 0, params, 2, additionalValues.length);
		return enumClass.cast(newInstance.invoke(getConstructorAccessor(enumClass, additionalTypes), new Object[] {
				params
		}));
	}

	public static void setFailsafeFieldValue(final Field field, final Object target, final Object value) throws Exception {
		field.setAccessible(true);
		final Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		final Object fieldAccessor = newFieldAccessor.invoke(reflectionFactory, field, false);
		fieldAccessorSet.invoke(fieldAccessor, target, value);
	}

	private static void blankField(final Class<?> enumClass, final String fieldName) throws Exception {
		for (final Field field : Class.class.getDeclaredFields()) {
			if (field.getName().contains(fieldName)) {
				field.setAccessible(true);
				setFailsafeFieldValue(field, enumClass, null);
				break;
			}
		}
	}

	private static void cleanEnumCache(final Class<?> enumClass) throws Exception {
		blankField(enumClass, "enumConstantDirectory");
		blankField(enumClass, "enumConstants");
	}

	public static <T extends Enum<?>> T addEnum(final Class<T> enumType, final String enumName, final Object... paramValues) {
		setup();
		return addEnum(commonTypes, enumType, enumName, paramValues);
	}

	public static <T extends Enum<?>> T addEnum(final Class<?>[][] map, final Class<T> enumType, final String enumName, final Object... paramValues) {
		for (final Class<?>[] lookup : map) {
			if (lookup[0] == enumType) {
				final Class<?>[] paramTypes = new Class<?>[lookup.length - 1];
				if (paramTypes.length > 0) {
					System.arraycopy(lookup, 1, paramTypes, 0, paramTypes.length);
				}
				return addEnum(enumType, enumName, paramTypes, paramValues);
			}
		}
		return null;
	}

	//Tests an enum is compatible with these args, throws an error if not.
	public static void testEnum(final Class<? extends Enum<?>> enumType, final Class<?>[] paramTypes) {
		addEnum(true, enumType, null, paramTypes, (Object[]) null);
	}

	public static <T extends Enum<?>> T addEnum(final Class<T> enumType, final String enumName, final Class<?>[] paramTypes, final Object... paramValues) {
		return addEnum(false, enumType, enumName, paramTypes, paramValues);
	}

	@SuppressWarnings({
			"unchecked", "serial"
	})
	public static <T extends Enum<?>> T addEnum(final boolean test, final Class<T> enumType, final String enumName, final Class<?>[] paramTypes, final Object[] paramValues) {
		if (!isSetup) {
			setup();
		}

		Field valuesField = null;
		final Field[] fields = enumType.getDeclaredFields();

		for (final Field field : fields) {
			final String name = field.getName();
			if (name.equals("$VALUES") || name.equals("ENUM$VALUES")) //Added 'ENUM$VALUES' because Eclipse's internal compiler doesn't follow standards
			{
				valuesField = field;
				break;
			}
		}

		final int flags = (FMLForgePlugin.RUNTIME_DEOBF ? Modifier.PUBLIC : Modifier.PRIVATE) | Modifier.STATIC | Modifier.FINAL | 0x1000 /*SYNTHETIC*/;
		if (valuesField == null) {
			final String valueType = String.format("[L%s;", enumType.getName().replace('.', '/'));

			for (final Field field : fields) {
				if ((field.getModifiers() & flags) == flags && field.getType().getName().replace('.', '/').equals(valueType)) //Apparently some JVMs return .'s and some don't..
				{
					valuesField = field;
					break;
				}
			}
		}

		if (valuesField == null) {
			final List<String> lines = Lists.newArrayList();
			lines.add(String.format("Could not find $VALUES field for enum: %s", enumType.getName()));
			lines.add(String.format("Runtime Deobf: %s", FMLForgePlugin.RUNTIME_DEOBF));
			lines.add(String.format("Flags: %s", String.format("%16s", Integer.toBinaryString(flags)).replace(' ', '0')));
			lines.add("Fields:");
			for (final Field field : fields) {
				final String mods = String.format("%16s", Integer.toBinaryString(field.getModifiers())).replace(' ', '0');
				lines.add(String.format("       %s %s: %s", mods, field.getName(), field.getType().getName()));
			}

			for (final String line : lines) {
				LogManager.getLogger(LibGlobals.MODID).error(line);
			}

			if (test) {
				throw new EnhancedRuntimeException("Could not find $VALUES field for enum: " + enumType.getName()) {
					@Override
					protected void printStackTrace(final WrappedPrintStream stream) {
						for (final String line : lines) {
							stream.println(line);
						}
					}
				};
			}
			return null;
		}

		if (test) {
			Object ctr = null;
			Exception ex = null;
			try {
				ctr = getConstructorAccessor(enumType, paramTypes);
			}
			catch (final Exception e) {
				ex = e;
			}
			if (ctr == null || ex != null) {
				throw new EnhancedRuntimeException(String.format("Could not find constructor for Enum %s", enumType.getName()), ex) {
					private String toString(final Class<?>[] cls) {
						final StringBuilder b = new StringBuilder();
						for (int x = 0; x < cls.length; x++) {
							b.append(cls[x].getName());
							if (x != cls.length - 1) {
								b.append(", ");
							}
						}
						return b.toString();
					}

					@Override
					protected void printStackTrace(final WrappedPrintStream stream) {
						stream.println("Target Arguments:");
						stream.println("    java.lang.String, int, " + toString(paramTypes));
						stream.println("Found Constructors:");
						for (final Constructor<?> ctr : enumType.getDeclaredConstructors()) {
							stream.println("    " + toString(ctr.getParameterTypes()));
						}
					}
				};
			}
			return null;
		}

		valuesField.setAccessible(true);

		try {
			final T[] previousValues = (T[]) valuesField.get(enumType);
			final List<T> values = new ArrayList<>(Arrays.asList(previousValues));
			final T newValue = makeEnum(enumType, enumName, values.size(), paramTypes, paramValues);
			values.add(newValue);
			setFailsafeFieldValue(valuesField, null, values.toArray((T[]) Array.newInstance(enumType, 0)));
			cleanEnumCache(enumType);

			return newValue;
		}
		catch (final Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	static {
		if (!isSetup) {
			setup();
		}
	}
}