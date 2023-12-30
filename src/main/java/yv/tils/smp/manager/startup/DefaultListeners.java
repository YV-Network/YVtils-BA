package yv.tils.smp.manager.startup;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import yv.tils.smp.YVtils;
import yv.tils.smp.manager.listener.*;
import yv.tils.smp.mods.admin.logger.logger.*;
import yv.tils.smp.mods.other.SpawnElytra;

/**
 * @version CH2-1.0.0
 * @since 4.6.8
 */
public class DefaultListeners {
    YVtils main = YVtils.getInstance();

    public void registerListener() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new JoinListener(), main);
        manager.registerEvents(new QuitListener(), main);
        manager.registerEvents(new ChatListener(), main);
        manager.registerEvents(new LoginListener(), main);
        manager.registerEvents(new ServerListPingListener(), main);
        manager.registerEvents(new AirTimeListener(), main);
        manager.registerEvents(new DamageListener(), main);
        manager.registerEvents(new WorldChangeListener(), main);
        manager.registerEvents(new InventoryListener(), main);
        manager.registerEvents(new SpawnElytra(), main);
        manager.registerEvents(new PlayerCommandPreprocess(), main);
        manager.registerEvents(new EntityTargetListener(), main);
        manager.registerEvents(new GamemodeSwitch(), main);
        manager.registerEvents(new AdvancementAnnounce(), main);
    }

    public void registerLogger() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new BlockInteract(), main);
        manager.registerEvents(new PlayerServerEvent(), main);
        manager.registerEvents(new ChatEvent(), main);
        manager.registerEvents(new ChestInteract(), main);
        manager.registerEvents(new EntityKillEvent(), main);
        manager.registerEvents(new ItemInteract(), main);
    }
}
