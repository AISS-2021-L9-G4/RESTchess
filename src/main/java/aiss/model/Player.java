package aiss.model;

public class Player {
	private String id;
	private String name;
	private String rating;

	public Player () {
	}

	public Player(String name, String rating) {
		this.name = name;
		this.rating = rating;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
}
