// IHelloService.aidl
package com.github.foodiestudio.boilerplate.playground;

// Declare any non-default types here with import statements
import com.github.foodiestudio.boilerplate.playground.Haha;

interface IHelloService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    Haha getHaha();
}