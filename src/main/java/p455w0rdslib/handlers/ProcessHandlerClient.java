package p455w0rdslib.handlers;

import java.util.*;

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

	private static List<IProcess> processes = new ArrayList<>();
	private static List<IProcess> newProcesses = new ArrayList<>();

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new ProcessHandlerClient());
	}

	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			final List<IProcess> tmpList = new ArrayList<>(processes);
			final Iterator<IProcess> i = tmpList.iterator();
			final List<IProcess> toBeRemoved = Lists.newArrayList();
			while (i.hasNext()) {
				final IProcess process = i.next();
				if (process.isDead()) {
					toBeRemoved.add(process);
				}
				else {
					process.updateProcess();
				}
			}
			if (!toBeRemoved.isEmpty()) {
				for (final IProcess p : toBeRemoved) {
					if (tmpList.contains(p)) {
						tmpList.remove(p);
					}
				}
			}
			if (!newProcesses.isEmpty()) {
				tmpList.addAll(newProcesses);
				newProcesses.clear();
			}
			if (!tmpList.isEmpty()) {
				processes.clear();
				processes.addAll(tmpList);
			}
		}
	}

	@SubscribeEvent
	public void onWorldClose(final WorldEvent.Unload event) {
		processes.clear();
		newProcesses.clear();
	}

	public static void addProcess(final IProcess process) {
		newProcesses.add(process);
	}

}