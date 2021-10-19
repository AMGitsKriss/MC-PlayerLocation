package uk.qvvz.location;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
	private FileConfiguration config;
	
	public ConfigManager(Plugin plugin) {
		config = plugin.getConfig();

		config.addDefault("printToConsole", false);
		config.addDefault("batchSize", 100);
		config.addDefault("apiEndpoint", "https://localhost:44343/recorder/add");
		config.options().copyDefaults(true);
		
		plugin.saveConfig();
	}
	
	public boolean printToConsole() {
		return config.getBoolean("printToConsole");
	}
	
	public int batchSize() {
		return config.getInt("batchSize");
	}

	public String endpointUri() {
		return config.getString("apiEndpoint");
	}
}
