package com.example.model;

public class User {
    private String username;
    private String password;
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private int id;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}