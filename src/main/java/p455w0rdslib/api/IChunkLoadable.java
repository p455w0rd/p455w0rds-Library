package p455w0rdslib.api;

/**
 * For easy attachment of capability-based TE chunkloader, implement this interface on your Tile Entity<br>
 * It is recommended to use <b>ForgeChunkManager.setForcedChunkLoadingCallback(<i>&lt;YOUR MOD INSTANCE&gt;</i>, {@link p455w0rdslib.util.ChunkUtils.Callback#getInstance() Callback.getInstance()});</b><br>
 * in {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent PreInit} as your Chunk Loading Callback
 * @author p455w0rd
 *
 */
public interface IChunkLoadable {

	/**
	 * @return An instance of your main mod container
	 */
	Object getModInstance();

	/**
	 * @return Your modid
	 */
	String getModID();

	/**
	 * This method determines chunkloading functionality.<br>
	 * This is primarily for instances where you might want to have a config option to disable chunk loading of a specific TE.
	 * @return true if TE should chunkload
	 */
	boolean shouldChunkLoad();

}
