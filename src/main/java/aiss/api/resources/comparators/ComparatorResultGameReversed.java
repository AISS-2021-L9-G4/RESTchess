package aiss.api.resources.comparators;

import java.util.Comparator;

import aiss.model.Game;


public class ComparatorResultGameReversed implements Comparator<Game>{

	@Override
	public int compare(Game p1, Game p2) {
		return -p1.getResult().compareTo(p2.getResult());
	}

}
