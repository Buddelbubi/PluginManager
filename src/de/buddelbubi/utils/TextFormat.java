package de.buddelbubi.utils;

import cn.nukkit.plugin.Plugin;

public class TextFormat {

	public static String getFormatedPluginName(Plugin plugin) {
		return plugin.getName() + " v" + plugin.getDescription().getVersion();
	}
	
}
