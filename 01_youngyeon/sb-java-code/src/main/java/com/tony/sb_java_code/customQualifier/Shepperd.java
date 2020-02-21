package com.tony.sb_java_code.customQualifier;

import com.tony.sb_java_code.anno.CatType;
import com.tony.sb_java_code.anno.DogType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Shepperd {

    @Autowired
    private @DogType
    Animal dog;

    @Autowired
    private @CatType
    Animal  cat;

    @Override
    public String toString() {
        return "Shepperd{" +
                "dog=" + dog +
                ", cat=" + cat +
                '}';
    }
}
