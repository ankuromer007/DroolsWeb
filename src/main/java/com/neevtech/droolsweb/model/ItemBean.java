package com.neevtech.droolsweb.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemBean {
	private int id;
	private String name;
	@XmlElement(name = "base-price")
	private float basePrice;
	@XmlTransient
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
	
	@XmlTransient
	public float getDiscount() {	
		float totalDiscount = 0;
		for(Float discount : discounts) {
			totalDiscount += discount;
		}
		return totalDiscount;
	}
	public void addDiscount(float discount) {
		discounts.add(new Float(discount));
	}
	
	@XmlElement(name = "discounted-price")
	public float getDiscountedPrice() {	
		for(Float discount : discounts) {
			basePrice -= basePrice*discount/100;
		}
		return basePrice;
	}
}
