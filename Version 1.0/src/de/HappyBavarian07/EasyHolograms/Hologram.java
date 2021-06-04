package de.HappyBavarian07.EasyHolograms;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;

import net.minecraft.server.v1_15_R1.ChatComponentText;
import net.minecraft.server.v1_15_R1.EntityArmorStand;
import net.minecraft.server.v1_15_R1.EntityTypes;

public class Hologram extends EntityArmorStand {

	public Hologram(Location loc, String name, String text, UUID uuid) {
		super(EntityTypes.ARMOR_STAND, ((CraftWorld) loc.getWorld()).getHandle());
		
		this.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		if(uuid.toString() != "") {
			this.uniqueID = uuid;
		}
		this.setCustomName(new ChatComponentText(text));
		this.setCustomNameVisible(true);
		this.addScoreboardTag("Hologram_Tag_Pleasenotdelete");
		this.setInvisible(true);
		this.setInvulnerable(true);
		this.setNoGravity(true);
		this.setSilent(true);
		this.setArms(false);
	}
}
