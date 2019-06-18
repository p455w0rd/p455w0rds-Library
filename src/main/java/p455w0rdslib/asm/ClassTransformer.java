package p455w0rdslib.asm;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

/**
 * @author p455w0rd
 *
 */
public class ClassTransformer implements IClassTransformer {

	private static final String RENDERGLOBAL_CLASS = "net.minecraft.client.renderer.RenderGlobal";
	private static final String CHUNKRENDERCONTAINER_CLASS = "net.minecraft.client.renderer.ChunkRenderContainer";
	private static final String HOOKS_PATH = "p455w0rdslib/asm/Hooks";
	private static final Logger log = LogManager.getLogger("p455w0rdsc0re");
	public static boolean enabled = true;
	private static boolean init = false;

	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
		if (shouldPatch()) {
			if (transformedName.equals(RENDERGLOBAL_CLASS)) {
				return patchRenderGlobal(basicClass);
			}
			if (transformedName.equals(CHUNKRENDERCONTAINER_CLASS)) {
				return patchChunkRenderContainer(basicClass);
			}
		}
		return basicClass;
	}

	private boolean shouldPatch() {
		if (!init) {
			init = true;
			if (classExists("org.spongepowered.mod.SpongeCoremod")) {
				enabled = false;
				log.info("Albedo detected; Patching aborted :D");
			}
			else if (classExists("optifine.OptiFineForgeTweaker")) {
				enabled = false;
				log.info("Optifine detected; Patching aborted :D");
			}
			else if (classExists("elucent.albedo.asm.ASMTransformer")) {
				enabled = false;
				log.info("Mixins detected; Patching aborted :D");
			}
		}
		return enabled;
	}

	private boolean classExists(final String name) {
		try {
			return Launch.classLoader.getClassBytes(name) != null;
		}
		catch (final IOException e) {
			throw new RuntimeException(e);
		}
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
				instr.insert(instr.getFirst(), new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_PATH, "enableColoredLighting", "()V", false));
				final AbstractInsnNode[] nodeList = instr.toArray();
				for (int i = nodeList.length - 1; i >= 0; i--) {
					final AbstractInsnNode currentNode = nodeList[i];
					if (currentNode.getOpcode() == Opcodes.RETURN) {
						instr.insertBefore(currentNode, new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_PATH, "disableColoredLighting", "()V", false));
						log.info("Successfully patched " + RENDERGLOBAL_CLASS + "#renderBlockLayer(Lnet/minecraft/util/BlockRenderLayer;)V");
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
						instr.insertBefore(currentNode, new VarInsnNode(Opcodes.ALOAD, 1));
						instr.insertBefore(currentNode, new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_PATH, "preRenderChunk", "(Lnet/minecraft/client/renderer/chunk/RenderChunk;)V", false));
						log.info("Successfully patched " + CHUNKRENDERCONTAINER_CLASS + "#preRenderChunk(Lnet/minecraft/client/renderer/chunk/RenderChunk;)V");
					}
				}
			}
		}
		final SafeClassWriter writer = new SafeClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		node.accept(writer);
		return writer.toByteArray();
	}

}
