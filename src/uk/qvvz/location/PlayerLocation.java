package uk.qvvz.location;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PlayerLocation extends JavaPlugin {
	BukkitTask recordTask;
	JoinLeaveListener loginListener;
	ConfigManager config;
	
	@Override
    public void onEnable() {
		config = new ConfigManager(this);
		LocationRecorder.setPlugin(config);
				
		loginListener = new JoinLeaveListener();
		getServer().getPluginManager().registerEvents(loginListener, this);
		
		// Spit 
		int frequencyMins = 1;
		int tps = 20;
		recordTask = new BukkitRunnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()){
					LocationRecorder.Record(p);
				}
			}
		}.runTaskTimer(this, 0L, tps * (60 * frequencyMins));
    }
	
	@Override
    public void onDisable() {
		// TODO - Unregister the MyListener Event
		HandlerList.unregisterAll(loginListener);

		recordTask.cancel();
    }
}
