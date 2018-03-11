package com.flowergarden.domain.flowers;

import javax.xml.bind.annotation.XmlRootElement;

import com.flowergarden.domain.properties.FreshnessInteger;

@XmlRootElement
public class Rose extends GeneralFlower {
	
	private boolean spike;
	
	public Rose(int length, float price, FreshnessInteger fresh, boolean spike){
		super(price, length, fresh);
		this.spike = spike;
	}
	public Rose(){
		
	}
	
	public boolean hasSpike() {
		return spike;
	}

	public void setSpike(boolean spike) {
		this.spike = spike;
	}
}
