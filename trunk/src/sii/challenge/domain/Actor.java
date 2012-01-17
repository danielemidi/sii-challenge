package sii.challenge.domain;

public class Actor {
	
	private String ID;
	private String name;
	private int ranking;
	
	public Actor(String iD, String name, int ranking) {
		super();
		ID = iD;
		this.name = name;
		this.ranking = ranking;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	@Override
	public String toString() {
		return "Actor [ID=" + ID + ", name=" + name + ", ranking=" + ranking
				+ "]";
	}

	
}
