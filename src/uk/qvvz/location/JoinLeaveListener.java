package uk.qvvz.location;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LocationRecorder.Record(player);
    }
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        LocationRecorder.Record(player);
    }
}