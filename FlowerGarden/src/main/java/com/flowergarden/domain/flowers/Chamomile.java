package com.flowergarden.domain.flowers;

import com.flowergarden.domain.properties.FreshnessInteger;

public class Chamomile extends GeneralFlower {
	
	private int petals;

	public Chamomile() {
	}

	public Chamomile(float price, int length, FreshnessInteger fresh, int petals){
		super(price, length, fresh);
		this.petals = petals;
	}
	
	public boolean getPetal(){
		if (petals <=0) return false;
		petals =-1;
		return true;
	}
	
	public int getPetals(){
		return petals;
	}

	public void setPetals(int petals) {
		this.petals = petals;
	}
}
