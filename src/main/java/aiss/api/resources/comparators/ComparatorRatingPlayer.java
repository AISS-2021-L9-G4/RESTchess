package aiss.api.resources.comparators;

import java.util.Comparator;

import aiss.model.Player;

public class ComparatorRatingPlayer implements Comparator<Player>{

	@Override
	public int compare(Player p1, Player p2) {
		return Integer.compare(Integer.parseInt(p1.getName()), Integer.parseInt(p2.getName()));
	}

}
