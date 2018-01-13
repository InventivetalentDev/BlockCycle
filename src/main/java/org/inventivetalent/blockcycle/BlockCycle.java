package org.inventivetalent.blockcycle;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlockCycle extends JavaPlugin implements Listener {

	ItemStack itemTypeCycler;
	ItemStack itemDataCycler;

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);

		itemTypeCycler = new ItemStack(Material.STICK);
		ItemMeta typeMeta = itemTypeCycler.getItemMeta();
		typeMeta.setDisplayName("§aBlock Type Cycler");
		itemTypeCycler.setItemMeta(typeMeta);

		itemDataCycler = new ItemStack(Material.STICK);
		ItemMeta dataMeta = itemDataCycler.getItemMeta();
		dataMeta.setDisplayName("§aBlock Data Cycler");
		itemDataCycler.setItemMeta(dataMeta);

		new Metrics(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			if (sender.hasPermission("blockcycle.item.type")) {
				sender.sendMessage("§a/bc type");
			}
			if (sender.hasPermission("blockcycle.item.data")) {
				sender.sendMessage("§a/bc data");
			}
			return true;
		}
		boolean type = "type".equalsIgnoreCase(args[0]);
		boolean data = "data".equalsIgnoreCase(args[0]);

		if (sender instanceof Player) {
			if (type) {
				if (sender.hasPermission("blockcycle.item.type")) {
					((Player) sender).getInventory().addItem(itemTypeCycler);
					return true;
				} else {
					sender.sendMessage("§cNo permission");
					return false;
				}
			}
			if (data) {
				if (sender.hasPermission("blockcycle.item.data")) {
					((Player) sender).getInventory().addItem(itemDataCycler);
					return true;
				} else {
					sender.sendMessage("§cNo permission");
					return false;
				}
			}
			if (sender.hasPermission("blockcycle.item.type")) {
				sender.sendMessage("§c/bc type");
			}
			if (sender.hasPermission("blockcycle.item.data")) {
				sender.sendMessage("§c/bc data");
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> list = new ArrayList<>();

		if (args.length == 1) {
			if (sender.hasPermission("blockcycle.item.type")) {
				list.add("type");
			}
			if (sender.hasPermission("blockcycle.item.data")) {
				list.add("data");
			}
		}

		return org.inventivetalent.blockcycle.TabCompletionHelper.getPossibleCompletionsForGivenArgs(args, list.toArray(new String[list.size()]));
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (!event.getPlayer().hasPermission("blockcycle.cycle")) { return; }
		Block block = event.getClickedBlock();
		if (block == null) { return; }
		ItemStack hand = event.getPlayer().getInventory().getItemInMainHand();
		if (hand == null) { return; }

		Block target = event.getPlayer().getTargetBlock((Set<Material>) null, 5);
		if (target != null) { block = target; }

		if (itemTypeCycler.isSimilar(hand)) {
			cycleBlockType(event.getPlayer(), block);
			event.setCancelled(true);
		}
		if (itemDataCycler.isSimilar(hand)) {
			cycleBlockData(event.getPlayer(), block);
			event.setCancelled(true);
		}
	}

	void cycleBlockType(Player player, Block block) {
		int currentId = block.getType().ordinal();
		int nextId = player.isSneaking() ? currentId - 1 : currentId + 1;
		if (nextId >= Material.values().length) { nextId = 0; }
		if (nextId < 0) { nextId = Material.values().length - 1; }
		Material next = Material.values()[nextId];
		block.setType(next, false);

		player.sendMessage("§aCurrent type: §b" + block.getType());
	}

	void cycleBlockData(Player player, Block block) {
		if (!player.isSneaking()) {
			byte next = (byte) (block.getData() + 1);
			try {
				block.setData(next, false);
			} catch (Exception e) {
				player.sendMessage("§cCould not update data");
				block.setData((byte) 0);
			}
		}

		player.sendMessage("§aCurrent data: §b" + block.getData());
	}
}
