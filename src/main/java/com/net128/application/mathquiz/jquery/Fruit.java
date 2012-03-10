package com.net128.application.mathquiz.jquery;

import java.io.Serializable;

public class Fruit implements Serializable {
	private static final long serialVersionUID = 1;

	private String name;
	private String link;

	public Fruit(String name, String link){
		this.name = name;
		this.link = link;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}


}