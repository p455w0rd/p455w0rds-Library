package p455w0rdslib.asm;

import org.objectweb.asm.tree.*;

import com.elytradev.mini.MiniTransformer;
import com.elytradev.mini.PatchContext;
import com.elytradev.mini.annotation.Patch;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author p455w0rd
 *
 */
@Patch.Class("net.minecraft.util.datafix.fixes.HorseSaddle")
public class HorseSaddleFixerTransformer extends MiniTransformer {

	@Patch.Method(srg = "func_188217_a", mcp = "fixTagCompound", descriptor = "(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/nbt/NBTTagCompound;")
	public void patchFixTagCompound(final PatchContext ctx) {
		ctx.jumpToEnd();
		ctx.searchBackward(new InsnNode(ARETURN)).jumpBefore();
		ctx.add(new VarInsnNode(ALOAD, 0), new VarInsnNode(ALOAD, 1), new MethodInsnNode(INVOKESTATIC, "p455w0rdslib/asm/HorseSaddleFixerTransformer$Hooks", "fixHorseSaddleNBT", "(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/nbt/NBTTagCompound;", false));
	}

	public static class Hooks {

		public static NBTTagCompound fixHorseSaddleNBT(final NBTTagCompound nbt) {
			if ("EntityHorse".equals(nbt.getString("id")) && !nbt.hasKey("SaddleItem", 10) && nbt.getBoolean("Saddle")) {
				final NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setString("id", "minecraft:saddle");
				nbttagcompound.setInteger("Count", 1);
				nbttagcompound.setShort("Damage", (short) 0);
				nbt.setTag("SaddleItem", nbttagcompound);
				nbt.removeTag("Saddle");
			}
			return nbt;
		}

	}

}