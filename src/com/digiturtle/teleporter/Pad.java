package com.digiturtle.teleporter;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class Pad {
	
	private Block plate;
	
	private Location location;
	
	public Pad(Block plate, Location location) {
		this.plate = plate;
		this.location = location;
	}
	
	public Block getPlate() {
		return plate;
	}
	
	public Location getLocation() {
		return location;
	}

}
