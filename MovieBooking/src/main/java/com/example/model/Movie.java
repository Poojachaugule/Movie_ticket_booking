package com.example.model;

public class Movie {
    private String name;
    private int frontSeatPrice;
    private int middleSeatPrice;
    private int lastSeatPrice;
    private int id;

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Movie(String name, int frontSeatPrice, int middleSeatPrice, int lastSeatPrice) {
	    this.name = name;
	    this.frontSeatPrice = frontSeatPrice;
	    this.middleSeatPrice = middleSeatPrice;
	    this.lastSeatPrice = lastSeatPrice;
	}
    public String getName() {
        return name;
    }

    public int getFrontSeatPrice() {
        return frontSeatPrice;
    }

    public int getMiddleSeatPrice() {
        return middleSeatPrice;
    }

    public int getLastSeatPrice() {
        return lastSeatPrice;
    }
}