package p455w0rdslib.asm;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import net.minecraft.launchwrapper.IClassTransformer;

/**
 * @author p455w0rd
 *
 */
public class ClassTransformer implements IClassTransformer {

	private static final String RENDERGLOBAL_CLASS = "net.minecraft.client.renderer.RenderGlobal";
	private static final String CHUNKRENDERCONTAINER_CLASS = "net.minecraft.client.renderer.ChunkRenderContainer";
	private static final String HOOKS_PATH = "p455w0rdslib/asm/Hooks";

	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
		if (transformedName.equals(RENDERGLOBAL_CLASS)) {
			return patchRenderGlobal(basicClass);
		}
		if (transformedName.equals(CHUNKRENDERCONTAINER_CLASS)) {
			return patchChunkRenderContainer(basicClass);
		}
		return basicClass;
	}

	private static byte[] patchRenderGlobal(final byte[] c) {
		final ClassReader reader = new ClassReader(c);
		final ClassNode node = new ClassNode();
		boolean found = false;
		reader.accept(node, 0);
		for (final MethodNode method : node.methods) {
			if (found) {
				break;
			}
			if ((method.name.equals("func_174982_a") || method.name.equals("renderBlockLayer")) && method.desc.equals("(Lnet/minecraft/util/BlockRenderLayer;)V")) {
				final InsnList instr = method.instructions;
				method.instructions.insert(instr.getFirst(), new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_PATH, "enableColoredLighting", "()V", false));
				final AbstractInsnNode[] nodeList = instr.toArray();
				for (int i = nodeList.length - 1; i >= 0; i--) {
					final AbstractInsnNode currentNode = nodeList[i];
					if (currentNode.getOpcode() == Opcodes.RETURN) {
						method.instructions.insertBefore(currentNode, new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_PATH, "disableColoredLighting", "()V", false));
						FMLPlugin.log("Successfully patched " + RENDERGLOBAL_CLASS + "#renderBlockLayer(Lnet/minecraft/util/BlockRenderLayer;)V");
						found = true;
						break;
					}
				}
			}
		}
		final SafeClassWriter writer = new SafeClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		node.accept(writer);
		return writer.toByteArray();
	}

	private static byte[] patchChunkRenderContainer(final byte[] c) {
		final ClassReader reader = new ClassReader(c);
		final ClassNode node = new ClassNode();
		reader.accept(node, 0);
		for (final MethodNode method : node.methods) {
			if ((method.name.equals("func_178003_a") || method.name.equals("preRenderChunk")) && method.desc.equals("(Lnet/minecraft/client/renderer/chunk/RenderChunk;)V")) {
				final InsnList instr = method.instructions;
				final AbstractInsnNode[] nodeList = instr.toArray();
				for (int i = nodeList.length - 1; i >= 0; i--) {
					final AbstractInsnNode currentNode = nodeList[i];
					if (currentNode.getOpcode() == Opcodes.RETURN) {
						method.instructions.insertBefore(currentNode, new VarInsnNode(Opcodes.ALOAD, 1));
						method.instructions.insertBefore(currentNode, new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_PATH, "preRenderChunk", "(Lnet/minecraft/client/renderer/chunk/RenderChunk;)V", false));
						FMLPlugin.log("Successfully patched " + CHUNKRENDERCONTAINER_CLASS + "#preRenderChunk(Lnet/minecraft/client/renderer/chunk/RenderChunk;)V");
					}
				}
			}
		}
		final SafeClassWriter writer = new SafeClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		node.accept(writer);
		return writer.toByteArray();
	}

}
