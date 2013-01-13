package ru.khl.core.match;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.khl.core.player.Player;

public class Penalty {

	private Player player;

	private Integer minute;

	private Integer second;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Integer getMinute() {
		return minute;
	}

	public void setMinute(Integer minute) {
		this.minute = minute;
	}

	public Integer getSecond() {
		return second;
	}

	public void setSecond(Integer second) {
		this.second = second;
	}

	public String getSafetyPlayerName() {
		if (getPlayer() == null) {
			return "";
		}
		return getPlayer().getName();
	}

	public String getSafetyPlayerNumber() {
		if (getPlayer() == null) {
			return "";
		}
		return getPlayer().getNumber().toString();
	}

	public static void sortByTime(List<Penalty> penalties) {
		Collections.sort(penalties, new TimeComparator());
	}

	static class TimeComparator implements Comparator<Penalty> {

		public int compare(Penalty penaltyA, Penalty penaltyB) {
			if (penaltyA.getMinute() > penaltyB.getMinute()) {
				return 1;
			} else if (penaltyA.getMinute() < penaltyB.getMinute()) {
				return -1;
			} else {
				if (penaltyA.getSecond() >= penaltyB.getSecond()) {
					return 1;
				} else {
					return -1;
				}
			}
		}
	}
}
