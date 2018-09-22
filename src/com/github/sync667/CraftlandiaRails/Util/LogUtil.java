package com.github.sync667.CraftlandiaRails.Util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;

public final class LogUtil{
    public static void sendError(CommandSender sender, String message) {
        display(sender, ChatColor.DARK_GREEN + "[CraftlandiaRails] " + ChatColor.RED + message);
    }

    public static void sendSuccess(CommandSender sender, String message) {
        display(sender, ChatColor.DARK_GREEN + "[CraftlandiaRails] " + ChatColor.YELLOW + message);
    }

    private static void display(CommandSender sender, String message) {
		if (sender != null && (sender instanceof Player) && ((Player) sender).isOnline()) {
			sender.sendMessage(message);
		} else {
			CraftlandiaRails.log.info(ChatColor.stripColor(message));
		}
    }
}
