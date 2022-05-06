package de.buddelbubi.listeners;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.utils.Config;
import de.buddelbubi.PluginManagerInstance;
import de.buddelbubi.utils.PluginManagerAPI;
import de.buddelbubi.utils.TextFormat;
import de.buddelbubi.utils.WindowFactory;
import de.buddelbubi.utils.WorkArounds;

public class UIListener implements Listener{

	@EventHandler
	public void on(PlayerFormRespondedEvent e) {
		
		if(e.getResponse() != null) {
			
			if(e.getWindow() instanceof FormWindowSimple) {
			
				FormWindowSimple fwresponse = (FormWindowSimple) e.getWindow();
				if(fwresponse.getTitle().equals(PluginManagerInstance.prefix + "§eInstalled Plugins")) {
					
					String plugin = fwresponse.getResponse().getClickedButton().getText().split("\\s+")[0].replace("§7", "");
					Plugin pl = Server.getInstance().getPluginManager().getPlugin(plugin);
					WindowFactory.openPluginWindow(e.getPlayer(), pl);
					
					
					//yup. I will think about this again xd 
				} else if(fwresponse.getContent().equals("§7§1§3")){
					
					String pluginname = fwresponse.getTitle().replace(PluginManagerInstance.prefix + "§e", "");
					Plugin plugin = Server.getInstance().getPluginManager().getPlugin(pluginname);
					FormWindowSimple fw = new FormWindowSimple(fwresponse.getTitle() + " §8» §e", "");
					
					switch (fwresponse.getResponse().getClickedButtonId()) {
					case 0:
						fw.setTitle(fw.getTitle() + "Plugin Info");
						String content = "";
						PluginDescription pl = Server.getInstance().getPluginManager().getPlugin(pluginname).getDescription();
						if(pl.getName() != null) content += "§eName: §8" + pl.getName() + "\n";
						if(pl.getVersion() != null) content += "§eVersion: §8" + pl.getVersion() + "\n"; 
						if(pl.getCompatibleAPIs() != null) {
							String apis = "§eAPI: §8";
							int length = pl.getCompatibleAPIs().size();
							for(int i = 0; i < length; i++) apis += pl.getCompatibleAPIs().get(i) + ( i != length-1 ? ", " : "");
							content += apis + "\n";
						}
						if(pl.getDescription() != null) content += "§eDescription: §8" + pl.getDescription() + "\n";
						if(pl.getAuthors() != null) {
							String authors = "§eAuthors: §8";
							int length = pl.getAuthors().size();
							for(int i = 0; i < length; i++) authors += pl.getAuthors().get(i) + ( i != length-1 ? ", " : "");
							content += authors + "\n";
						}
						if(pl.getPrefix() != null) content += "§ePrefix: §8" + pl.getPrefix() + "\n";
						if(pl.getWebsite() != null) content += "§eWebsite: §8" + pl.getWebsite() + "\n";
						File file = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
						content += "§eFile Name: §8" + file.getName() + "\n";
						if(pl.getDepend() != null) {
							String depend = "§eDepending Plugins: §8\n";
							int length = pl.getDepend().size();
							for(int i = 0; i < length; i++) depend += pl.getDepend().get(i) + ( i != length-1 ? "\n" : "");
							content += depend + "\n";
						}
						if(pl.getSoftDepend() != null) {
							String softdepend = "§eRecommended Plugins: §8\n";
							int length = pl.getSoftDepend().size();
							for(int i = 0; i < length; i++) softdepend += pl.getSoftDepend().get(i) + ( i != length-1 ? "\n" : "");
							content += softdepend + "\n";
						}
						if(pl.getLoadBefore() != null) {
							String loadbefore = "§eLoad Before: §8\n";
							int length = pl.getLoadBefore().size();
							for(int i = 0; i < length; i++) loadbefore += pl.getLoadBefore().get(i) + ( i != length-1 ? "\n" : "");
							content += loadbefore + "\n";
						}
						if(pl.getOrder() != null) content += "§eOrder: §8" + pl.getOrder().name() + "\n"; 
						if(pl.getMain() != null) content += "§eMain: §8" + pl.getMain() + "\n"; 
						if(pl.getCommands() != null) {
							String cmds = "§eCommands: §8\n";
						
							for(Command c : PluginManagerAPI.findCommmands(plugin)) {
								
								cmds += c.getName() + "\n";
								for(String s : c.getAliases()) cmds += s + "\n";
								
							}
		
								content += cmds + "\n";
						}
						if(pl.getPermissions() != null) {
							String knownPermissions = "§eKnown Permissions: §8\n";
							int length = pl.getPermissions().size();
							for(int i = 0; i < length; i++) knownPermissions += pl.getPermissions().get(i).getName() + ( i != length-1 ? "\n" : "");
							content += knownPermissions + "\n";
						}
						fw.setContent(content);
						
						break;
						
					case 1:
						
						
						File folder = plugin.getDataFolder();
						if(folder.exists() && folder.isDirectory()) {
							fw.setTitle(fw.getTitle() + "Config Browser");
							fw.setContent("§1§4§7" + folder.getAbsolutePath());
							for(File f : plugin.getDataFolder().listFiles()) {
								
								if(f.isDirectory()) {
									fw.addButton(new ElementButton("§e" + f.getName(), new ElementButtonImageData("path", "textures/items/book_written.png")));
								} else if(f.getName().toLowerCase().endsWith(".yml") || f.getName().toLowerCase().endsWith(".yaml")){
									fw.addButton(new ElementButton("§8" + f.getName(), new ElementButtonImageData("path", "textures/ui/icon_map.png")));
								} 
								
							}
							
							
							
						} else {
							e.getPlayer().sendMessage(PluginManagerInstance.prefix + "§cThis plugin does not have any config files.");
							return;
						}
						
						break;
						
					case 2:
						
						fw.setTitle(fw.getTitle() + "Management");
						fw.setContent("§3§4§cKeep in mind that this may cause problems.\nPlugin: " + pluginname);
						if(plugin.isEnabled()) {
							fw.addButton(new ElementButton("§cDisable", new ElementButtonImageData("path", "textures/ui/Ping_Offline_Red.png")));
						} else fw.addButton(new ElementButton("§aEnable", new ElementButtonImageData("path", "textures/ui/realms_slot_check.png")));
						fw.addButton(new ElementButton("§eReload", new ElementButtonImageData("path", "textures/ui/refresh_light.png")));
						fw.addButton(new ElementButton("§cUnload", new ElementButtonImageData("path", "textures/ui/crossout.png")));
						if(!pluginname.equals(PluginManagerInstance.plugin.getName()) && e.getPlayer().isOp())
						fw.addButton(new ElementButton("§4Uninstall", new ElementButtonImageData("path", "textures/blocks/barrier.png")));
						
						break;

					default:
						break;
					}
					
					e.getPlayer().showFormWindow(fw);
				} else if(fwresponse.getContent().startsWith("§3§4§c")) {
					String pluginname = fwresponse.getContent().replace("§3§4§cKeep in mind that this may cause problems.\nPlugin: ", "");
					Plugin plugin = Server.getInstance().getPluginManager().getPlugin(pluginname);
					
					switch (fwresponse.getResponse().getClickedButtonId()) {
					case 0:
						
						if(plugin.isEnabled()) {
							e.getPlayer().sendMessage(PluginManagerInstance.prefix + TextFormat.getFormatedPluginName(plugin) + " got disabled.");
							Server.getInstance().getPluginManager().disablePlugin(plugin);
						} else {
							e.getPlayer().sendMessage(PluginManagerInstance.prefix + TextFormat.getFormatedPluginName(plugin) + " got enabled.");
							Server.getInstance().getPluginManager().enablePlugin(plugin);
						}
						
						break;
					case 1:
						Server.getInstance().dispatchCommand(e.getPlayer(), "pm reload " + pluginname);
						break;
					
					case 2:
						Server.getInstance().dispatchCommand(e.getPlayer(), "pm unload " + pluginname);
						break;
					
					case 3:
						
						try {
							File f = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
							WorkArounds.unregisterPlugin(plugin);
							Server.getInstance().getPluginManager().disablePlugin(plugin);
							new Timer().schedule(new TimerTask() {
								
								@Override
								public void run() {
									f.delete();
									e.getPlayer().sendMessage(PluginManagerInstance.prefix + pluginname + " got uninstalled successfully.");
									PluginManagerInstance.plugin.getLogger().info(e.getPlayer().getName() + " uninstalled the plugin " + pluginname + ".");
								}
							}, 100);
						} catch (Exception e2) {
							e2.printStackTrace();
							e.getPlayer().sendMessage(PluginManagerInstance.prefix + "§cCould not uninstall " + pluginname + ". Check the console.");
						}
						
						break;
					default:
						break;
					}
					
					
				}
				
				else if(fwresponse.getContent().startsWith("§1§4§7")) {
					
					String path = fwresponse.getContent().replace("§1§4§7", "");
					File file = new File(path, fwresponse.getResponse().getClickedButton().getText().replace("§e", "").replace("§8", ""));
					if(file.isDirectory()) {
						
						FormWindowSimple fw = new FormWindowSimple(fwresponse.getTitle(), "§1§4§7" +file.getAbsolutePath());
						
						if(file.listFiles().length == 0) {
							e.getPlayer().sendMessage(PluginManagerInstance.prefix + "§cThis folder is empty.");
							return;
						}
						
						for(File f : file.listFiles()) {
							
							if(f.isDirectory()) {
								fw.addButton(new ElementButton("§e" + f.getName(), new ElementButtonImageData("path", "textures/items/book_written.png")));
							} else if(f.getName().toLowerCase().endsWith(".yml") || f.getName().toLowerCase().endsWith(".yaml")){
								fw.addButton(new ElementButton("§8" + f.getName(), new ElementButtonImageData("path", "textures/ui/icon_map.png")));
							} 
							
						}
						e.getPlayer().showFormWindow(fw);
						
					} else {
						
						Config c = new Config(file);
						FormWindowCustom fw = new FormWindowCustom("§5§9§e" + file.getAbsolutePath());
						
						if(!e.getPlayer().hasPermission("pluginmanager.config." + file.getAbsolutePath().replace("/", ".")) && !e.getPlayer().hasPermission("pluginmanager.admin")) {
							
							e.getPlayer().sendMessage(PluginManagerInstance.prefix + "§cYou are lacking the permission 'pluginmanager.config." + file.getAbsolutePath().replace("/", ".") + "'.");
							return;
						}
						
						for(String s : c.getKeys(true)) {
							
							if(c.isSection(s)) continue;
							
							 if(c.isBoolean(s)) fw.addElement(new ElementToggle("§f(§bboolean§f) §8" +s, c.getBoolean(s)));
							 else if(c.isInt(s)) fw.addElement(new ElementInput("§f(§9Int§f) §8" + s, s, String.valueOf(c.getInt(s))));
							 else if(c.isString(s)) fw.addElement(new ElementInput("§f(§cString§f) §8" + s, s, c.getString(s)));
							 else if(c.isDouble(s)) fw.addElement(new ElementInput("§f(§bdouble§f) §8" + s, s, String.valueOf( c.getDouble(s))));
							 else if(c.isList(s)) {
								 String valuestring = "";
								 int index = 0;
								 for(Object o : c.getList(s)) { 
									 index++;
									 valuestring += String.valueOf(o) + (index != c.getList(s).size() ? ", " : "");
								 }
								 fw.addElement(new ElementInput("§f(§eList§f) §8" + s, s, valuestring));
							 } else fw.addElement(new ElementInput("§f(§4Unidentified§f) §8" + s, s, String.valueOf(c.get(s))));
						}
						
						e.getPlayer().showFormWindow(fw);
						
					}
					
					
				}
				
				
			} else if(e.getWindow() instanceof FormWindowCustom) {
				
				FormWindowCustom fw = (FormWindowCustom) e.getWindow();
				
				if(fw.getTitle().startsWith("§5§9§e")) {
					
					String path = fw.getTitle().replace("§5§9§e", "");
					File file = new File(path);
					Config c = new Config(file);
					
					int index = 0;
					for(String s : c.getKeys(true)) {
						
						if(c.isSection(s)) continue;
						
						try {
							if(c.isBoolean(s)) c.set(s,  (boolean) fw.getResponse().getResponse(index));
							else if(c.isDouble(s)) c.set(s, Double.valueOf(fw.getResponse().getResponse(index).toString()));
							else if(c.isInt(s)) c.set(s, Integer.valueOf(fw.getResponse().getInputResponse(index)));
							else if(c.isLong(s)) c.set(s, Long.valueOf(fw.getResponse().getInputResponse(index)));
							else if(c.isString(s)) c.set(s, fw.getResponse().getInputResponse(index));
							else if(c.isList(s)) {
								
								List<Object> values = new ArrayList<>();
								for(String ss : fw.getResponse().getInputResponse(index).split(", ")) {
									values.add(ss);
								}
								
								c.set(s, values);
							} else {
								Server.getInstance().getLogger().warning(PluginManagerInstance.prefix + "§cUnknown Data Type in " + path + " (" + s +")");
								c.set(s, fw.getResponse().getResponse(index));
							}
						} catch (Exception e2) {
							e.getPlayer().sendMessage(PluginManagerInstance.prefix + "§cWrong data type for §e" + s + "§c! (Data not saved)");
						}
						index++;
						
					}
					
					c.save();
					e.getPlayer().sendMessage(PluginManagerInstance.prefix + file.getName() + " saved successfully.");
				}
				
			}
			
		}
	}

}
