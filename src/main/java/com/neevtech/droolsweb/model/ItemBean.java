package com.neevtech.droolsweb.model;

import java.util.ArrayList;
import java.util.List;

public class ItemBean {
	private int id;
	private String name;
	private float basePrice;
	public List<Float> discounts = new ArrayList<Float>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(float basePrice) {
		this.basePrice = basePrice;
	}
	public void addtDiscount(float discount) {
		
		discounts.add(new Float(discount));
	}
	public float getDiscount() {	
		float totalDiscount = 0;
		for(Float discount : discounts) {
			totalDiscount += discount;
		}
		return totalDiscount;
	}
	public float getDiscountedPrice() {	
		for(Float discount : discounts) {
			basePrice -= basePrice*discount/100;
		}
		return basePrice;
	}
}
