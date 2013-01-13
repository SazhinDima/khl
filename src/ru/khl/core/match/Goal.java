package ru.khl.core.match;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.khl.core.player.Player;

public class Goal {

	private Player player;

	private Player assistant1;

	private Player assistant2;

	private Integer minute;

	private Integer second;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getAssistant1() {
		return assistant1;
	}

	public void setAssistant1(Player assistant1) {
		this.assistant1 = assistant1;
	}

	public Player getAssistant2() {
		return assistant2;
	}

	public void setAssistant2(Player assistant2) {
		this.assistant1 = assistant2;
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

	public static void sortByTime(List<Goal> goals) {
		Collections.sort(goals, new TimeComparator());
	}

	static class TimeComparator implements Comparator<Goal> {

		public int compare(Goal goalA, Goal goalB) {
			if (goalA.getMinute() > goalB.getMinute()) {
				return 1;
			} else if (goalA.getMinute() < goalB.getMinute()) {
				return -1;
			} else {
				if (goalA.getSecond() >= goalB.getSecond()) {
					return 1;
				} else {
					return -1;
				}
			}
		}
	}

}
