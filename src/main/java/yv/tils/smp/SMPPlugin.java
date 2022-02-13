package yv.tils.smp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import yv.tils.smp.Placeholder.AnnouncementPlaceholder;
import yv.tils.smp.Placeholder.LanguagePlaceholder;
import yv.tils.smp.commands.*;
import yv.tils.smp.commands.autocompleter.FlySpeedAutoCompleter;
import yv.tils.smp.commands.autocompleter.GamemodeAutoCompleter;
import yv.tils.smp.commands.autocompleter.ModerationAutoCompleter;
import yv.tils.smp.commands.autocompleter.VanishAutoCompleter;
import yv.tils.smp.commands.replacecommands.*;
import yv.tils.smp.listeners.JoinListener;
import yv.tils.smp.listeners.MotdListener;
import yv.tils.smp.listeners.QuitListener;
import yv.tils.smp.listeners.SpawnBoostListener;
import yv.tils.smp.otherfiles.CustomCraftingRecipes;
import yv.tils.smp.utils.ConfigModeration;
import yv.tils.smp.utils.ConfigVersionUpdateChecker;
import yv.tils.smp.utils.UpdateChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class SMPPlugin extends JavaPlugin {

    private static SMPPlugin instance;
    DamageKickCommand damageKickCommand = new DamageKickCommand();
    ChatMuteCommand chatMuteCommand = new ChatMuteCommand();
    FlyCommand flyCommand = new FlyCommand();
    VanishCommand vanishCommand = new VanishCommand();
    GodCommand godCommand = new GodCommand();
    public final List<UUID> fly = new ArrayList<>();
    public final List<UUID> fly1 = new ArrayList<>();
    public List<UUID> vanished = new ArrayList();
    public List<UUID> godmode = new ArrayList();
    public List<UUID> godmode1 = new ArrayList();
    private HashMap<UUID, UUID> recentMessages;

    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(LanguagePlaceholder.StartMessage());
        saveDefaultConfig();
        ConfigModeration configModeration = new ConfigModeration();
        configModeration.onNameGenerate();
        configModeration.onEntranceGeneration();
        registerListener();
        registerCommands();
        registerTabCompleter();
        registerCommandReplace();
        recentMessages = new HashMap<>();
        if (getConfig().getBoolean("StartupAnnouncement")) {
            Bukkit.getConsoleSender().sendMessage(AnnouncementPlaceholder.STARTUPANNOUNCE());
        }
        if (getConfig().getBoolean("CustomRecipes")) {
            new CustomCraftingRecipes().addToCraftingManager();
        }
        new UpdateChecker(this, 97642).getLatestVersion(version -> {
            if(this.getDescription().getVersion().equalsIgnoreCase(version)) {
                Bukkit.getConsoleSender().sendMessage(LanguagePlaceholder.UpToDate());
            } else {
                Bukkit.getConsoleSender().sendMessage(LanguagePlaceholder.UpdateAviaible());
            }
        });
        Bukkit.getConsoleSender().sendMessage(LanguagePlaceholder.StartCompletedMessage());
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(LanguagePlaceholder.StopMessage());

        new ConfigModeration().onSave();

        Bukkit.getConsoleSender().sendMessage(LanguagePlaceholder.StopCompletedMessage());
    }

    private void registerCommands() {
        getCommand("chatmute").setExecutor(chatMuteCommand);
        getCommand("afkdamage").setExecutor(damageKickCommand);
        getCommand("fly").setExecutor(flyCommand);
        getCommand("vanish").setExecutor(vanishCommand);
        getCommand("flywalkspeed").setExecutor(new FlyWalkSpeed());
        getCommand("start").setExecutor(new StartCommand());
        getCommand("moderation").setExecutor(new ModerationCommand());
        getCommand("gm").setExecutor(new GamemodeCommand());
        getCommand("directmessage").setExecutor(new MessageCommand());
        getCommand("reply").setExecutor(new ReplyCommand());
        getCommand("god").setExecutor(godCommand);
        getCommand("heal").setExecutor(new HealCommand());
    }

    private void registerTabCompleter() {
        getCommand("moderation").setTabCompleter(new ModerationAutoCompleter());
        getCommand("flywalkspeed").setTabCompleter(new FlySpeedAutoCompleter());
        getCommand("gm").setTabCompleter(new GamemodeAutoCompleter());
        getCommand("vanish").setTabCompleter(new VanishAutoCompleter());
    }

    private void registerListener() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(chatMuteCommand, this);
        manager.registerEvents(damageKickCommand, this);
        manager.registerEvents(flyCommand, this);
        manager.registerEvents(vanishCommand, this);
        manager.registerEvents(godCommand, this);
        manager.registerEvents(new JoinListener(), this);
        manager.registerEvents(new QuitListener(), this);
        manager.registerEvents(new MotdListener(), this);
        manager.registerEvents(new SpawnBoostListener(this), this);
        manager.registerEvents(new ConfigVersionUpdateChecker(), this);
    }

    private void registerCommandReplace() {
        PluginManager manager = Bukkit.getPluginManager();
        if (getConfig().getBoolean("CommandBlock./seed")) {
            manager.registerEvents(new seedcommand(), this);
        }
        if (getConfig().getBoolean("CommandBlock./pardon")) {
            manager.registerEvents(new pardonCommand(), this);
        }
        if (getConfig().getBoolean("CommandBlock./msg")) {
            manager.registerEvents(new msgCommand(), this);
        }
        if (getConfig().getBoolean("CommandBlock./kick")) {
            manager.registerEvents(new KickCommand(), this);
        }
        if (getConfig().getBoolean("CommandBlock./ban")) {
            manager.registerEvents(new BanCommand(), this);
        }
    }
    public static SMPPlugin getInstance() {
        return instance;
    }
    public HashMap<UUID, UUID> getRecentMessages() {
        return recentMessages;
    }
}
