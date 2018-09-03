package p455w0rdslib.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import p455w0rdslib.api.IProcess;

/**
 * This is a class used to run processes that implement IProcess.
 * Processes are similar to tile entities except that they are not bound to anything and they are not currently
 * persistent (they will be deleted when the world closes)
 * <p/>
 * Created by brandon3055 on 12/8/2015.
 */

public class ProcessHandlerClient {

	private static List<IProcess> processes = new ArrayList<IProcess>();
	private static List<IProcess> newProcesses = new ArrayList<IProcess>();

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new ProcessHandlerClient());
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {

			Iterator<IProcess> i = processes.iterator();
			List<IProcess> toBeRemoved = Lists.newArrayList();

			while (i.hasNext()) {
				IProcess process = i.next();
				if (process.isDead()) {
					toBeRemoved.add(process);
				}
				else {
					process.updateProcess();
				}
			}

			if (!toBeRemoved.isEmpty()) {
				for (IProcess p : toBeRemoved) {
					if (processes.contains(p)) {
						processes.remove(p);
					}
				}
			}

			if (!newProcesses.isEmpty()) {
				processes.addAll(newProcesses);
				newProcesses.clear();
			}
		}
	}

	@SubscribeEvent
	public void onWorldClose(WorldEvent.Unload event) {
		processes.clear();
		newProcesses.clear();
	}

	public static void addProcess(IProcess process) {
		newProcesses.add(process);
	}

}