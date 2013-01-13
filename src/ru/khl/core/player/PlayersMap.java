package ru.khl.core.player;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlayersMap extends AbstractMap<Integer, Player> {

	Map<Integer, Player> players = new HashMap<Integer, Player>();

	private static PlayersMap instance;

	public static synchronized PlayersMap getInstance() {
		if (instance == null) {
			instance = new PlayersMap();
		}
		return instance;
	}

	public Player createPlayer(Integer id, String name, Integer number) {
		if (containsKey(id)) {
			return get(id);
		}
		Player player = new Player();
		player.setId(id);
		player.setName(name);
		player.setNumber(number);
		put(id, player);
		return player;
	}

	@Override
	public Player put(Integer key, Player player) {
		return players.put(key, player);
	}

	@Override
	public Set<java.util.Map.Entry<Integer, Player>> entrySet() {
		return players.entrySet();
	}

	public Player obtainPlayerById(Integer id) {
		return players.get(id);
	}
}
