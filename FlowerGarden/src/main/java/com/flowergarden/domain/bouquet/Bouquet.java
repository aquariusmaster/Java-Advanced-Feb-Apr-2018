package com.flowergarden.domain.bouquet;
import java.util.Collection;

public interface Bouquet<T> {
	float getPrice();
	void addFlower(T flower);
	Collection<T> searchFlowersByLength(int start, int end);
	void sortByFreshness();
	Collection<T> getFlowers();
}
