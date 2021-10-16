package uk.qvvz.location;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
	private FileConfiguration config;
	
	public ConfigManager(Plugin plugin) {
		config = plugin.getConfig();
		
		config.addDefault("printToConsole", false);
		config.options().copyDefaults(true);
		
		plugin.saveConfig();
	}
	
	public boolean printToConsole() {
		return config.getBoolean("printToConsole");
	}
}
