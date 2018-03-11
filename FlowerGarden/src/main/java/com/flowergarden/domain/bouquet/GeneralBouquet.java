package com.flowergarden.domain.bouquet;

import com.flowergarden.domain.flowers.GeneralFlower;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class GeneralBouquet implements Bouquet<GeneralFlower> {

    private Integer id;
    private Float assemblePrice;
    private List<GeneralFlower> flowerList = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getAssemblePrice() {
        return assemblePrice;
    }

    public void setAssemblePrice(Float assemblePrice) {
        this.assemblePrice = assemblePrice;
    }

    public void setFlowerList(List<GeneralFlower> flowerList) {
        this.flowerList = flowerList;
    }

    @Override
    public float getPrice() {
        float price = assemblePrice;
        for (GeneralFlower flower : flowerList) {
            price += flower.getPrice();
        }
        return price;
    }

    @Override
    public void addFlower(GeneralFlower flower) {
        flowerList.add(flower);
    }

    @Override
    public Collection<GeneralFlower> searchFlowersByLength(int start, int end) {
        List<GeneralFlower> searchResult = new ArrayList<GeneralFlower>();
        for (GeneralFlower flower : flowerList) {
            if (flower.getLength() >= start && flower.getLength() <= end) {
                searchResult.add(flower);
            }
        }
        return searchResult;
    }

    @Override
    public void sortByFreshness() {
        Collections.sort(flowerList);
    }

    @Override
    public Collection<GeneralFlower> getFlowers() {
        return flowerList;
    }

}
