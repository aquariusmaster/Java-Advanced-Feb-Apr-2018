package com.flowergarden.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@NoArgsConstructor @AllArgsConstructor
@Setter @Getter
@XmlAccessorType(XmlAccessType.FIELD)
public class Address {
    private String street;

}
