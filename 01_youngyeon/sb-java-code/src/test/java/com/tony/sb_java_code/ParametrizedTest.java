package com.tony.sb_java_code;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.testng.annotations.Parameters;

import java.util.Arrays;
import java.util.Collection;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)      // => #1
public class ParametrizedTest {

    private final int input;       // => #2
    private final int expected;

    public ParametrizedTest(int input, int expected) {      // => #4
        this.input = input;
        this.expected = expected;
    }

    @Parameters                    // => #3
    public static Collection data(){
        return Arrays.asList(new Integer[][] {
                {0,1},{1,1},{2,2},{3,6},{4,24},{5,120}});
    }

    @Test
    public void calculatorAddTest(){
        /**
         * factorial of: 0 should be: 1
         * factorial of: 1 should be: 1
         * factorial of: 2 should be: 2
         * factorial of: 3 should be: 6
         * factorial of: 4 should be: 24
         * factorial of: 5 should be: 120
         */
        System.out.println("factorial of: " + input + " should be: " + expected);
        Assert.assertEquals(expected, Math.fact(input));
    }

    static class Math {

        static int fact(int n) {
            int result;
            if (n == 0 || n == 1)
                return 1;

            result = fact(n - 1) * n;
            return result;
        }
    }
}
