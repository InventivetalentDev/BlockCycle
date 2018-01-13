package org.inventivetalent.blockcycle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to help with the TabCompletion for Bukkit.
 *
 * @author D4rKDeagle
 */

class TabCompletionHelper {

	public static List<String> getPossibleCompletionsForGivenArgs(String[] args, String[] possibilitiesOfCompletion) {
		final String argumentToFindCompletionFor = args[args.length - 1];

		final List<String> listOfPossibleCompletions = new ArrayList<>();
		for (int i = 0; i < possibilitiesOfCompletion.length; i++) {
			final String[] foundString = possibilitiesOfCompletion;
			try {
				if (foundString[i] != null && foundString[i].regionMatches(true, 0, argumentToFindCompletionFor, 0, argumentToFindCompletionFor.length())) {
					listOfPossibleCompletions.add(foundString[i]);
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		Collections.sort(listOfPossibleCompletions);

		return listOfPossibleCompletions;
	}

	public static List<String> getChatColors() {
		List<String> names = new ArrayList<>();
		for (ChatColor cc : ChatColor.values()) {
			if (!cc.isFormat() && cc != ChatColor.RESET) {
				names.add(cc.name());
			}
		}
		return names;
	}

	public static List<String> getDyeColors() {
		List<String> names = new ArrayList<>();
		for (DyeColor cc : DyeColor.values()) {
			names.add(cc.name());
		}
		return names;
	}

	public static List<String> getOnlinePlayerNames() {
		List<String> list = new ArrayList<>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			list.add(p.getName());
		}
		return list;
	}

}
