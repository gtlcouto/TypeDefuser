package com.example.managerdata;

import java.util.ArrayList;

public class SitesList {

	private ArrayList<String> text = new ArrayList<String>();

	public void setImages(String text) {
		this.text.add(text);
	}

	public ArrayList<String> getText() {
		return text;
	}

}
