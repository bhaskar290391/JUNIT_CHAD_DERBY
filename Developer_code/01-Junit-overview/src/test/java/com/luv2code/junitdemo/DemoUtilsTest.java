package com.luv2code.junitdemo;


import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
//@DisplayNameGeneration(DisplayNameGenerator.IndicativeSentences.class)
//@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DemoUtilsTest {

    private DemoUtils utils;

    @BeforeAll
    public static  void beforeAllForClass(){
        System.out.println("@BeforeAll method execution of Each class");
        System.out.println();
    }


    @BeforeEach
    public void beforeEachTest(){
        utils=new DemoUtils();
        System.out.println("@BeforeEach method execution of Each method");

    }

    @AfterEach
    public void afterEachTest(){

        System.out.println("@AfterEach method execution of Each method");
        System.out.println();
    }

    @AfterAll
    public static void  afterAllForClass(){
        System.out.println("@AfterAll method execution of Each class");
        System.out.println();
    }

    @Test
    @DisplayName("test Multiply")
    public void testMultiply(){
        System.out.println("Test ==> testMultiply()");

        assertEquals(8,utils.multiply(2,4),"2 * 4 must be 8");

    }

    @Test
    @DisplayName("Equals and Not Equals")
    public void checkEqualsAndNotEquals(){
        System.out.println("Test ==> checkEqualsAndNotEquals()");

        assertEquals(6,utils.add(2,4),"2 + 4 must be 6");
        assertNotEquals(4,utils.add(4,4),"4+4 must not be 4");
    }


    @Test
    @DisplayName("Null and Not Null")
    public void checkNullNotNull(){

        System.out.println("Test ==> checkNullNotNull()");
        String str1=null;
        String str2="bhaskar";

        assertNull(utils.checkNull(str1));
        assertNotNull(utils.checkNull(str2));
    }


    @Test
    @DisplayName("Same and Not Same")
    public void testSameNotSame(){
        System.out.println("Test ==> testSameNotSame()");

        String str="luv2code";
        assertSame(utils.getAcademy(),utils.getAcademyDuplicate());
        assertNotSame(str,utils.getAcademy());

    }


    @Test
    @DisplayName("True and Not False")
    public void testTrueAndFalse(){
        System.out.println("Test ==> testTrueAndFalse()");

        assertTrue(utils.isGreater(10,6));
        assertFalse(utils.isGreater(6,16));
    }


    @Test
    @DisplayName("Array Equals")
    public void testArrayEquals(){
        System.out.println("Test ==> testArrayEquals()");
        String[] test={"A","B","C"};
       assertArrayEquals(test,utils.getFirstThreeLettersOfAlphabet());
    }

    @Test
    @Order(2)
    @DisplayName("Iterable Equals")
    public void testIterableEquals(){
        System.out.println("Test ==> testIterableEquals()");
        List<String> data = List.of("luv", "2", "code");
        assertIterableEquals(data,utils.getAcademyInList());
    }

    @Test
    @DisplayName("Throws Test")
    public void testThrows(){

        assertThrows(Exception.class,()->{ utils.throwException(-1);},"This will throw Exception");
        assertDoesNotThrow(()->{ utils.throwException(9);});
    }


    @Test
    @Order(1)
    @DisplayName("TimeOut")
    public void testTimeout(){

        assertTimeout(Duration.ofSeconds(3),()->{utils.checkTimeout();});
    }
}
