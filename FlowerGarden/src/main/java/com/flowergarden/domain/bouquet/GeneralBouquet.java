package com.flowergarden.domain.bouquet;

import com.flowergarden.domain.flowers.GeneralFlower;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class GeneralBouquet implements Bouquet<GeneralFlower> {

    @XmlElement(name="id")
    private Integer id;
    @XmlElement(name="price")
    private Float assemblePrice;
    @XmlElement(name="flowers")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GeneralBouquet bouquet = (GeneralBouquet) o;
        return Objects.equals(id, bouquet.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "GeneralBouquet{" +
                "id=" + id +
                ", assemblePrice=" + assemblePrice +
                '}';
    }
}
