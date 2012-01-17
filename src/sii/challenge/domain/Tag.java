package sii.challenge.domain;

public class Tag {

	private int id;
	private String value;
	
	
	public Tag(int id, String value) {
		super();
		this.id = id;
		this.value = value;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	@Override
	public String toString() {
		return "Tag [id=" + id + ", value=" + value + "]";
	}
	
	
	
}
