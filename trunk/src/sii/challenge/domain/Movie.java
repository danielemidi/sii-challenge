package sii.challenge.domain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Movie {

	private int id;
	private String regista; // potrebbe essere INT!
	private List<String> attori; // potrebbero essere INT!
	private List<String> generi;  // potrebbero essere INT!
	private String country;  // potrebbero essere INT!
	private List<String> locations;  // potrebbero essere INT!
	private Map<Integer, Integer> generalweightedtags;
	
	private int rtAllCriticsRating;
	private int rtAllCriticsScore;
	private int rtTopCriticsRating;
	private int rtTopCriticsScore;
	private int rtAudienceRating;
	private int rtAudienceScore;
	
	public Movie(int id) {
		this.id = id;
		this.attori = new LinkedList<String>();
		this.generi = new LinkedList<String>();
		this.locations = new LinkedList<String>();
		this.generalweightedtags = new HashMap<Integer, Integer>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRegista() {
		return regista;
	}

	public void setRegista(String regista) {
		this.regista = regista;
	}

	public List<String> getAttori() {
		return attori;
	}

	public void setAttori(List<String> attori) {
		this.attori = attori;
	}

	public List<String> getGeneri() {
		return generi;
	}

	public void setGeneri(List<String> generi) {
		this.generi = generi;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<String> getLocations() {
		return locations;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
	}

	public Map<Integer, Integer> getGeneralweightedtags() {
		return generalweightedtags;
	}

	public void setGeneralweightedtags(Map<Integer, Integer> generalweightedtags) {
		this.generalweightedtags = generalweightedtags;
	}

	public int getRtAllCriticsRating() {
		return rtAllCriticsRating;
	}

	public void setRtAllCriticsRating(int rtAllCriticsRating) {
		this.rtAllCriticsRating = rtAllCriticsRating;
	}

	public int getRtAllCriticsScore() {
		return rtAllCriticsScore;
	}

	public void setRtAllCriticsScore(int rtAllCriticsScore) {
		this.rtAllCriticsScore = rtAllCriticsScore;
	}

	public int getRtTopCriticsRating() {
		return rtTopCriticsRating;
	}

	public void setRtTopCriticsRating(int rtTopCriticsRating) {
		this.rtTopCriticsRating = rtTopCriticsRating;
	}

	public int getRtTopCriticsScore() {
		return rtTopCriticsScore;
	}

	public void setRtTopCriticsScore(int rtTopCriticsScore) {
		this.rtTopCriticsScore = rtTopCriticsScore;
	}

	public int getRtAudienceRating() {
		return rtAudienceRating;
	}

	public void setRtAudienceRating(int rtAudienceRating) {
		this.rtAudienceRating = rtAudienceRating;
	}

	public int getRtAudienceScore() {
		return rtAudienceScore;
	}

	public void setRtAudienceScore(int rtAudienceScore) {
		this.rtAudienceScore = rtAudienceScore;
	}
	
	
	
}
