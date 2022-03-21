package de.buddelbubi.commands;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.Plugin;
import de.buddelbubi.PluginManagerInstance;
import de.buddelbubi.utils.TextFormat;
import de.buddelbubi.utils.WindowFactory;
import de.buddelbubi.utils.WorkArounds;

public class PluginManagerCommand extends Command{

	public PluginManagerCommand(String name) {
		super(name);
		
	}

	@Override
	public boolean execute(CommandSender arg0, String arg1, String[] args) {
		
		
		String prefix = PluginManagerInstance.prefix;
		
		
		
		
		if(args.length >= 1) {
		
			if(!arg0.hasPermission("pluginmanager." + args[0])) {
				
				arg0.sendMessage(prefix + "§cYou are lacking the permission 'pluginmanager." + args[0] + "'.");
				return false;
			}
			
			switch (args[0]) {
			case "load":
				
				if(args.length == 2) {
					
					File file = new File(Server.getInstance().getPluginPath(), (args[1].contains(".jar") ? args[1] : args[1] + ".jar"));
					if(file.exists()) {
						
						Plugin plugin = Server.getInstance().getPluginManager().loadPlugin(file);
	
						if(plugin != null) {
						
							for(String depend : plugin.getDescription().getDepend()) {
								
								if(Server.getInstance().getPluginManager().getPlugin(depend) == null) {
									arg0.sendMessage(prefix + "§cCould not enable " + TextFormat.getFormatedPluginName(plugin) + " due a missing dependency: §4" + depend + ".jar");
									return false;
								}
							}
							
							Server.getInstance().getPluginManager().enablePlugin(plugin);
							arg0.sendMessage(prefix + TextFormat.getFormatedPluginName(plugin) + " loaded successully.");
							
						} else arg0.sendMessage(prefix + "§cSomething went wrong while loading the plugin.");
						
					} else arg0.sendMessage(prefix + "There is no file called " + file.getName() + ".");
					
				} else arg0.sendMessage(prefix + "§cDo /pluginmanager load [filename]");
				
				break;
				
			case "unload":
				
				if(args.length == 2) {
					
				
						Plugin plugin = Server.getInstance().getPluginManager().getPlugin(args[1]);
	
						if(plugin == PluginManagerInstance.plugin) {
							arg0.sendMessage(prefix + "§cYou can not disable PluginManager.");
							return false;
						}
						
						if(plugin != null) {
							
							
							WorkArounds.unregisterPlugin(plugin);
							Server.getInstance().getPluginManager().disablePlugin(plugin);
							
							arg0.sendMessage(prefix + TextFormat.getFormatedPluginName(plugin) + " unloaded successully.");
							
						} else arg0.sendMessage(prefix + "§cThere is no plugin called " + args[1] + ".");
						
					
					
				} else arg0.sendMessage(prefix + "§cDo /pluginmanager unload [filename]");
				
				break;
			
			case "enable":
				
				if(args.length == 2) {
				
						Plugin plugin = Server.getInstance().getPluginManager().getPlugin(args[1]);
	
						if(plugin != null) {
						
							if(plugin.isEnabled()) {
								arg0.sendMessage(prefix + "§c" + TextFormat.getFormatedPluginName(plugin) + " is already enabled.");
								return false;
							}
							
							for(String depend : plugin.getDescription().getDepend()) {
								
								if(Server.getInstance().getPluginManager().getPlugin(depend) == null) {
									arg0.sendMessage(prefix + "§cCould not enable " + TextFormat.getFormatedPluginName(plugin) + " due a missing dependency: §4" + depend + ".jar");
									return false;
								}
							}
							
							Server.getInstance().getPluginManager().enablePlugin(plugin);
							arg0.sendMessage(prefix + TextFormat.getFormatedPluginName(plugin) + " enabled successully.");
							
						} else arg0.sendMessage(prefix + "§cThere is no Plugin called " + args[1] + ".");
					
				} else arg0.sendMessage(prefix + "§cDo /pluginmanager enable [Plugin]");
				
				break;
				
			case "disable":
				
				if(args.length == 2) {
				
						Plugin plugin = Server.getInstance().getPluginManager().getPlugin(args[1]);
					
						if(plugin == PluginManagerInstance.plugin) {
							arg0.sendMessage(prefix + "§cYou can not disable PluginManager.");
							return false;
						}
						
						if(plugin != null) {
						
							if(!plugin.isEnabled()) {
								arg0.sendMessage(prefix + "§c" + TextFormat.getFormatedPluginName(plugin) + " is already disabled.");
								return false;
							}
							
							Server.getInstance().getPluginManager().disablePlugin(plugin);
							
							
							arg0.sendMessage(prefix + TextFormat.getFormatedPluginName(plugin) + " disabled successully.");
							
						} else arg0.sendMessage(prefix + "§cThere is no Plugin called " + args[1] + ".");
					
				} else arg0.sendMessage(prefix + "§cDo /pluginmanager disable [Plugin]");
				
				break;
				
			case "reload":
				
				if(args.length == 2) {
				
						Plugin plugin = Server.getInstance().getPluginManager().getPlugin(args[1]);
						
						if(plugin == PluginManagerInstance.plugin) {
							arg0.sendMessage(prefix + "§cYou can not disable PluginManager.");
							return false;
						}
	
						if(plugin != null) {
							
							File file = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
							
							long millis = System.currentTimeMillis();
							
							WorkArounds.unregisterPlugin(plugin);
							
							for(String depend : plugin.getDescription().getDepend()) {
								
								if(Server.getInstance().getPluginManager().getPlugin(depend) == null) {
									arg0.sendMessage(prefix + "§cCould not enable " + TextFormat.getFormatedPluginName(plugin) + " due a missing dependency: §4" + depend + ".jar");
									return false;
								}
							}
							Server.getInstance().getPluginManager().disablePlugin(plugin);
							try {
								Server.getInstance().enablePlugin(Server.getInstance().getPluginManager().loadPlugin(file)); 
							} catch (Exception e) {
								arg0.sendMessage(prefix + "§cCouldn't load Plugin " + plugin.getName() + ". File renamed or deleted?"); 
							}
							
							
							arg0.sendMessage(prefix + TextFormat.getFormatedPluginName(plugin) + " reloaded successully. (" + (System.currentTimeMillis() - millis) + " milliseconds)");
							
						} else arg0.sendMessage(prefix + "§cThere is no Plugin called " + args[1] + ".");
					
				} else if(args.length == 1) {
					
					
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								long millis = System.currentTimeMillis();
								Server.getInstance().broadcastMessage(PluginManagerInstance.prefix + "§eReloading the Server...");
								Server.getInstance().reload();
								Server.getInstance().broadcastMessage(PluginManagerInstance.prefix + "§eServer reloaded in " + (System.currentTimeMillis() - millis) + " milliseconds.");
								
							}
						}).start();
					
				} else arg0.sendMessage(prefix + "§cDo /pluginmanager reload [Plugin]");
				break;

			case "help":
				
				arg0.sendMessage(prefix + "Help Page\n"
						+ "§e/pluginmanager §7opens an UI to manage all plugins ingame.\n"
						+ "§e/pluginmanager load [file] §7loads a plugin from a .jar file.\n"
						+ "§e/pluginmanager unload [plugin] §7unloads a plugin from your server.\n"
						+ "§e/pluginmanager reload [plugin] §7reloads a plugin without reloading the whole server.\n"
						+ "§e/pluginmanager enable [plugin] §7enables a disabled plugin.\n"
						+ "§e/pluginmanager disable [plugin] §7disables a plugin.\n"
						+ "§e/reload [plugin] §7reloads a single plugin.");
				
				
				break;
				
			default:
				arg0.sendMessage(prefix + "§cDo /pluginmanager help");
				break;
			}
			
		} else {
			if(arg0 instanceof Player) {
				
				Player p = (Player) arg0;
				
				if(p.hasPermission("pluginmanager.ui"))
				
				WindowFactory.openPluginListWindow(p);
				
				else p.sendMessage(prefix + "§cYou are lacking the permission 'pluginmanager.ui'"); 
				
			}  else arg0.sendMessage(prefix + "§cDo /pluginmanager help");
		}
		
		
		return false;
	}

}
