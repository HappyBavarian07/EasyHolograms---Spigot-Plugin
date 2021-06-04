package de.HappyBavarian07.commands;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.HappyBavarian07.EasyHolograms.EasyHolograms;
import de.HappyBavarian07.EasyHolograms.Hologram;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_15_R1.WorldServer;

public class HoloAddCommand implements CommandExecutor {
	
	EasyHolograms plugin;
	FileConfiguration holograms;
	File holofile;
	
	public HoloAddCommand(EasyHolograms plugin, FileConfiguration holograms, File holofile) {
		this.plugin = plugin;
		this.holograms = holograms;
		this.holofile = holofile;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(cmd.getName().equalsIgnoreCase("holo") || cmd.getName().equalsIgnoreCase("hologram")) {
				Player p = (Player) sender;
				if(args.length == 0) {
					p.sendMessage("§4You need more Arguments!");
					return true;
				}
				if(args[0].equalsIgnoreCase("add")) {
					if(args.length == 2) {
						if(p.hasPermission("easyholo.add")) {
							if(holograms.getConfigurationSection("Holograms") == null) {
								holograms.createSection("Holograms");
								try {
									holograms.save(holofile);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							if(holograms.getConfigurationSection("Holograms").contains(args[1])) {
								p.sendMessage("§4Error: §fHologram §b" + args[1] + "§f already exists!");
								return true;
							}
							String name = args[1].replace("&", "").replace("§", "");
							Location loc = p.getLocation().add(0, -1, 0);
							String text = plugin.getConfig().getString("Standard-Text").replace("[Holoname]", name).replace('&', '§');
							Hologram holo = new Hologram(loc, name, text, UUID.randomUUID());
							addHologram(loc, name, text, holo);
							p.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 80, (float) 1.0);
							p.sendMessage("§7Successfully created Hologram: §b" + name + "§7 in World: §b" + loc.getWorld().getName() + "§7 at X:§b" + (int) loc.getX() + "§7, Y:§b" + (int) loc.getY() + "§7, Z:§b" + (int) loc.getZ() + "§7!");
						} else {
							p.sendMessage("§4You don't have Permissions for that Command!");
							return true;
						}
					} else if(args.length > 2) {
						if(p.hasPermission("easyholo.add")) {
							if(holograms.getConfigurationSection("Holograms") == null) {
								holograms.createSection("Holograms");
								try {
									holograms.save(holofile);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							if(holograms.getConfigurationSection("Holograms").contains(args[1])) {
								p.sendMessage("§4Error: §fHologram §b" + args[1] + "§f already exists!");
								return true;
							}
							String name = args[1].replace("&", "").replace("§", "");
							Location loc = p.getLocation().add(0, -1, 0);
							String text = "";
							for(int i = 2; i < args.length; i++) {
								text = text + " " + args[i].replace('&', '§');
							}
							Hologram holo = new Hologram(loc, name, text, UUID.randomUUID());
							addHologram(loc, name, text, holo);
							p.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 80, (float) 1.0);
							p.sendMessage("§7Successfully created Hologram: §b" + name + "§7 in World: §b" + loc.getWorld().getName() + "§7 at X:§b" + (int) loc.getX() + "§7, Y:§b" + (int) loc.getY() + "§7, Z:§b" + (int) loc.getZ() + "§7!");
						} else {
							p.sendMessage("§4You don't have Permissions for that Command!");
							return true;
						}
					} else {
						p.sendMessage("§6Usage: §2/holo <add|edit|list|remove|reload|help §c(For more Infos /holo help)");
						return true;
					}
					return true;
				}
				if(args[0].equalsIgnoreCase("edit")) {
					p.sendMessage("§4§l§nStill under Development!");
					if(args.length == 2) {
						TextComponent edittools = new TextComponent();
						edittools.setText("§a§lEdit the Lines of Holo " + args[1]);
						TextComponent edit = new TextComponent();
						edit.setBold(true);
						edit.setText("  §e§l[Edit Line]");
						edit.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/holo edit " + args[1] + " editline <Line> <Text>"));
						TextComponent add = new TextComponent();
						add.setBold(true);
						add.setText(" §a§l[Add Line]");
						add.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/holo edit " + args[1] + " addline <Line> <Text>"));
						TextComponent remove = new TextComponent();
						remove.setBold(true);
						remove.setText(" §c§l[Remove Line]");
						remove.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/holo edit " + args[1] + " removeline <Line>"));
						TextComponent list = new TextComponent();
						list.setBold(true);
						list.setText(" §5§l[List Lines]");
						list.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/holo edit " + args[1] + " listlines"));
						edittools.addExtra(edit);
						edittools.addExtra(add);
						edittools.addExtra(remove);
						edittools.addExtra(list);
						p.spigot().sendMessage(edittools);
						return true;
					}
					if(args.length == 5) {
						if(holograms.getConfigurationSection("Holograms") == null) {
							holograms.createSection("Holograms");
							try {
								holograms.save(holofile);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if(!holograms.getConfigurationSection("Holograms").contains(args[1])) {
							p.sendMessage("§4Error: §fHologram §b" + args[1] + "§f doesn't exists!");
							return true;
						}
						String name = args[1].replace("&", "").replace("§", "");
						String text = args[4].replace('&', '§');
//						List<String> oldtext = holograms.getStringList("Holograms." + name + ".Text");
						if(args[2].equalsIgnoreCase("editline")) {
//							if(Integer.getInteger(args[3]) == null) {
//								p.sendMessage("§c" + args[3] + "§4 is not a Number!");
//								return true;
//							}
//							int line = Integer.parseInt(args[3]);
//							if(oldtext.size() < line) {
//								p.sendMessage("§4You must suggest a Line that exists!");
//								return true;
//							}
//							oldtext.set(line, text);
//							holograms.set("Holograms." + name + ".Text", oldtext);
							if(text == null) {
								p.sendMessage("Du musst einen Text angeben!");
								return true;
							}
							holograms.set("Holograms." + name + ".Text", text);
							try {
								holograms.save(holofile);
							} catch (IOException e) {
								e.printStackTrace();
							}
							plugin.loadHolos();
							p.sendMessage("§aSuccessfully edited the Text of §b" + name + "§a!");
							return true;
						}
						if(args[2].equalsIgnoreCase("addline")) {
							
						}
						if(args[2].equalsIgnoreCase("removeline")) {
							
						}
						if(args[2].equalsIgnoreCase("listlines")) {
							
						}
					}
					
					p.sendMessage("§6Usage: §2/holo <add|edit|list|remove|reload|help §c(For more Infos /holo help)");
					return true;
				}
				if(args[0].equalsIgnoreCase("remove")) {
					if(args.length == 2) {
						if(p.hasPermission("easyholo.remove")) {
							if(holograms.getConfigurationSection("Holograms") == null) {
								holograms.createSection("Holograms");
								try {
									holograms.save(holofile);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							String name = args[1].replace("&", "").replace("§", "");
							if(!holograms.getConfigurationSection("Holograms").contains(args[1]) || Bukkit.getEntity(UUID.fromString(holograms.getString("Holograms." + name + ".UUID"))) == null) {
								p.sendMessage("§4Error: §fHologram §b" + args[1] + "§f doesn't exists or the Hologram Entity UUID is damaged / broken!");
								if(p.isOp()) {
									p.sendMessage("§aWhat should i do if my Holograms are bugged:\n" + "§aPlease only execute this Command if the Hologram is in the Config else the Holo gets Deleted\n \n" + " §4With the Command /minecraft:kill @e[type=minecraft:armor_stand,distance:..2] all holograms within a radius of 2 blocks around you will be killed + enter /holo reload holograms!");
								}
								return true;
							}
							
							Bukkit.getEntity(UUID.fromString(holograms.getString("Holograms." + name + ".UUID"))).remove();
							holograms.set("Holograms." + name, null);
							try {
								holograms.save(holofile);
							} catch (IOException e) {
								e.printStackTrace();
							}
							p.playSound(p.getLocation(), Sound.ENTITY_BAT_DEATH, 80, (float) 1.0);
							p.sendMessage("§7Successfully removed Hologram §b"+ name + "§7!");
						} else {
							p.sendMessage("§4You don't have Permissions for that Command!");
							return true;
						}
					} else {
						p.sendMessage("§6Usage: §2/holo <add|edit|list|remove|reload|help §c(For more Infos /holo help)");
						return true;
					}
					return true;
				}
				if(args[0].equalsIgnoreCase("help")) {
					if(args.length == 1) {
						if(p.hasPermission("easyholo.help")) {
							p.sendMessage(""
									+ "§7+------------Easy-Holograms-Help-----------+\n"
									+ "§7  /holo add <Holoname>:\n"
									+ "§b      Adds a new Holo at your Head!\n"
									+ "§b  \n"
									+ "§7  /holo edit <Holoname>\n  <addline|editline|removeline|listlines>\n  [<Line>] [<Text>]:\n"
									+ "§b      Edit the Lines of a Holo or add new Lines!\n"
									+ "§b  \n"
									+ "§7  /holo remove <Holoname>:\n"
									+ "§b      Removes the Holo you entered!\n"
									+ "§b  \n"
									+ "§7  /holo list <Distance>:\n"
									+ "§b      List all Holos with Positions and Worlds\n"
									+ "§b  \n"
									+ "§7  /holo reload <Config(con)|Holograms(Holos)|Plugin(Pl)|all(**)>:\n"
									+ "§b      Reloads the Config / the Holograms\n"
									+ "§b  \n"
									+ "§7  /holo help:\n"
									+ "§b      Opens that Help GUI\n"
									+ "§7+------------------------------------------+");
						} else {
							p.sendMessage("§4You don't have Permissions for that Command!");
							return true;
						}
					} else {
						p.sendMessage("§6Usage: §2/holo <add|edit|list|remove|reload|help §c(For more Infos /holo help)");
						return true;
					}
					return true;
				}
				if(args[0].equalsIgnoreCase("reload")) {
					if(args.length == 2) {
						if(p.hasPermission("easyholo.reload")) {
							if(args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("*") || args[1].equalsIgnoreCase("**")) {
								EasyHolograms.getInstance().reloadConfig();
								p.sendMessage("§aReloaded Config successfully!");
								EasyHolograms.getInstance().loadHolos();
								p.sendMessage("§aReloaded all Holograms successfully!");
								Bukkit.getPluginManager().disablePlugin(plugin);
								Bukkit.getPluginManager().enablePlugin(plugin);
								p.sendMessage("§aReloaded the Plugin successfully!");
								return true;
							}
							if(args[1].equalsIgnoreCase("Config") || args[1].equalsIgnoreCase("con")) {
								EasyHolograms.getInstance().reloadConfig();
								p.sendMessage("§aReloaded Config successfully!");
								return true;
							}
							if(args[1].equalsIgnoreCase("Holograms") || args[1].equalsIgnoreCase("Holos")) {
								EasyHolograms.getInstance().loadHolos();
								p.sendMessage("§aReloaded all Holograms successfully!");
								return true;
							}
							if(args[1].equalsIgnoreCase("Plugin") || args[1].equalsIgnoreCase("Pl")) {
								Bukkit.getPluginManager().disablePlugin(plugin);
								Bukkit.getPluginManager().enablePlugin(plugin);
								p.sendMessage("§aReloaded the Plugin successfully!");
								return true;
							}
							p.sendMessage("§6Usage: §2/holo <add|edit|list|remove|reload|help §c(For more Infos /holo help)");
							return true;
						} else {
							p.sendMessage("§4You don't have Permissions for that Command!");
							return true;
						}
					} else {
						p.sendMessage("§6Usage: §2/holo <add|edit|list|remove|reload|help §c(For more Infos /holo help)");
						return true;
					}
				}
				if(args[0].equalsIgnoreCase("list")) {
					if(args.length == 2) {
						if(p.hasPermission("easyholo.list")) {
							p.sendMessage("§bHolograms§7:");
							for(Entity ent : p.getWorld().getEntities()) {
								if(Double.valueOf(args[1]) == null) {
									p.sendMessage("§4" + args[1] + " is not a Number!");
									return true;
								}
								if(isinSpawn(ent.getLocation(), p.getLocation(), Double.parseDouble(args[1]))) {
									if(EasyHolograms.getHoloConfig().getConfigurationSection("Holograms") == null) {
										EasyHolograms.getHoloConfig().createSection("Holograms");
										try {
											EasyHolograms.getHoloConfig().save(EasyHolograms.getHoloFile());
										} catch (IOException e) {
											e.printStackTrace();
										}
										break;
									}
									if(!ent.getScoreboardTags().contains("Hologram_Tag_Pleasenotdelete")) continue;
									holograms.getConfigurationSection("Holograms").getKeys(false).forEach(holo -> {
										if(ent.getUniqueId().equals(UUID.fromString(holograms.getString("Holograms." + holo + ".UUID")))) {
											p.sendMessage("§b" + holograms.getString("Holograms." + holo + ".Name"));
										}
									});
								}
							}
						}
					} else {
						p.sendMessage("§6Usage: §2/holo <add|edit|list|remove|reload|help §c(For more Infos /holo help)");
						return true;
					}
				}
				return true;
			}
		} else {
			sender.sendMessage("§4Only Players can executed this Command!");
			return true;
		}
		return true;
	}
	
	public boolean isinSpawn(Location block, Location plotMiddel, double radius){
        return plotMiddel.distance(block) < radius;
    }
	
	public void addHologram(Location loc, String name, String text, Hologram holo) {
		WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
		world.addEntity(holo);
		holograms.set("Holograms." + name + ".X", loc.getX());
		holograms.set("Holograms." + name + ".Y", loc.getY());
		holograms.set("Holograms." + name + ".Z", loc.getZ());
		holograms.set("Holograms." + name + ".Yaw", loc.getYaw());
		holograms.set("Holograms." + name + ".Pitch", loc.getPitch());
		holograms.set("Holograms." + name + ".World", loc.getWorld().getName());
		holograms.set("Holograms." + name + ".UUID", holo.getUniqueIDString());
		holograms.set("Holograms." + name + ".Text", text.replace('§', '&'));
		holograms.set("Holograms." + name + ".Name", name);
		try {
			holograms.save(holofile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
