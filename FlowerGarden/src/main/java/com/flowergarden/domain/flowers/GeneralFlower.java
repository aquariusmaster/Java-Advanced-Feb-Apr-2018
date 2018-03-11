package com.flowergarden.domain.flowers;

import com.flowergarden.domain.bouquet.GeneralBouquet;
import com.flowergarden.domain.properties.FreshnessInteger;

public abstract class GeneralFlower implements Flower<Integer>, Comparable<GeneralFlower> {

	private Integer id;

	private float price;

	private int length;

	private FreshnessInteger freshness;

	private GeneralBouquet bouquet;

	public GeneralFlower() {
	}

	public GeneralFlower(float price, int length, FreshnessInteger freshness) {
		this.price = price;
		this.length = length;
		this.freshness = freshness;
	}

	public void setFreshness(FreshnessInteger fr){
		freshness = fr;
	}
	
	@Override
	public FreshnessInteger getFreshness() {
		return freshness;
	}

	@Override
	public float getPrice() {
		return price;
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public int compareTo(GeneralFlower compareFlower) {
		int compareFresh = compareFlower.getFreshness().getFreshness();		
		return this.getFreshness().getFreshness() - compareFresh;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public GeneralBouquet getBouquet() {
		return bouquet;
	}

	public void setBouquet(GeneralBouquet bouquet) {
		this.bouquet = bouquet;
	}

	@Override
	public String toString() {
		return "GeneralFlower{" +
				"id=" + id +
				", freshness=" + freshness +
				", price=" + price +
				", length=" + length +
				'}';
	}
}
