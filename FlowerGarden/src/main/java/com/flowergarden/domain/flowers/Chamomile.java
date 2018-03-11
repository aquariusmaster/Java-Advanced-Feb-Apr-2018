package com.flowergarden.domain.flowers;

public class Chamomile extends GeneralFlower {
	
	private int petals;
	
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
