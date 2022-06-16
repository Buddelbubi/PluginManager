package de.buddelbubi.utils;

import java.util.List;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.plugin.Plugin;
import de.buddelbubi.PluginManagerInstance;

public class PluginManagerAPI {
	
	public static void reloadServer() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					long millis = System.currentTimeMillis();
					Server.getInstance().broadcastMessage(PluginManagerInstance.prefix + "§eReloading the Server...");
					Server.getInstance().reload();
					Server.getInstance().broadcastMessage(PluginManagerInstance.prefix + "§eServer reloaded in " + (System.currentTimeMillis() - millis) + " milliseconds.");
				} catch (Exception e) {
					Server.getInstance().broadcastMessage(PluginManagerInstance.prefix + "§cCould not reload the server. Please initialize a reboot.");
				}
				
				
			}
		}).start();
	}
	
	public static void unloadPlugin(Plugin plugin) {
		
		WorkArounds.unregisterPlugin(plugin);
		
	}
	
	public static void unloadPlugin(String plugin) {
		
		WorkArounds.unregisterPlugin(Server.getInstance().getPluginManager().getPlugin(plugin));
		
	}
	
	public static List<Command> findCommmands(Plugin plugin){
		
		return WorkArounds.findCommands(plugin);
		
	}
	
	
	
}
