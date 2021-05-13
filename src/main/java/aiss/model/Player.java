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
	
	public Player(String id, String name, String rating) {
		this.id = id;
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

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}
}
