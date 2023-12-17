package yv.tils.smp.manager.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import yv.tils.smp.mods.server.connect.PlayerQuit;

/**
 * @since 4.6.6
 * @version CH2-1.0.0
 */
public class QuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        new PlayerQuit().onPlayerQuit(e);
    }
}