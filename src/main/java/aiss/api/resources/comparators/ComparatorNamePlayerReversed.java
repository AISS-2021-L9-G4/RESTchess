package aiss.api.resources.comparators;

import java.util.Comparator;

import aiss.model.Player;

public class ComparatorNamePlayerReversed implements Comparator<Player>{

	@Override
	public int compare(Player p1, Player p2) {
		return - p1.getName().compareTo(p2.getName());
	}

}
