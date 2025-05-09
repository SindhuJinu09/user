package com.algobrewery.persistence.attributeconverters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringArrayConverter implements AttributeConverter<String[], String[]> {

    @Override
    public String[] convertToDatabaseColumn(String[] attribute) {
        return attribute;
    }

    @Override
    public String[] convertToEntityAttribute(String[] dbData) {
        return dbData;
    }
}