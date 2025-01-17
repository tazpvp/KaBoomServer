package world.ntdi.api.playerwrapper;

import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class PlayerWrapperListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent p_playerJoinEvent) {
        PlayerWrapper.playerJoin(p_playerJoinEvent.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent p_playerQuitEvent) {
        PlayerWrapper.playerQuit(p_playerQuitEvent.getPlayer().getUniqueId());
    }
}
