package com.skytechbytes.playerstatuebuilder;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.skytechbytes.playerstatuebuilder.builder.Schematic;
import com.skytechbytes.playerstatuebuilder.builder.SchematicUtil;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author SkyTechBytes
 *
 */
public class CommandUndostatue implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String commandLabel, String[] args) {
		if (!(sender instanceof Player p)) {
			return true;
		}

		try {
			if (!p.hasPermission("playerstatuebuilderx.undo")) {
				throw new Exception("Insufficient permissions.");
			}

			Schematic s = null;

			if (!Schematic.history.isEmpty()) {
				s = Schematic.history.pop();
			}

			if (s == null) {
				throw new Exception("There is no statue to undo right now.");
			}
			boolean canBuild = SchematicUtil.canBuild(s, p);

			if (!canBuild) {
				throw new Exception("Insufficient build permissions. That statue is in a protected location!");
			}

			s.createSchematic(true, !p.hasPermission("playerstatuebuilderx.override"));

			sender.sendMessage(ChatColor.GREEN + "Undo successful.");
		} catch (Exception e) {
			sender.sendMessage(ChatColor.RED + "Error! " + e.getMessage());
		}

		return true;
	}
}
