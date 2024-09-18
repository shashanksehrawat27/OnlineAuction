package org.bidding.domain.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum ProductCategoryEnum {
    ELECTRONICS(1, "Electronics"),
    MOBILE(2, "Mobile"),
    FURNITURE(3, "Furniture"),
    SPORTS(4, "Sports");
    private final Integer value;
    private final String text;
    ProductCategoryEnum(Integer value, String text){
        this.text = text;
        this.value = value;
    }

    public String toString(){
        return text;
    }

    private static final Map<Integer, ProductCategoryEnum> BY_VALUE = Arrays.stream(values()).collect(Collectors.toMap(c -> c.value, c -> c));
    public static ProductCategoryEnum of(Integer value) {
        return BY_VALUE.get(value);
    }
}

