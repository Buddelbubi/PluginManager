package de.buddelbubi.utils;

import cn.nukkit.Player;

import cn.nukkit.Server;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.plugin.Plugin;
import de.buddelbubi.PluginManagerInstance;

public class WindowFactory {
	
	public static void openPluginListWindow(Player p) {
		
		FormWindowSimple fw = new FormWindowSimple(PluginManagerInstance.prefix + "§eInstalled Plugins", "§7Select a plugin to continue.");
		
		for(Plugin pl : Server.getInstance().getPluginManager().getPlugins().values()) {
			
			String img = pl.getDescription().getWebsite();
			if(img != null)
			if(img.contains("cloudburstmc.org/resources/") || img.contains("nukkitx.com/resources/")) {
				try {
					img = "https://www.cloudburstmc.org/data/resource_icons/0/" + img.split("\\.")[img.split("\\.").length-1].replace("/", ".jpg");
				} catch (Exception e) {
					
				}
			}
			fw.addButton(new ElementButton("§7" + TextFormat.getFormatedPluginName(pl), pl.getDescription().getWebsite() != null ? new ElementButtonImageData("url", img) : new ElementButtonImageData("path", "textures/ui/gear.png")));
			
		}
		p.showFormWindow(fw);
		
	}
	
}
