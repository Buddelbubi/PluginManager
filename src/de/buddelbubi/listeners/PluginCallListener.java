package de.buddelbubi.listeners;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.plugin.PluginDisableEvent;
import cn.nukkit.event.plugin.PluginEnableEvent;
import cn.nukkit.plugin.Plugin;

public class PluginCallListener implements Listener {

	private HashMap<String, List<String>> disableddepend = new HashMap<>();
	
	@EventHandler
	public void on(PluginEnableEvent e) {
		
		 for(Player p : Server.getInstance().getOnlinePlayers().values()) p.sendCommandData();
		 
		 if(disableddepend.containsKey(e.getPlugin().getName())) {
			 for(String s : disableddepend.get(e.getPlugin().getName())) {
				if(Server.getInstance().getPluginManager().getPlugin(s) != null) {
					Plugin dis = Server.getInstance().getPluginManager().getPlugin(s);
					if(dis.isDisabled()) {
						Server.getInstance().getPluginManager().enablePlugin(dis);
					}
				}
			 }
			 disableddepend.remove(e.getPlugin().getName());
		 }
		 
		 
	}
	
	@EventHandler
	public void on(PluginDisableEvent e) {
		
		 for(Player p : Server.getInstance().getOnlinePlayers().values()) p.sendCommandData();
		 
		 for(Plugin p : Server.getInstance().getPluginManager().getPlugins().values()) {
			 
			 if(p.getDescription().getDepend().contains(e.getPlugin().getName()) && p.isEnabled()) {
				 List<String> disabled = Collections.singletonList(p.getName());
				 if(disableddepend.containsKey(e.getPlugin().getName())) {
					 for(String s : disableddepend.get(e.getPlugin().getName())) {
						if(!disabled.contains(s)) disabled.add(s);
					 }
				 }
				 disableddepend.put(e.getPlugin().getName(), disabled);
				 Server.getInstance().getPluginManager().disablePlugin(p);
			 }
			 
		 }
		
	}
	
}
