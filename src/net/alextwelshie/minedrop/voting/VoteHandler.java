package net.alextwelshie.minedrop.voting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.alextwelshie.minedrop.ranks.PlayerManager;

import org.bukkit.entity.Player;

public class VoteHandler {
	
	private static VoteHandler instance = new VoteHandler();
	
	public static VoteHandler getInstance() {
		return instance;
	}
	
	public ArrayList<String> voted = new ArrayList<>();
	public ArrayList<String> maps = new ArrayList<>();
	public HashMap<String, Integer> votes = new HashMap<>();
	
	public void addVote(int map, Player player) {
		map--;
		switch (PlayerManager.getInstance().getRank(player)) {
			case "Regular":
			case "Special":
				//1
				votes.put(maps.get(map), 1);
				break;
			case "Hive":
				//2
				votes.put(maps.get(map), 2);
				break;
			case "Mod":
				//3
				votes.put(maps.get(map), 3);
				break;
			case "Admin":
				//4
				votes.put(maps.get(map), 4);
				break;
			case "Owner":
				//5
				votes.put(maps.get(map), 5);
				break;
		}
		
		voted.add(player.getName());
	}
	
	public String getMapVotedFor() {	
		int highest = 0;
		ArrayList<String> winners = new ArrayList<>();
		for(Entry<String, Integer> entry : votes.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			
			if (value >= highest) {
				highest = value;
				winners.add(key);
			}
		}

		return winners.get(0);
	}

}
