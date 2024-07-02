package com.luv2code.junitdemo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

public class ConditionalTest {

    @Test
    @Disabled
    public void testDisabled(){

    }


    @Test
    @EnabledOnOs(OS.MAC)
    public void testMac(){

    }


    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void testWindows(){

    }


    @Test
    @EnabledOnOs({OS.WINDOWS,OS.LINUX})
    public void testLinuxWindows(){

    }

    @Test
    @EnabledOnJre(JRE.JAVA_11)
    void testJava11(){

    }


    @Test
    @EnabledOnJre(JRE.JAVA_17)
    void testJava17(){

    }

    @Test
    @EnabledForJreRange(min = JRE.JAVA_13,max = JRE.JAVA_18)
    void testJavaRange(){

    }



    @Test
    @EnabledForJreRange(min = JRE.JAVA_13)
    void testJavaRangeMin(){

    }

    @Test
    @EnabledIfEnvironmentVariable(named = "dev",matches = "dev_value")
    public void devTest(){

    }

    @Test
    @EnabledIfSystemProperty(named = "system",matches = "system_value")
    public void systemTest(){

    }
}
