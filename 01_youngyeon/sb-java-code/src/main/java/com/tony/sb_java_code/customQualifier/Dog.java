package com.tony.sb_java_code.customQualifier;

import com.tony.sb_java_code.anno.DogType;
import org.springframework.stereotype.Component;

@DogType
@Component
public class Dog extends Animal {
    @Override
    public String toString() {
        return "Dog {}";
    }
}
