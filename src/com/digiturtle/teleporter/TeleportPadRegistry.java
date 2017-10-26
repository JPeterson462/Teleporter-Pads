package com.digiturtle.teleporter;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class TeleportPadRegistry {
	
	private HashMap<String, Pad> pads = new HashMap<>();
	
	public void loadFromFile(String[] lines) {
		for (int i = 0; i < lines.length; i++) {
			String[] locations = lines[i].split(";");
			String padName = locations[0].trim();
			String padLocation = locations[1].trim();
			String targetLocation = locations[2].trim();
			pads.put(padName, new Pad(stringToLocation(padLocation).getBlock(), stringToLocation(targetLocation)));
		}
	}
	
	public void addTeleportPad(Block block, String name, Location location) {
		pads.put(name, new Pad(block, location));
	}
	
	public void removeTeleportPad(String name) {
		pads.remove(name);
	}
	
	public Location getTeleportLocation(Block block) {
		for (Map.Entry<String, Pad> blockToLocation : pads.entrySet()) {
			if (blockToLocation.getValue().getPlate().equals(block)) {
				return blockToLocation.getValue().getLocation();
			}
		}
		return null;
	}
	
	public String[] getList() {
		String[] lines = new String[pads.size()];
		int index = 0;
		for (Map.Entry<String, Pad> blockToLocation : pads.entrySet()) {
			Location from = blockToLocation.getValue().getPlate().getLocation();
			Location to = blockToLocation.getValue().getLocation();
			lines[index] = blockToLocation.getKey() + ": " + "From " + from.getWorld().getName() + "(" + from.getBlockX() + ", " + from.getBlockY() + ", " + from.getBlockZ() + ")" + 
								" To " + to.getWorld().getName() + "(" + to.getBlockX() + ", " + to.getBlockY() + ", " + to.getBlockZ() + ")";
			index++;
		}
		return lines;
	}
	
	public String[] saveToFile() {
		String[] lines = new String[pads.size()];
		int index = 0;
		for (Map.Entry<String, Pad> blockToLocation : pads.entrySet()) {
			lines[index] = blockToLocation.getKey() + ";" + locationToString(blockToLocation.getValue().getPlate().getLocation()) + ";" + locationToString(blockToLocation.getValue().getLocation());
			index++;
		}
		return lines;
	}
	
	private String locationToString(Location location) {
		return location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
	}
	
	private Location stringToLocation(String string) {
		String[] parts = string.split(",");
		String worldName = parts[0].trim();
		World world = Bukkit.getWorld(worldName);
		int x = Integer.parseInt(parts[1].trim());
		int y = Integer.parseInt(parts[2].trim());
		int z = Integer.parseInt(parts[3].trim());
		return world.getBlockAt(x, y, z).getLocation();
	}

}
