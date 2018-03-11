package com.flowergarden.domain.flowers;

import com.flowergarden.domain.properties.Freshness;

public interface Flower<T> {
	Freshness<T> getFreshness();
	float getPrice();
	int getLength();
}
