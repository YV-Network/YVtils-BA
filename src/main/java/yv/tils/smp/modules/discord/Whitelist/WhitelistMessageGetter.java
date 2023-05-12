package yv.tils.smp.modules.discord.Whitelist;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import yv.tils.smp.SMPPlugin;
import yv.tils.smp.modules.discord.EmbedManager.whitelist.*;
import yv.tils.smp.placeholder.MessagePlaceholder;
import yv.tils.smp.placeholder.StringReplacer;
import yv.tils.smp.utils.configs.discord.DiscordConfigManager;
import yv.tils.smp.utils.configs.language.LanguageFile;
import yv.tils.smp.utils.configs.language.LanguageMessage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @since 4.6.6
 * @version 4.6.8
 */
public class WhitelistMessageGetter extends ListenerAdapter {

    //DiscordName#Tag: Minecraft Username + UUID -> Example: WolfiiYV#3204: WolfiiYV aab8f297-b6f0-4ebb-a064-9968e1a1cc45

    YamlConfiguration config = new DiscordConfigManager().ConfigRequest();
    YamlConfiguration linkedRequest = new DiscordConfigManager().LinkedRequest();

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getChannel().getType().isMessage()) return;
        if (e.getAuthor().isBot()) return;
        if (e.getChannelType().compareTo(ChannelType.TEXT) != 0) return;

        TextChannel channel = e.getChannel().asTextChannel();

        String member = e.getMember().getUser().getAsTag();

        member = member.replace(",", "");
        member = member.replace("[", "");
        member = member.replace("]", "");

        if (e.getChannel().getId().equals(config.getString("WhitelistFeature.Channel"))) {
            String name = e.getMessage().getContentRaw();
            String MessageId = e.getMessageId();
            OfflinePlayer player = Bukkit.getOfflinePlayer(name);

            if (!name.matches("[a-zA-Z0-9_]+")) {
                channel.deleteMessageById(MessageId).queue();
                channel.sendMessageEmbeds(new AccountCantExist().Embed(e.getMessage().getContentRaw()).build()).complete().delete().queueAfter(5, TimeUnit.SECONDS);
                return;
            }

            if (player.isWhitelisted()) {
                channel.deleteMessageById(MessageId).queue();
                channel.sendMessageEmbeds(new AccountAlreadyListed().Embed(e.getMessage().getContentRaw()).build()).complete().delete().queueAfter(5, TimeUnit.SECONDS);
                return;
            }

            try {
                URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
                HttpURLConnection http = (HttpURLConnection)url.openConnection();
                int statusCode = http.getResponseCode();
                if (statusCode == 200) {
                    if (new ImportWhitelist().reader(member, null, null).contains(member)) {
                        List<String> whitelist = new ImportWhitelist().reader(member, null, null);
                        OfflinePlayer playerwhitelistremove = Bukkit.getOfflinePlayer(whitelist.get(1));
                        playerwhitelistremove.setWhitelisted(false);
                        whitelistRemove(member, playerwhitelistremove.getName(), playerwhitelistremove.getUniqueId().toString());

                        List<String> list1 = new ArrayList<>();
                        List<String> list2 = new ArrayList<>();
                        list1.add("DISCORDUSER");
                        list2.add(member);
                        list1.add("OLDNAME");
                        list2.add(whitelist.get(1));
                        list1.add("NEWNAME");
                        list2.add(e.getMessage().getContentRaw());

                        try {
                            try {
                                channel.deleteMessageById(MessageId).queue();
                                e.getGuild().addRoleToMember(e.getMember(), e.getGuild().getRoleById(new DiscordConfigManager().ConfigRequest().getLong("WhitelistFeature.Role"))).queue();
                                Bukkit.getConsoleSender().sendMessage(new StringReplacer().ListReplacer(MessagePlaceholder.PREFIXDC + " §f" + LanguageFile.getMessage(LanguageMessage.MODULE_DISCORD_REGISTERED_NAME_CHANGE), list1, list2));
                                channel.sendMessageEmbeds(new AccountChange().Embed(whitelist.get(1), e.getMessage().getContentRaw()).build()).complete().delete().queueAfter(5, TimeUnit.SECONDS);
                                player.setWhitelisted(true);
                                SMPPlugin.getInstance().WhitelistManager.add(member + "," + player.getName() + "," + player.getUniqueId());
                                new DiscordConfigManager().LinkedWriter(member, player.getName() + " " + player.getUniqueId());
                            }catch (HierarchyException ignored) {
                                channel.sendMessageEmbeds(new RoleHierarchyError().Embed(e.getGuild().getRoleById(new DiscordConfigManager().ConfigRequest().getLong("WhitelistFeature.Role"))).build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
                            }
                        }catch (IllegalArgumentException ignored) {}
                    }else {
                        List<String> list1 = new ArrayList<>();
                        List<String> list2 = new ArrayList<>();
                        list1.add("DISCORDUSER");
                        list2.add(member);
                        list1.add("NAME");
                        list2.add(e.getMessage().getContentRaw());

                        try {
                            try {
                                channel.deleteMessageById(MessageId).queue();
                                e.getGuild().addRoleToMember(e.getMember(), e.getGuild().getRoleById(new DiscordConfigManager().ConfigRequest().getLong("WhitelistFeature.Role"))).queue();
                                Bukkit.getConsoleSender().sendMessage(new StringReplacer().ListReplacer(MessagePlaceholder.PREFIXDC + " §f" + LanguageFile.getMessage(LanguageMessage.MODULE_DISCORD_REGISTERED_NAME_ADD), list1, list2));
                                channel.sendMessageEmbeds(new AccountAdded().Embed(e.getMessage().getContentRaw()).build()).complete().delete().queueAfter(5, TimeUnit.SECONDS);
                                player.setWhitelisted(true);
                                SMPPlugin.getInstance().WhitelistManager.add(member + "," + player.getName() + "," + player.getUniqueId());
                                new DiscordConfigManager().LinkedWriter(member, player.getName() + " " + player.getUniqueId());
                            }catch (HierarchyException ignored) {
                                channel.sendMessageEmbeds(new RoleHierarchyError().Embed(e.getGuild().getRoleById(new DiscordConfigManager().ConfigRequest().getLong("WhitelistFeature.Role"))).build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
                            }
                        }catch (IllegalArgumentException ignored) {}
                    }
                }else if (statusCode == 400) {
                    List<String> list1 = new ArrayList<>();
                    List<String> list2 = new ArrayList<>();
                    list1.add("DISCORDUSER");
                    list2.add(member);
                    list1.add("NAME");
                    list2.add(e.getMessage().getContentRaw());

                    channel.deleteMessageById(MessageId).queue();
                    Bukkit.getConsoleSender().sendMessage(new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.MODULE_DISCORD_REGISTERED_NAME_WRONG), list1, list2));
                    channel.sendMessageEmbeds(new AccountNotFound().Embed(e.getMessage().getContentRaw()).build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
                }else {
                    List<String> list1 = new ArrayList<>();
                    List<String> list2 = new ArrayList<>();
                    list1.add("DISCORDUSER");
                    list2.add(member);
                    list1.add("NAME");
                    list2.add(e.getMessage().getContentRaw());

                    channel.deleteMessageById(MessageId).queue();
                    Bukkit.getConsoleSender().sendMessage(new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.MODULE_DISCORD_REGISTERED_NAME_SERVERERROR_CHECK_INPUT), list1, list2));
                    channel.sendMessageEmbeds(new AccountCheckError().Embed(e.getMessage().getContentRaw()).build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
                }} catch (IOException ignored) {}}}


    private void whitelistAdd(String dc, String mc, String uuid) {
        SMPPlugin.getInstance().WhitelistManager.add(dc + "," + mc + "," + uuid);
    }

    private void whitelistRemove(String dc, String mc, String uuid) {
        SMPPlugin.getInstance().WhitelistManager.remove(dc + "," + mc + "," + uuid);
    }

}