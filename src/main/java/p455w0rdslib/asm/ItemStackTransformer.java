package p455w0rdslib.asm;

import org.objectweb.asm.tree.*;

import com.elytradev.mini.MiniTransformer;
import com.elytradev.mini.PatchContext;
import com.elytradev.mini.annotation.Patch;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * @author p455w0rd
 *
 */
@Patch.Class("net.minecraft.item.ItemStack")
public class ItemStackTransformer extends MiniTransformer {

	@Patch.Method(srg = "<init>", mcp = "<init>", descriptor = "(Lnet/minecraft/nbt/NBTTagCompound;)V")
	public void patchItemStackConstructor(final PatchContext ctx) {
		ctx.jumpToEnd();
		ctx.searchBackward(new InsnNode(RETURN)).jumpBefore();
		ctx.add(new VarInsnNode(ALOAD, 0), new VarInsnNode(ALOAD, 1), new MethodInsnNode(INVOKESTATIC, "p455w0rdslib/asm/ItemStackTransformer$Hooks", "initItemStack", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/nbt/NBTTagCompound;)V", false));
	}

	@Patch.Method(srg = "func_77955_b", mcp = "writeToNBT", descriptor = "(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/nbt/NBTTagCompound;")
	public void patchWriteNBT(final PatchContext ctx) {
		ctx.jumpToEnd();
		ctx.searchBackward(new InsnNode(ARETURN)).jumpBefore();
		ctx.add(new VarInsnNode(ALOAD, 0), new VarInsnNode(ALOAD, 1), new MethodInsnNode(INVOKESTATIC, "p455w0rdslib/asm/ItemStackTransformer$Hooks", "writeNBT", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/nbt/NBTTagCompound;", false));
	}

	public static class Hooks {

		public static void initItemStack(final ItemStack stack, final NBTTagCompound nbt) {
			if (nbt.hasKey("Count", NBT.TAG_INT)) {
				stack.setCount(nbt.getInteger("Count"));
			}
		}

		public static NBTTagCompound writeNBT(final ItemStack stack, final NBTTagCompound nbt) {
			if (nbt.hasKey("Count", NBT.TAG_BYTE)) {
				nbt.removeTag("Count");
			}
			nbt.setInteger("Count", stack.getCount());
			return nbt;
		}

	}

}
