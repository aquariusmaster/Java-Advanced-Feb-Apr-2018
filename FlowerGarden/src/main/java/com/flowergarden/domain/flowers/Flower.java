package com.flowergarden.domain.flowers;

public interface Flower<T> {
	T getFreshness();
	float getPrice();
	int getLength();
}
