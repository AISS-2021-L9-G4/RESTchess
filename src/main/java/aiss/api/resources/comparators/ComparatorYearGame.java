package aiss.api.resources.comparators;

import java.util.Comparator;

import aiss.model.Game;


public class ComparatorYearGame implements Comparator<Game>{

	@Override
	public int compare(Game p1, Game p2) {
		return p1.getYear().compareTo(p2.getYear());
	}

}
