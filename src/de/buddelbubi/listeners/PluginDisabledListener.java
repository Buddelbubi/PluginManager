package de.buddelbubi.listeners;

import java.util.ArrayList;
import java.util.List;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.plugin.PluginDisableEvent;
import de.buddelbubi.PluginManagerInstance;
import de.buddelbubi.utils.WorkArounds;

public class PluginDisabledListener implements Listener{


	@EventHandler
	public void on(PluginDisableEvent e) {
		
		try {
			
			List<Command> commands = new ArrayList<>();
			
			for(String s : Server.getInstance().getCommandMap().getCommands().keySet()) {
			
				if(s.startsWith(e.getPlugin().getName().toLowerCase() + ":")) {
					commands.add(Server.getInstance().getCommandMap().getCommand(s.split(":")[1]));
					
				}
				
			}
			for(Command c : commands) WorkArounds.unregisterCommand(c);
		
		} catch (Exception e2) {
			Server.getInstance().getLogger().critical(PluginManagerInstance.prefix + "§cSomething went wrong while unloading commands.");
		}
		
	/*	AvailableCommandsPacket packet = new AvailableCommandsPacket();
		
		Map<String, CommandDataVersions> availableCommands = new HashMap<>();
		CommandDataVersions ver = new CommandDataVersions();
		
		for(Command c : Server.getInstance().getCommandMap().getCommands().values()) {
			System.out.println(c.getName());
			ver.versions.add(c.getDefaultCommandData());
			availableCommands.put(c.getName(), ver); 
		}
		for(String s : Server.getInstance().getCommandMap().getCommands().keySet()) {
			
		}
		
		packet.commands = availableCommands;
		
		for(Player p : Server.getInstance().getOnlinePlayers().values()) {
			p.dataPacket(packet);
		} */
	}
	
	
}
