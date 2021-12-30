/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrk;

import org.junit.Test;
import static org.junit.Assert.*;


public class WrkTest {
    //                     INPUT[double,double]                    OUTPUT[double,double]  
    //                     [magnitude,angle]                      [leftSpeed,rightSpeed]  
    //Test 1               [1,0]                                  [>~0.9,>~0.9]
    //Test 2               [1,90]                                 [>~0.9,<~0.9]
    //Test 3               [1,180]                                [<~0.9,<~0.9]
    //Test 4               [1,270]                                [<~0.9,>~0.9]
    //Test 5               [1,Double.MAX_VALUE]                   [>~0.9;>~0.9]
    //Test 6               [1,Double.MIN_VALUE]                   [>~0.9;>~0.9]
    //Test 7               [Double.MAX_VALUE,Double.MAX_VALUE]    [>~0.9;>~0.9]
    //Test 8               [Double.MAX_MIN,Double.MAX_VALUE]      [<~0.9;<~0.9]
    @Test
    public void testThrottleAngleToThrust() {
        System.out.println("Testing method : throttleAngleToThrust");

        Wrk instance = new Wrk();
        boolean ok = false;
        //Test 1               [1,0]                                  [>~0.9,>~0.9]
        System.out.println("-----------Test 1-----------");
        double magnitude = 1;
        double angle = 0;
        double[] result = instance.throttleAngleToThrust(magnitude, angle);
        if (result[0] > 0.9 && result[1] > 0.9) {
            ok = true;
        }
        assertTrue(ok);
        System.out.println("Input : magnitude = " + magnitude + " , angle = " + angle + System.lineSeparator() + "Output : leftSpeed = " + result[0] + " , rightSpeed = " + result[1]);
        ok = false;

        //Test 2               [1,90]                                 [>~0.9,<~0.9]
        System.out.println("-----------Test 2-----------");
        angle = 90;
        result = instance.throttleAngleToThrust(magnitude, angle);
        if (result[0] > 0.9 && result[1] < 0.9) {
            ok = true;
        }
        assertTrue(ok);
        System.out.println("Input : magnitude = " + magnitude + " , angle = " + angle + System.lineSeparator() + "Output : leftSpeed = " + result[0] + " , rightSpeed = " + result[1]);
        ok = false;

        //Test 3               [1,180]                                [<~0.9,<~0.9]
        System.out.println("-----------Test 3-----------");
        angle = 180;
        result = instance.throttleAngleToThrust(magnitude, angle);
        if (result[0] < 0.9 && result[1] < 0.9) {
            ok = true;
        }
        assertTrue(ok);
        System.out.println("Input : magnitude = " + magnitude + " , angle = " + angle + System.lineSeparator() + "Output : leftSpeed = " + result[0] + " , rightSpeed = " + result[1]);
        ok = false;

        //Test 4               [1,270]                                [<~0.9,>~0.9]
        System.out.println("-----------Test 4-----------");
        angle = 270;
        result = instance.throttleAngleToThrust(magnitude, angle);
        System.out.println(result[0] + "  " + result[1]);
        if (result[0] < 0.9 && result[1] > 0.9) {
            ok = true;
        }
        assertTrue(ok);
        System.out.println("Input : magnitude = " + magnitude + " , angle = " + angle + System.lineSeparator() + "Output : leftSpeed = " + result[0] + " , rightSpeed = " + result[1]);
        ok = false;

        //Test 5               [1,Double.MAX_VALUE]                   [>~0.9;>~0.9]
        System.out.println("-----------Test 5-----------");
        angle = Double.MAX_VALUE;
        result = instance.throttleAngleToThrust(magnitude, angle);
        if (result[0] > 0.9 && result[1] > 0.9) {
            ok = true;
        }
        assertTrue(ok);
        System.out.println("Input : magnitude = " + magnitude + " , angle = " + angle + System.lineSeparator() + "Output : leftSpeed = " + result[0] + " , rightSpeed = " + result[1]);
        ok = false;

        //Test 6               [1,Double.MIN_VALUE]                   [>~0.9;>~0.9]
        System.out.println("-----------Test 6-----------");
        angle = Double.MIN_VALUE;
        result = instance.throttleAngleToThrust(magnitude, angle);
        if (result[0] > 0.9 && result[1] > 0.9) {
            ok = true;
        }
        assertTrue(ok);
        System.out.println("Input : magnitude = " + magnitude + " , angle = " + angle + System.lineSeparator() + "Output : leftSpeed = " + result[0] + " , rightSpeed = " + result[1]);
        ok = false;

        //Test 7               [Double.MAX_VALUE,Double.MAX_VALUE]    [>~0.9;>~0.9]
        System.out.println("-----------Test 7-----------");
        angle = Double.MAX_VALUE;
        magnitude = Double.MAX_VALUE;
        result = instance.throttleAngleToThrust(magnitude, angle);
        if (result[0] > 0.9 && result[1] > 0.9) {
            ok = true;
        }
        assertTrue(ok);
        System.out.println("Input : magnitude = " + magnitude + " , angle = " + angle + System.lineSeparator() + "Output : leftSpeed = " + result[0] + " , rightSpeed = " + result[1]);
        ok = false;

        //Test 8              [Double.MAX_MIN,Double.MAX_VALUE]      [<~0.9;<~0.9]
        System.out.println("-----------Test 8-----------");

        angle = Double.MIN_VALUE;
        magnitude = Double.MIN_VALUE;
        result = instance.throttleAngleToThrust(magnitude, angle);
        if (result[0] < 0.9 && result[1] < 0.9) {
            ok = true;
        }
        System.out.println("Input : magnitude = " + magnitude + " , angle = " + angle + System.lineSeparator() + "Output : leftSpeed = " + result[0] + " , rightSpeed = " + result[1]);
        assertTrue(ok);

    }

}
