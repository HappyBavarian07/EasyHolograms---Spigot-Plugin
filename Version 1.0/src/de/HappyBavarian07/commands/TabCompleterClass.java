package de.HappyBavarian07.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import de.HappyBavarian07.EasyHolograms.EasyHolograms;

public class TabCompleterClass implements TabCompleter {

	List<String> commandargs1 = new ArrayList<String>();
	List<String> commandargs2 = new ArrayList<String>();
	List<String> commandargs3 = new ArrayList<String>();
	List<String> commandargs4 = new ArrayList<String>();
	List<String> holos = new ArrayList<String>();

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("hologram") || cmd.getName().equalsIgnoreCase("holo")) {
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("reload")) {
					commandargs3.add(0, "Config");
					commandargs3.add(1, "Holograms");
					commandargs3.add(2, "Plugin");
					commandargs3.add(3, "Pl");
					commandargs3.add(4, "Con");
					commandargs3.add(5, "Holos");
					
					List<String> result = new ArrayList<String>();
					if (args.length == 2) {
						for (String a : commandargs3) {
							if (a.toLowerCase().startsWith(args[0].toLowerCase()))
								result.add(a);
						}
						return result;
					}
					return commandargs3;
				}
				if (args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("remove")) {
					if (holos.isEmpty()) {
						for (int i = 0; i < EasyHolograms.getHoloConfig().getConfigurationSection("Holograms").getKeys(false).size(); i++) {
							holos.addAll(EasyHolograms.getHoloConfig().getConfigurationSection("Holograms").getKeys(false));
						}
					}
					
					List<String> result = new ArrayList<String>();
					if (args.length == 2) {
						for (String a : holos) {
							if (a.toLowerCase().startsWith(args[0].toLowerCase()))
								result.add(a);
						}
						return result;
					}
					return holos;
				}
				return null;
			}
			if (args.length == 1) {
				if (commandargs1.isEmpty()) {
					commandargs1.add("add");
					commandargs1.add("edit");
					commandargs1.add("remove");
					commandargs1.add("list");
					commandargs1.add("reload");
					commandargs1.add("help");
				}

				List<String> result = new ArrayList<String>();
				if (args.length == 1) {
					for (String a : commandargs1) {
						if (a.toLowerCase().startsWith(args[0].toLowerCase()))
							result.add(a);
					}
					return result;
				}
				return commandargs1;
			}
		}
		return null;
	}
}
