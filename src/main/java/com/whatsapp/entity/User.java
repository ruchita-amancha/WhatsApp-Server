package com.whatsapp.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String full_name;
	private String email;
	private String profile_picture;
	private String password;
	
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(Integer id, String full_name, String email, String profile_picture, String password) {
		super();
		this.id = id;
		this.full_name = full_name;
		this.email = email;
		this.profile_picture = profile_picture;
		this.password = password;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProfile_picture() {
		return profile_picture;
	}

	public void setProfile_picture(String profile_picture) {
		this.profile_picture = profile_picture;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
//	@OneToMany(mappedBy="user",cascade=CascadeType.ALL)
//	private List<Notification> notifications=new ArrayList<>();
	
	
}
