package p455w0rdslib.api;

/**
 * For easy attachment of capability-based TE chunkloader, implement this interface on your Tile Entity
 *
 * @author p455w0rd
 *
 */
public interface IChunkLoadable {

	/**
	 * <b>Not fully implemented</b>
	 *
	 * @return An instance fo your main mod container
	 */
	Object getModInstance();

	/**
	 * @return Your modid
	 */
	String getModID();

}
