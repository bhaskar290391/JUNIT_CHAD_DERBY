package com.luv2code.TDD;

public class FizzBuzz {

    public static  String compute(int number){

        if(number %3 ==0 && number % 5==0){
            return  "FizzBuzz";
        }else if(number %3 ==0){
            return  "Fizz";
        } else if (number %5 ==0) {
            return "Buzz";
        }else{
            return Integer.toString(number);
        }
    }
}
