package net.alextwelshie.minedrop.voting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.alextwelshie.minedrop.Main;

public class VoteHandler {

	private static VoteHandler	instance	= new VoteHandler();

	public static VoteHandler getInstance() {
		return instance;
	}

	public List<String>				maps	= new ArrayList<>();
	public List<String>				voted	= new ArrayList<>();
	public HashMap<String, Integer>	votes	= new HashMap<>();

	public void pickMap() {
		Main main = Main.getPlugin();

		int map1 = votes.get(maps.get(0));
		int map2 = votes.get(maps.get(1));
		int map3 = votes.get(maps.get(2));
		int map4 = votes.get(maps.get(3));
		//if map1 is the biggest
		if ((map1 > map2) && (map1 > map3) && (map1 > map4)) {
			main.mapName = maps.get(0);
		}
		//if map2 is the biggest
		else if ((map2 > map1) && (map2 > map3) && (map2 > map4)) {
			main.mapName = maps.get(1);
		}
		//if map3 is the biggest
		else if ((map3 > map1) && (map3 > map2) && (map3 > map4)) {
			main.mapName = maps.get(2);
		}
		//if map4 is the biggest
		else if ((map4 > map1) && (map4 > map3) && (map4 > map2)) {
			main.mapName = maps.get(3);
		}
		//if map1 and map2 are the same
		else if ((map1 == map2) && (map1 > map3) && (map1 > map4) && (map2 > map3) && (map2 > map4)) {
			main.mapName = maps.get(0);
		}
		//if map1 and map3 are the same
		else if ((map1 == map3) && (map1 > map2) && (map1 > map4) && (map3 > map2) && (map3 > map4)) {
			main.mapName = maps.get(0);
		}
		//if map1 and map4 are the same
		else if ((map1 == map4) && (map1 > map2) && (map1 > map3) && (map4 > map2) && (map4 > map3)) {
			main.mapName = maps.get(0);
		}
		//if map2 and map3 are the same
		else if ((map2 == map3) && (map2 > map1) && (map2 > map4) && (map3 > map1) && (map3 > map4)) {
			main.mapName = maps.get(2);
		}
		//if map2 and map4 are the same
		else if ((map2 == map4) && (map2 > map1) && (map2 > map3) && (map4 > map1) && (map4 > map3)) {
			main.mapName = maps.get(1);
		}
		//if map3 and map4 are the same
		else if ((map3 == map4) && (map3 > map1) && (map3 > map2) && (map4 > map1) && (map4 > map2)) {
			main.mapName = maps.get(2);
		}
		//if map1, map2 and map3 are the same
		else if ((map1 == map2) && (map2 == map3) && (map1 > map4) && (map2 > map4) && (map3 > map4)) {
			main.mapName = maps.get(0);
		}
		//if map1, map2 and map4 are the same
		else if ((map1 == map2) && (map2 == map4) && (map1 > map3) && (map2 > map3) && (map4 > map3)) {
			main.mapName = maps.get(0);
		}
		//if map2, map3 and map4 are the same
		else if ((map2 == map3) && (map3 == map4) && (map2 > map1) && (map3 > map1) && (map4 > map1)) {
			main.mapName = maps.get(1);
		}
		//if map1, map3 and map4 are the same
		else if ((map1 == map3) && (map3 == map4) && (map1 > map2) && (map3 > map2) && (map4 > map2)) {
			main.mapName = maps.get(0);
		}
		//if all the maps are the same
		else if ((map1 == map3) && (map3 == map4) && (map4 == map2)) {
			main.mapName = maps.get(0);
		}
	}

	public Integer getVotes(int rhif) {
		return votes.get(maps.get(rhif));
	}

	public void addVote(int map, int number) {
		String mapName = maps.get(map);
		int newVotes = votes.get(mapName);
		newVotes += number;
		votes.put(mapName, newVotes);
	}

	public void setVote(int map, int number) {
		String mapName = maps.get(map);
		votes.put(mapName, number);
	}
}
