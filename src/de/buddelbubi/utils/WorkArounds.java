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
	
	
	
	@SuppressWarnings("unused")
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
	    	
	        Object map = getPrivateField(Server.getInstance().getCommandMap(), "knownCommands");
	        @SuppressWarnings("unchecked")
	        HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
	       if(knownCommands.containsKey(cmd.getName())) knownCommands.remove(cmd.getName());
	        for (String alias : cmd.getAliases()){
	           if(knownCommands.containsKey(alias)){
	                knownCommands.remove(alias);
	            }
	        }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	//Jup. You cant read that code. I cant neighter.. After 4 hours of testing it finally works and I dont want to touch it again. :)
	
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
	        	if(cmdds.containsKey(c.getClass().getName())) {
	        		cmdds.put(c.getClass().getName(),cmdds.get(c.getClass().getName()) + ">>>" + c.getName());
	        	} else cmdds.put(c.getClass().getName(), c.getName());
	        }
	        
	        Object files = getPrivateField(Server.getInstance().getPluginManager(), "fileAssociations");
	        @SuppressWarnings("unchecked")
	        HashMap<String, PluginLoader> fileAssociations = (HashMap<String, PluginLoader>) files;
	        for(PluginLoader p : fileAssociations.values()) {
	        	
	        	Object map2 = getPrivateField(p, "classLoaders");
		        @SuppressWarnings("unchecked")
		        HashMap<String, PluginClassLoader> classes = (HashMap<String, PluginClassLoader>) map2;
		        
		        Object map4 = getPrivateField(p, "classes");
		        @SuppressWarnings({ "unchecked", "rawtypes" })
		        HashMap<String, Class> packs = (HashMap<String, Class>) map4;
		        
		       // for(String s : packs.keySet()) System.out.println("1 " + s);
		        
		        
		        PluginClassLoader loader = classes.get(plugin.getName());
		        Object map3 = getPrivateField(loader, "classes");
		        @SuppressWarnings({ "unchecked", "rawtypes" })
		        HashMap<String, Class> classess = (HashMap<String, Class>) map3;
		        for(Class<?> s : classess.values()) {
		      
		        	packs.remove(s.getName());
		        	
		        	if(cmdds.containsKey(s.getName())) for(String ss : cmdds.get(s.getName()).split(">>>")) if(!commands.contains(knownCommands.get(ss))) commands.add(knownCommands.get(ss));
		        	
		        }
		  
		        for(Command c : commands) unregisterCommand(c);
		        
		        classess.clear();
		      
		        classes.remove(plugin.getName());

	        }
	       
	       
	        
	        
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static List<Command> findCommands(Plugin plugin) {
		
		List<Command> commands = new ArrayList<>();
		
	    try {
	    
	        Object cmds = getPrivateField(Server.getInstance().getCommandMap(), "knownCommands");
	        @SuppressWarnings("unchecked")
	        HashMap<String, Command> knownCommands = (HashMap<String, Command>) cmds;
	        
	        HashMap<String, String> cmdds = new HashMap<>();

	        for(Command c : Server.getInstance().getCommandMap().getCommands().values()) {
	       
	        	if(cmdds.containsKey(c.getClass().getName())) {
	        		cmdds.put(c.getClass().getName(),cmdds.get(c.getClass().getName()) + ">>>" + c.getName());
	        	} else cmdds.put(c.getClass().getName(), c.getName());
	        }
	     
	        
	        Object files = getPrivateField(Server.getInstance().getPluginManager(), "fileAssociations");
	        @SuppressWarnings("unchecked")
	        HashMap<String, PluginLoader> fileAssociations = (HashMap<String, PluginLoader>) files;
	        for(PluginLoader p : fileAssociations.values()) {
	        	
	        	Object map2 = getPrivateField(p, "classLoaders");
		        @SuppressWarnings("unchecked")
		        HashMap<String, PluginClassLoader> classes = (HashMap<String, PluginClassLoader>) map2;

		        
		        PluginClassLoader loader = classes.get(plugin.getName());
		        Object map3 = getPrivateField(loader, "classes");
		        @SuppressWarnings({ "unchecked", "rawtypes" })
		        HashMap<String, Class> classess = (HashMap<String, Class>) map3;
		        for(Class<?> s : classess.values()) {	
		        	if(cmdds.containsKey(s.getName())) for(String ss : cmdds.get(s.getName()).split(">>>")) if(!commands.contains(knownCommands.get(ss))) commands.add(knownCommands.get(ss));
		        }
	
	        }

	       
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return commands;
	}
	
}
