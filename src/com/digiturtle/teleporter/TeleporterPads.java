package com.digiturtle.teleporter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TeleporterPads extends JavaPlugin {
	
	public static final TeleportPadRegistry PADS = new TeleportPadRegistry();
	
	public static final PadEditingState EDITING_STATE = new PadEditingState();

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equals("tpad") && sender instanceof Player) {
			if (args.length == 0 || args[0].equals("help")) {
				Player player = (Player) sender;
				player.sendMessage(ChatColor.GOLD + "Teleporter Pads Help");
				player.sendMessage(ChatColor.YELLOW + "/tpad create <name>: Create a Pad. Right click on the selected plate.");
				player.sendMessage(ChatColor.YELLOW + "/tpad destroy <name>: Destroy a Pad.");
				player.sendMessage(ChatColor.YELLOW + "/tpad link: Link the selected plate to your current location.");
				player.sendMessage(ChatColor.YELLOW + "/tpad list: List all active teleporter pads");
				player.sendMessage(ChatColor.YELLOW + "/tpad help: View this help page");
			}
			else if (args[0].equals("create") && sender.hasPermission("tpad.create")) {
				if (args.length < 2) {
					sender.sendMessage("/tpad create <name>");
				} else {
					EDITING_STATE.createPad((Player) sender, args[1]);
				}
			}
			else if (args[0].equals("link") && sender.hasPermission("tpad.create")) {
				Location location = ((Player) sender).getLocation();
				EDITING_STATE.linkPad((Player) sender, location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
			}
			else if (args[0].equals("destroy") && sender.hasPermission("tpad.destroy")) {
				if (args.length < 2) {
					sender.sendMessage("/tpad destroy <name>");
				} else {
					EDITING_STATE.destroyPad((Player) sender, args[1]);
				}
			}
			else if (args[0].equals("list") && sender.hasPermission("tpad.list")) {
				String[] pads = PADS.getList();
				Player player = (Player) sender;
				if (pads.length > 0) {
					player.sendMessage(ChatColor.GOLD + "Teleporter Pads List");
					for (int i = 0; i < pads.length; i++) {
						player.sendMessage(ChatColor.YELLOW + pads[i]);
					}
				} else {
					player.sendMessage(ChatColor.RED + "There are currently no teleporter pads.");
				}
			}
			return true;
		}
		return false;
	}
	
	public void onEnable() {
		File file = new File("plugins" + "/teleporter-pads.data");
		if (file.exists()) {
			String line;
			ArrayList<String> lines = new ArrayList<>();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
				while ((line = reader.readLine()) != null) {
					if (line.length() > 0) {
						lines.add(line);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			String[] arrayLines = lines.toArray(new String[0]);
			PADS.loadFromFile(arrayLines);
		}
		getServer().getPluginManager().registerEvents(new TeleportListener(), this);
	}
	
	public void onDisable() {
		String[] arrayLines = PADS.saveToFile();
		File file = new File("plugins" + "/teleporter-pads.data");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
			for (int i = 0; i < arrayLines.length; i++) {
				writer.write(arrayLines[i] + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
