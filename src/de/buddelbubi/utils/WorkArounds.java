package de.buddelbubi.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginClassLoader;
import cn.nukkit.plugin.PluginLoader;
import de.buddelbubi.PluginManagerInstance;


public class WorkArounds {
	
	
	
	private static Object getPrivateField(Object object, String field)throws SecurityException,
    NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
    Class<?> clazz = object.getClass();
    Field objectField = clazz.getDeclaredField(field);
    objectField.setAccessible(true);
    Object result = objectField.get(object);
    objectField.setAccessible(false);
    return result;
}

	public static void unregisterCommand(Command cmd) {
	    try {
	    	
	      //  Object map = getPrivateField(Server.getInstance().getCommandMap(), "knownCommands");
	        @SuppressWarnings("unchecked")
			HashMap<String, Command> knownCommands = (HashMap<String, Command>) getPrivateField(Server.getInstance().getCommandMap(), "knownCommands");

	       if(knownCommands.containsKey(cmd.getName())) knownCommands.remove(cmd.getName());
	       if(knownCommands.containsKey(cmd.getName() + ":" + cmd.getName())) knownCommands.remove(cmd.getName()+ ":" + cmd.getName());
	        for (String alias : cmd.getAliases()){
	           if(knownCommands.containsKey(cmd.getName() + ":" + alias)){
	                knownCommands.remove(cmd.getName() + ":" + alias);
	            }
	           if(knownCommands.containsKey(alias)){
	                knownCommands.remove(alias);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static void unregisterPlugin(Plugin plugin) {
	    try {
	    	List<Command> commands = new ArrayList<>();
	    	
	    	if(plugin == PluginManagerInstance.plugin) return;
	    	
	    	Server.getInstance().getServiceManager().cancel(plugin);
	    	
	    	
	        Object map = getPrivateField(Server.getInstance().getPluginManager(), "plugins");
	        @SuppressWarnings("unchecked")
	        HashMap<String, Plugin> plugins = (HashMap<String, Plugin>) map;
	       
	        plugins.remove(plugin.getName());
	        
	        Object cmds = getPrivateField(Server.getInstance().getCommandMap(), "knownCommands");
	        @SuppressWarnings("unchecked")
	        HashMap<String, Command> knownCommands = (HashMap<String, Command>) cmds;
	        
	        HashMap<String, String> cmdds = new HashMap<>();
	        
	        for(Command c : Server.getInstance().getCommandMap().getCommands().values()) {
	        	try {
	        		if(cmdds.containsKey(c.getClass().getName())) {
		        		cmdds.put(c.getClass().getName(),cmdds.get(c.getClass().getName()) + ">>>" + c.getName());
		        	} else cmdds.put(c.getClass().getName(), c.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	        
	        Object files = getPrivateField(Server.getInstance().getPluginManager(), "fileAssociations");
	        @SuppressWarnings("unchecked")
	        HashMap<String, PluginLoader> fileAssociations = (HashMap<String, PluginLoader>) files;
	        for(PluginLoader p : fileAssociations.values()) {
	        	
	        	try {
	        		Object map2 = getPrivateField(p, "classLoaders");
			        @SuppressWarnings("unchecked")
			        HashMap<String, PluginClassLoader> classes = (HashMap<String, PluginClassLoader>) map2;
			        
			        Object map4 = getPrivateField(p, "classes");
			        @SuppressWarnings({ "unchecked", "rawtypes" })
			        HashMap<String, Class> packs = (HashMap<String, Class>) map4;
			        			        
			        
			        PluginClassLoader loader = classes.get(plugin.getName());
			        @SuppressWarnings({ "unchecked", "rawtypes" })
			        HashMap<String, Class> classess = (HashMap<String, Class>) getPrivateField(loader, "classes");
			        for(Class<?> s : classess.values()) {
			      
			        	packs.remove(s.getName());
			        	
			        	if(cmdds.containsKey(s.getName())) for(String ss : cmdds.get(s.getName()).split(">>>")) if(!commands.contains(knownCommands.get(ss))) commands.add(knownCommands.get(ss));
			        	
			        }
			        
			        commands = findCommands(plugin);
			        
			        for(Command c : commands) {
			        	
			        	unregisterCommand(c);
			        }
			        
			        classess.clear();
			   
			        
			        classes.remove(plugin.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}

	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	}
	
	public static List<Command> findCommands(Plugin plugin) {
		
		List<Command> commands = new ArrayList<>();
		
		for(Object c : plugin.getDescription().getCommands().values()) {
			if(c instanceof Command) {
				Command command = (Command) c;
				commands.add(command);
			}
		}
		
		try {
	    
	        Object cmds = getPrivateField(Server.getInstance().getCommandMap(), "knownCommands");
	        @SuppressWarnings("unchecked")
	        HashMap<String, Command> knownCommands = (HashMap<String, Command>) cmds;
	        
	        HashMap<String, String> cmdds = new HashMap<>();

	        for(Command c : Server.getInstance().getCommandMap().getCommands().values()) {
	        	try {
	        		if(cmdds.containsKey(c.getClass().getName())) {
		        		cmdds.put(c.getClass().getName(),cmdds.get(c.getClass().getName()) + ">>>" + c.getName());
		        	} else cmdds.put(c.getClass().getName(), c.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	     
	        
	        Object files = getPrivateField(Server.getInstance().getPluginManager(), "fileAssociations");
	        @SuppressWarnings("unchecked")
	        HashMap<String, PluginLoader> fileAssociations = (HashMap<String, PluginLoader>) files;
	        for(PluginLoader p : fileAssociations.values()) {
	        	
	        try {
	        	Object map2 = getPrivateField(p, "classLoaders");
		        @SuppressWarnings("unchecked")
		        HashMap<String, PluginClassLoader> classes = (HashMap<String, PluginClassLoader>) map2;
		        
		        
		        
		        PluginClassLoader loader = classes.get(plugin.getName());
		        Object map3 = getPrivateField(loader, "classes");
		        @SuppressWarnings({ "unchecked", "rawtypes" })
		        HashMap<String, Class> classess = (HashMap<String, Class>) map3;
		        for(Class<?> s : classess.values()) {	
		        	try {
		        		if(cmdds.containsKey(s.getName())) for(String ss : cmdds.get(s.getName()).split(">>>")) if(!commands.contains(knownCommands.get(ss))) commands.add(knownCommands.get(ss));
					} catch (Exception e) {
						e.printStackTrace();
					}
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
	
	        }

	       
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return commands;
	}
	
}
