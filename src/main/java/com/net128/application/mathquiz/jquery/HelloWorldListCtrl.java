package com.net128.application.mathquiz.jquery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HelloWorldListCtrl implements Serializable {
	private static final long serialVersionUID = 1;

	List<Fruit> model = new ArrayList<Fruit>();

	public HelloWorldListCtrl() {
		model.add(new Fruit("Apples", "apples.xhtml"));
		model.add(new Fruit("Kiwis", "kiwis.xhtml"));
		model.add(new Fruit("Mangos", "mangos.xhtml"));
		model.add(new Fruit("Peaches", "peaches.xhtml"));
	}

	public List<Fruit> getModel() {
		return model;
	}

	public void setModel(List<Fruit> model) {
		this.model = model;
	}
}