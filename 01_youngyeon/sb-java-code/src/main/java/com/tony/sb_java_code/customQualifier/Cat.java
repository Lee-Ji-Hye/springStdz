package com.tony.sb_java_code.customQualifier;

import com.tony.sb_java_code.anno.CatType;
import org.springframework.stereotype.Component;

@CatType
@Component
public class Cat extends Animal {
    @Override
    public String toString() {
        return "Cat {}";
    }
}
