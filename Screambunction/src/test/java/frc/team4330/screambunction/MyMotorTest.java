package frc.team4330.screambunction;

import frc.team4330.screambunction.testingutils.ToastRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(ToastRunner.class)
public class MyMotorTest {

    private MyMotor testObject;

    @Before
    public void setUp () {
        testObject = new MyMotor();
    }

    @Test
    public void testMotorStart () {
        testObject.runMotor();
        assertEquals(1.0, testObject.getSpeed(), 0.01);
    }

    @Test
    public void testMotorStop () {
        testObject.runMotor();
        testObject.stopMotor();
        assertEquals(0.0, testObject.getSpeed(), 0.01);
    }
}
