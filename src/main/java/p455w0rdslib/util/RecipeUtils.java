package p455w0rdslib.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.oredict.OreIngredient;
import p455w0rdslib.LibGlobals;

/**
 * @author Shadows-of-Fire
 *
 */
public class RecipeUtils {

	private int j = 0;
	private String modID = LibGlobals.MODID;
	private String modName = LibGlobals.NAME;
	/*
	 * This needs to be looped through and passed in a RegistryEvent.Register<IRecipe>, it should also be populated during that event.
	 */
	public final List<IRecipe> RECIPES = new ArrayList<IRecipe>();

	public RecipeUtils() {

	}

	public RecipeUtils(String modID, String modName) {
		this.modID = modID;
		this.modName = modName;
	}

	/*
	 * This adds the recipe to the list of crafting recipes.  Since who cares about names, it adds it as recipesX, where X is the current recipe you are adding.
	 */
	public void addRecipe(int j, IRecipe rec) {
		if (rec.getRegistryName() == null) {
			RECIPES.add(rec.setRegistryName(new ResourceLocation(modID, "recipes" + j)));
		}
		else {
			RECIPES.add(rec);
		}
	}

	/*
	 * This adds the recipe to the list of crafting recipes.  Cares about names.
	 */
	public void addRecipe(String name, IRecipe rec) {
		if (rec.getRegistryName() == null) {
			RECIPES.add(rec.setRegistryName(new ResourceLocation(modID, name)));
		}
		else {
			RECIPES.add(rec);
		}
	}

	/*
	 * This adds a shaped recipe to the list of crafting recipes, using the forge format.
	 */
	@Deprecated
	public IRecipe addOldShaped(ItemStack output, Object... input) {
		ShapedPrimer primer = CraftingHelper.parseShaped(input);
		IRecipe recipe = new ShapedRecipes(new ResourceLocation(modID, output.getUnlocalizedName()).toString(), primer.width, primer.height, primer.input, output);
		addRecipe(j++, recipe);
		return recipe;
	}

	/*
	 * This adds a shaped recipe to the list of crafting recipes, using the forge format, with a custom group.
	 */
	@Deprecated
	public void addOldShaped(String group, ItemStack output, Object... input) {
		ShapedPrimer primer = CraftingHelper.parseShaped(input);
		addRecipe(j++, new ShapedRecipes(new ResourceLocation(modID, group).toString(), primer.width, primer.height, primer.input, output));
	}

	/*
	* This adds a shaped recipe to the list of crafting recipes, using the forge format, with a custom group.
	*/
	@Deprecated
	public void addOldShaped(String name, String group, ItemStack output, Object... input) {
		ShapedPrimer primer = CraftingHelper.parseShaped(input);
		addRecipe(j++, new ShapedRecipes(new ResourceLocation(modID, group).toString(), primer.width, primer.height, primer.input, output).setRegistryName(modID, name));
	}

	/*
	 * This adds a shapeless recipe to the list of crafting recipes, using the forge format.
	 */
	@Deprecated
	public void addOldShapeless(ItemStack output, Object... input) {
		addRecipe(j++, new ShapelessRecipes(new ResourceLocation(modID, "recipes" + j).toString(), output, createInput(input)));
	}

	/*
	 * This adds a shapeless recipe to the list of crafting recipes, using the forge format, with a custom group.
	 */
	@Deprecated
	public void addOldShapeless(String group, ItemStack output, Object... input) {
		addRecipe(j++, new ShapelessRecipes(new ResourceLocation(modID, group).toString(), output, createInput(input)));
	}

	@Deprecated
	public void addOldShapeless(String name, String group, ItemStack output, Object... input) {
		addRecipe(j++, new ShapelessRecipes(new ResourceLocation(modID, group).toString(), output, createInput(input)).setRegistryName(modID, name));
	}

	/*
	 * Adds a shapeless recipe with X output using an array of inputs. Use Strings for OreDictionary support. This array is not ordered.
	 */
	public void addShapeless(ItemStack output, Object... inputs) {
		addRecipe(j++, new ShapelessRecipes(modID + ":" + j, output, createInput(inputs)));
	}

	public void addShapeless(Item output, Object... inputs) {
		addShapeless(new ItemStack(output), inputs);
	}

	public void addShapeless(Block output, Object... inputs) {
		addShapeless(new ItemStack(output), inputs);
	}

	public void addSmallShapeless(ItemStack output, Ingredient input) {
		NonNullList<Ingredient> k = NonNullList.withSize(1, input);
		addRecipe(j++, new ShapelessRecipes(modID + ":" + j, output, k));
	}

	/*
	 * Adds a shapeless recipe with X output using an array of inputs. Use Strings for OreDictionary support. This array is not ordered.  This has a custom group.
	 */
	public void addShapeless(String group, ItemStack output, Object... inputs) {
		addRecipe(j++, new ShapelessRecipes(modID + ":" + group, output, createInput(inputs)));
	}

	public void addShapeless(String group, Item output, Object... inputs) {
		addShapeless(group, new ItemStack(output), inputs);
	}

	public void addShapeless(String group, Block output, Object... inputs) {
		addShapeless(group, new ItemStack(output), inputs);
	}

	/*
	 * Adds a shapeless recipe with X output on a crafting grid that is W x H, using an array of inputs.  Use null for nothing, use Strings for OreDictionary support, this array must have a length of width * height.
	 * This array is ordered, and items must follow from left to right, top to bottom of the crafting grid.
	 */
	public void addShaped(ItemStack output, int width, int height, Object... input) {
		addRecipe(j++, genShaped(output, width, height, input));
	}

	public void addShaped(Item output, int width, int height, Object... input) {
		addShaped(new ItemStack(output), width, height, input);
	}

	public void addShaped(Block output, int width, int height, Object... input) {
		addShaped(new ItemStack(output), width, height, input);
	}

	/*
	 * Adds a shapeless recipe with X output on a crafting grid that is W x H, using an array of inputs.  Use null for nothing, use Strings for OreDictionary support, this array must have a length of width * height.
	 * This array is ordered, and items must follow from left to right, top to bottom of the crafting grid. This has a custom group.
	 */
	public void addShaped(String group, ItemStack output, int width, int height, Object... input) {
		addRecipe(j++, genShaped(modID + ":" + group, output, width, height, input));
	}

	public void addShaped(String group, Item output, int width, int height, Object... input) {
		addShaped(group, new ItemStack(output), width, height, input);
	}

	public void addShaped(String group, Block output, int width, int height, Object... input) {
		addShaped(group, new ItemStack(output), width, height, input);
	}

	public ShapedRecipes genShaped(ItemStack output, int l, int w, Object[] input) {
		if (input[0] instanceof Object[]) {
			input = (Object[]) input[0];
		}
		if (l * w != input.length) {
			throw new UnsupportedOperationException("Attempted to add invalid shaped recipe.  Complain to the author of " + modName);
		}
		NonNullList<Ingredient> inputL = NonNullList.create();
		for (int i = 0; i < input.length; i++) {
			Object k = input[i];
			if (k instanceof String) {
				inputL.add(i, new OreIngredient((String) k));
			}
			else if (k instanceof ItemStack && !((ItemStack) k).isEmpty()) {
				inputL.add(i, Ingredient.fromStacks((ItemStack) k));
			}
			else if (k instanceof Item) {
				inputL.add(i, Ingredient.fromStacks(new ItemStack((Item) k)));
			}
			else if (k instanceof Block) {
				inputL.add(i, Ingredient.fromStacks(new ItemStack((Block) k)));
			}
			else {
				inputL.add(i, Ingredient.EMPTY);
			}
		}

		return new ShapedRecipes(modID + ":" + j, l, w, inputL, output);
	}

	public ShapedRecipes genShaped(String group, ItemStack output, int l, int w, Object[] input) {
		if (input[0] instanceof List) {
			input = ((List<?>) input[0]).toArray();
		}
		else if (input[0] instanceof Object[]) {
			input = (Object[]) input[0];
		}
		if (l * w != input.length) {
			throw new UnsupportedOperationException("Attempted to add invalid shaped recipe.  Complain to the author of " + modName);
		}
		NonNullList<Ingredient> inputL = NonNullList.create();
		for (int i = 0; i < input.length; i++) {
			Object k = input[i];
			if (k instanceof String) {
				inputL.add(i, new OreIngredient((String) k));
			}
			else if (k instanceof ItemStack && !((ItemStack) k).isEmpty()) {
				inputL.add(i, Ingredient.fromStacks((ItemStack) k));
			}
			else if (k instanceof Item) {
				inputL.add(i, Ingredient.fromStacks(new ItemStack((Item) k)));
			}
			else if (k instanceof Block) {
				inputL.add(i, Ingredient.fromStacks(new ItemStack((Block) k)));
			}
			else {
				inputL.add(i, Ingredient.EMPTY);
			}
		}

		return new ShapedRecipes(group, l, w, inputL, output);
	}

	public NonNullList<Ingredient> createInput(Object[] input) {
		if (input[0] instanceof List) {
			input = ((List<?>) input[0]).toArray();
		}
		else if (input[0] instanceof Object[]) {
			input = (Object[]) input[0];
		}
		NonNullList<Ingredient> inputL = NonNullList.create();
		for (int i = 0; i < input.length; i++) {
			Object k = input[i];
			if (k instanceof String) {
				inputL.add(i, new OreIngredient((String) k));
			}
			else if (k instanceof ItemStack) {
				inputL.add(i, Ingredient.fromStacks((ItemStack) k));
			}
			else if (k instanceof Item) {
				inputL.add(i, Ingredient.fromStacks(new ItemStack((Item) k)));
			}
			else if (k instanceof Block) {
				inputL.add(i, Ingredient.fromStacks(new ItemStack((Block) k)));
			}
			else {
				throw new UnsupportedOperationException("Attempted to add invalid shapeless recipe.  Complain to the author of " + modName);
			}
		}
		return inputL;
	}
}