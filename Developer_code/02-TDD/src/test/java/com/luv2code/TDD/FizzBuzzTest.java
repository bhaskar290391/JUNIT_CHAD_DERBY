package com.luv2code.TDD;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FizzBuzzTest {

    @Test
    @DisplayName("Divisible by 3")
    @Order(1)
    public void divisibleByThree(){
        assertEquals("Fizz",FizzBuzz.compute(3),"Divisible by 3");
    }

    @Test
    @DisplayName("Divisible by 5")
    @Order(2)
    public void divisibleByFive(){
        assertEquals("Buzz",FizzBuzz.compute(5),"Divisible by 5");
    }

    @Test
    @DisplayName("Divisible by 3 and 5")
    @Order(3)
    public void divisibleByThreeAndFive(){
        assertEquals("FizzBuzz",FizzBuzz.compute(15),"Divisible by 3 and 5");
    }

    @Test
    @DisplayName("Not Divisible by 3 or  5")
    @Order(4)
    public void notDivisibleByThreeAndFive(){
        assertEquals("1",FizzBuzz.compute(1),"Divisible by 3 or 5");
    }


    //Testing file with paremeterized Test
    @CsvFileSource(resources = "/small-test-data.csv")
    @ParameterizedTest(name = "value={0},expected={1}")
    @DisplayName("Test Small File ")
    @Order(5)
    public void testSmallFile(int value,String expected){
        assertEquals(expected,FizzBuzz.compute(value));
    }



    @CsvFileSource(resources = "/medium-test-data.csv")
    @ParameterizedTest(name = "value={0},expected={1}")
    @DisplayName("Test Medium File ")
    @Order(6)
    public void testMediumFile(int value,String expected){
        assertEquals(expected,FizzBuzz.compute(value));
    }


    @CsvFileSource(resources = "/large-test-data.csv")
    @ParameterizedTest(name = "value={0},expected={1}")
    @DisplayName("Test Large File ")
    @Order(7)
    public void testLargeFile(int value,String expected){
        assertEquals(expected,FizzBuzz.compute(value));
    }
}
