# PluginManager

![grafik](https://cloudburstmc.org/attachments/pluginmanagerlong-png.3442/


A Plugin to manage all of your servers plugins and also a tool to speed up plugin development



You can use PluginManager to load, unload, reload, enable, disable and uninstall plugins.

Yes. You can reload single plugins without the need to reload the entire server. This could save alot of time while developing plugins due the reduced loading times.



So what features does PluginManager have?


    Load Plugins

    Unload Plugins

    Reload Plugins

    Enable Plugins

    Disable Plugins

    Uninstall Plugins

    Manage Plugin Configuration files ingame

    Get plugin data

  Commands:

    /pluginmanager (opens the UI)

    /pluginmanager load [file] loads a plugin from a file.

    /pluginmanager unload [plugin] unloads the plugin.

    /pluginmanager reload [plugin] reloads the plugin.

    /pluginmanager enable [plugin] enables a disabled plugin.

    /pluginmanager disable [plugin] disables a enabled plugin.

    /reload [plugin] reloads the plugin without reloading the whole server.


You can use /pm instead of using /pluginmanager


Permissions:

Each Command has the permission of its sub command.

Like /pm load has the permission "pluginmanager.load"

The only exceptions are /pm (without subcommands) and /reload

/pm has the permission "pluginmanager.ui" and /reload remains with "nukkit.command.reload"

Just to be sure that no one of your team uninstalls any plugins, you have to be op to uninstall plugins.

To modify configs you need the permission "pluginmanager.config.*".

If you want them to have access to only certain configs, give them the permission with the file path.
