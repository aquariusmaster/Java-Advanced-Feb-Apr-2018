package com.flowergarden.domain.flowers;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Rose extends GeneralFlower {
	
	private boolean spike;
	
	public boolean hasSpike() {
		return spike;
	}

	public void setSpike(boolean spike) {
		this.spike = spike;
	}
}
