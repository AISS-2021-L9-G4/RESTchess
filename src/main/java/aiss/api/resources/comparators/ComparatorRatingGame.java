package aiss.api.resources.comparators;

import java.util.Comparator;

import aiss.model.Game;

public class ComparatorRatingGame implements Comparator<Game> {

	private Double getMeanRating(Game g) {
		return (Double.valueOf(g.getBlack().getRating())+Double.valueOf(g.getWhite().getRating()))/2;
	}
	@Override
	public int compare(Game g1, Game g2) {
		return getMeanRating(g1).compareTo(getMeanRating(g2));
	}

}
