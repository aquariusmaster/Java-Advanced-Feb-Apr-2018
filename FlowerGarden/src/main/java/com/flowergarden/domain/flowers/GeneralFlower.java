package com.flowergarden.domain.flowers;

import com.flowergarden.domain.bouquet.GeneralBouquet;
import com.flowergarden.domain.properties.FreshnessInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class GeneralFlower implements Flower<Integer>, Comparable<GeneralFlower> {

	@XmlElement(name="id")
	private Integer id;
	@XmlElement(name="price")
	private float price;
	@XmlElement(name="length")
	private int length;
	@XmlElement(name = "freshness")
	private FreshnessInteger freshness;
	@XmlTransient
	private GeneralBouquet bouquet;

	public GeneralFlower() {
	}

	public GeneralFlower(float price, int length, FreshnessInteger freshness) {
		this.price = price;
		this.length = length;
		this.freshness = freshness;
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
	public FreshnessInteger getFreshness() {
		return freshness;
	}

	public void setFreshness(FreshnessInteger fr){
		freshness = fr;
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

	@Override
	public String toString() {
		return "GeneralFlower{" +
				"id=" + id +
				", freshness=" + freshness.getFreshness() +
				", price=" + price +
				", length=" + length +
				", bouquetId=" + (bouquet != null ? bouquet.getId() : null) +
				'}';
	}
}
