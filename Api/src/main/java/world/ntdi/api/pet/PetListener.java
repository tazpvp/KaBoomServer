package world.ntdi.api.pet;

import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class PetListener implements Listener {
    private final PetService m_petService;

    @EventHandler
    public void onJoin(PlayerJoinEvent p_playerJoinEvent) {
        m_petService.playerJoin(p_playerJoinEvent.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent p_playerQuitEvent) {
        m_petService.playerLeave(p_playerQuitEvent.getPlayer().getUniqueId());
    }
}