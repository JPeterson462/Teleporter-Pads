package com.digiturtle.teleporter;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PadEditingState {
	
	private HashMap<Player, CreationState> creationStates = new HashMap<>();

	private class CreationState {
		
		private String name;
		
		private Block block;
		
		private CreationState(String name) {
			this.name = name;
		}
		
	}
	
	public void createPad(Player player, String name) {
		creationStates.put(player, new CreationState(name));
		player.sendMessage(ChatColor.YELLOW + "Pad created. Use /tpad link to link this pad to a location.");
	}
	
	public void linkPadBlock(Player player, Block block) {
		if (creationStates.containsKey(player)) {
			creationStates.get(player).block = block;
			player.sendMessage(ChatColor.YELLOW + "Pad '" + creationStates.get(player).name + "' linked to block.");
		} else {
			player.sendMessage(ChatColor.RED + "You must create a pad first with /tpad create <name>");
		}
	}
	
	public void linkPad(Player player, World world, int x, int y, int z) {
		if (creationStates.containsKey(player)) {
			CreationState state = creationStates.get(player);
			TeleporterPads.PADS.addTeleportPad(state.block, state.name, new Location(world, x, y, z));
			player.sendMessage(ChatColor.YELLOW + "Pad '" + creationStates.get(player).name + "' linked to location.");
			creationStates.remove(player);
		} else {
			player.sendMessage(ChatColor.RED + "You must create a pad first with /tpad create <name>");
		}
	}
	
	public void destroyPad(Player player, String name) {
		TeleporterPads.PADS.removeTeleportPad(name);
	}
	
}
