package yv.tils.smp.mods.admin.invsee;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import yv.tils.smp.utils.configs.language.LanguageFile;
import yv.tils.smp.utils.configs.language.LanguageMessage;
import yv.tils.smp.utils.internalapi.StringReplacer;

import java.util.*;

/**
 * @version 4.6.8
 * @since 4.6.8
 */
public class EcSee implements CommandExecutor {

    public static Map<UUID, UUID> ecSee = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) return false;
        if (args.length == 0) return false;

        if (!player.hasPermission("yvtils.smp.command.ecsee")) {
            player.sendMessage(LanguageFile.getMessage(LanguageMessage.MISSING_PERMISSION) + " yvtils.smp.command.ecsee");
            return false;
        }

        if (Bukkit.getPlayer(args[0]) == null) {
            player.sendMessage(LanguageFile.getMessage(LanguageMessage.PLAYER_NOT_ONLINE));
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        player.openInventory(getInventory(target));
        ecSee.put(player.getUniqueId(), target.getUniqueId());
        return false;
    }


    public Inventory getInventory(Player player) {

        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        list1.add("PLAYER");
        list2.add(player.getName());

        Inventory ec = Bukkit.createInventory(null, 27, new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.MODULE_INVSEE_ENDERCHEST), list1, list2));

        ItemStack[] ecContent = player.getEnderChest().getStorageContents();

        List<ItemStack> contents = new ArrayList<>(Arrays.asList(ecContent));

        ItemStack[] cont = contents.toArray(new ItemStack[0]);

        ec.setContents(cont);
        return ec;
    }
}
