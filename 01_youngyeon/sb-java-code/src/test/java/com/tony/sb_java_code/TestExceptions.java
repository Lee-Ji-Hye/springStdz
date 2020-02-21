package com.tony.sb_java_code;

import com.tony.sb_java_code.anno.ExpectsException;
import com.tony.sb_java_code.runner.ExpectsExceptionRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ExpectsExceptionRunner.class)
public class TestExceptions {

    @Test
    @ExpectsException(type = ArithmeticException.class, message = "/ by zero")
    public void throwsArrayIndexOutOfBoundsException(){
        float temp = 5 / 0;
    }

}
