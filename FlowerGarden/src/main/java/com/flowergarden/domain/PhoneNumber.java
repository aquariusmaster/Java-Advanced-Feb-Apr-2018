package com.flowergarden.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class PhoneNumber {
    @XmlAttribute
    private String type;
    @XmlValue
    private String value;
}
