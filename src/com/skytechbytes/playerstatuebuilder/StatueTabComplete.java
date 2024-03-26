package com.skytechbytes.playerstatuebuilder;

import java.util.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

public class StatueTabComplete implements TabCompleter {
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
		if (!command.getName().equalsIgnoreCase("statue")) {
			return null;
		}

		if (!(sender instanceof Player p)) {
			return null;
		}

		if (args.length < 2) {
			return Collections.emptyList();
		}

		List<String> autoCompletions = new ArrayList<>();
		autoCompletions.add("glass");
		autoCompletions.add("concrete");
		autoCompletions.add("terracotta");
		autoCompletions.add("planks");
		autoCompletions.add("gray");
		autoCompletions.add("wool");
		autoCompletions.add("slim");
		autoCompletions.add("legacy");
		autoCompletions.add("default");
		autoCompletions.add("hue:");
		autoCompletions.add("iron_armor");
		autoCompletions.add("diamond_armor");
		autoCompletions.add("chainmail_armor");
		autoCompletions.add("golden_armor");
		autoCompletions.add("netherite_armor");
		if (p.hasPermission("playerstatuebuilderx.specialOrientations")) {
			autoCompletions.add("xy");
			autoCompletions.add("xz");
			autoCompletions.add("yz");
		}

		for (String arg : args) {
			autoCompletions.remove(arg);
		}

		return autoCompletions;
	}
}
