package de.buddelbubi.commands;

import cn.nukkit.Server;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import de.buddelbubi.utils.PluginManagerAPI;

public class PluginManagerReloadCommand extends Command {

	public PluginManagerReloadCommand(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	
	
	@Override
	public boolean execute(CommandSender arg0, String arg1, String[] args) {
		
		if(args.length == 0) {
			
			PluginManagerAPI.reloadServer();
			
		} else {
			Server.getInstance().dispatchCommand(arg0, "pluginmanager reload " + args[0]);
		}
		
		return false;
	}

}
