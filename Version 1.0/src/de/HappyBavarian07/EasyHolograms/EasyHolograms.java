package de.HappyBavarian07.EasyHolograms;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.HappyBavarian07.apis.FileManager;
import de.HappyBavarian07.apis.Fireworkgenerator;
import de.HappyBavarian07.apis.StartUpLogger;
import de.HappyBavarian07.commands.HoloAddCommand;
import de.HappyBavarian07.commands.TabCompleterClass;
import net.minecraft.server.v1_15_R1.WorldServer;

public class EasyHolograms extends JavaPlugin {
	
	StartUpLogger logger;
	static FileManager fm;
	Fireworkgenerator fwg;
	static EasyHolograms instance;
	PluginManager pm;
	
	@Override
	public void onEnable() {
		setInstance(this);
		setLogger(new StartUpLogger());
		setFm(new FileManager(getInstance()));
		setFwg(new Fireworkgenerator(getInstance()));
		setPm(Bukkit.getPluginManager());
		
		//Creating Files and saving Config
		saveDefaultConfig();
		fm.createFile("", "holograms", "yml");
		
		//Loading Holograms
		
		loadHolos();
		
		
		getCommand("hologram").setExecutor(new HoloAddCommand(getInstance(), fm.getConfig("", "holograms", "yml"), fm.getFile("", "holograms", "yml")));
		getCommand("hologram").setTabCompleter(new TabCompleterClass());
		logger.spacer().message("§r[§4Easy§6Holograms§r] §aenabled!").spacer();
	}
	
	@Override
	public void onDisable() {
		setFm(null);
		setFwg(null);
		setPm(null);
		
		HandlerList.unregisterAll(getInstance());
		
		PluginCommand cmd1 = getCommand("hologram");
		
		unRegisterBukkitCommand(cmd1);
		
		setInstance(null);
		logger.spacer().message("§r[§4Easy§6Holograms§r] §adisabled!").spacer();
		setLogger(null);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public void loadHolos() {
		FileConfiguration data = fm.getConfig("", "holograms", "yml");
		if(data.getConfigurationSection("Holograms") != null) {
			data.getConfigurationSection("Holograms").getKeys(false).forEach(holo -> {
				String name = data.getString("Holograms." + holo + ".Name");
				UUID uuid = UUID.fromString(data.getString("Holograms." + holo + ".UUID"));
				String text = data.getString("Holograms." + holo + ".Text").replace('&', '§');
//				List<String> text = data.getStringList("Holograms." + holo + ".Text");
				World w = Bukkit.getWorld(data.getString("Holograms." + holo + ".World"));
				double x = data.getDouble("Holograms." + holo + ".X");
				double y = data.getDouble("Holograms." + holo + ".Y");
				double z = data.getDouble("Holograms." + holo + ".Z");
				float yaw = (float) data.getDouble("Holograms." + holo + ".Yaw");
				float pitch = (float) data.getDouble("Holograms." + holo + ".Pitch");
				Location loc = new Location(w, x, y, z, yaw, pitch);
				
//				Hologram hologram = new Hologram(loc, name, getListAsString(text), uuid);
				Hologram hologram = new Hologram(loc, name, text, uuid);
				if(data.getConfigurationSection("Holograms").contains(name)) {
					if(!w.getEntities().contains(hologram)) {
						WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
						world.addEntity(hologram);
						return;
					} else {
//						if(!Bukkit.getEntity(uuid).getCustomName().equals(getListAsString(text))) {
//							Bukkit.getEntity(uuid).setCustomName(getListAsString(text));
//							return;
//						}
						if(!Bukkit.getEntity(uuid).getCustomName().equals(text)) {
							Bukkit.getEntity(uuid).setCustomName(text);
							return;
						}
					}
				} else {
					if(Bukkit.getEntity(uuid) != null)
						Bukkit.getEntity(hologram.getUniqueID()).remove();
				}
			});
		}
	}
	
	public String getListAsString(List<String> list) {
		String text = "";
		for(int i = 0; i < list.size(); i++) {
			text = text + list.get(i) + " \n ";
		}
		return text.replace('&', '§');
	}
	
	private Object getPrivateField(Object object, String field)throws SecurityException,
    	NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = object.getClass();
    	Field objectField = clazz.getDeclaredField(field);
    	objectField.setAccessible(true);
    	Object result = objectField.get(object);
    	objectField.setAccessible(false);
    	return result;
	}
	
	public void unRegisterBukkitCommand(PluginCommand cmd) {
		try {
			Object result = getPrivateField(this.getServer().getPluginManager(), "commandMap");
			SimpleCommandMap commandMap = (SimpleCommandMap) result;
			Object map = getPrivateField(commandMap, "knownCommands");
			@SuppressWarnings("unchecked")
			HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
			knownCommands.remove(cmd.getName());
			for (String alias : cmd.getAliases()){
				if(knownCommands.containsKey(alias) && knownCommands.get(alias).toString().contains(this.getName())){
					knownCommands.remove(alias);
				}
			}
		} catch (Exception e) {
			return;
		}
	}
	
	public static FileConfiguration getHoloConfig() {
		return fm.getConfig("", "holograms", "yml");
	}
	
	public static File getHoloFile() {
		return fm.getFile("", "holograms", "yml");
	}
	
	public PluginManager getPm() {
		return pm;
	}

	public void setPm(PluginManager pm) {
		this.pm = pm;
	}

	public StartUpLogger getSLogger() {
		return logger;
	}

	public static EasyHolograms getInstance() {
		return instance;
	}

	private void setInstance(EasyHolograms instance) {
		EasyHolograms.instance = instance;
	}

	public FileManager getFm() {
		return fm;
	}

	public Fireworkgenerator getFwg() {
		return fwg;
	}

	private void setLogger(StartUpLogger logger) {
		this.logger = logger;
	}

	private void setFm(FileManager fm) {
		EasyHolograms.fm = fm;
	}

	private void setFwg(Fireworkgenerator fwg) {
		this.fwg = fwg;
	}
}
