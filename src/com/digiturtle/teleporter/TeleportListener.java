package com.digiturtle.teleporter;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TeleportListener implements Listener {

	@EventHandler
	public void onTeleportInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			TeleporterPads.EDITING_STATE.linkPadBlock(event.getPlayer(), event.getClickedBlock());
		}
		else if (event.getAction() == Action.PHYSICAL) {
			if (event.getClickedBlock().getType() == Material.STONE_PLATE) {
				Location linkedLocation = TeleporterPads.PADS.getTeleportLocation(event.getClickedBlock());
				if (linkedLocation != null) {
					event.getPlayer().teleport(linkedLocation);
					event.getPlayer().sendMessage(ChatColor.GOLD + "Teleporting...");
				}
			}
		}
	}
	
}
