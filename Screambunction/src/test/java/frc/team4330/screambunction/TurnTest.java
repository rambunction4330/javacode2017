package frc.team4330.screambunction;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.wpi.first.wpilibj.SerialPort.Port;
import frc.team4330.screambunction.commands.Turn;
import frc.team4330.screambunction.testingutils.ToastRunner;

@RunWith(ToastRunner.class)
public class TurnTest {
    private Turn testObject;
    
    @Before
    public void setUp () {
    	testObject = new Turn(10, Port.kMXP); //teggsecute

    }

    @Test
    public void testExecute () {
    	testObject.execute();
    	assertEquals(.5, testObject.getLeftMotorSpeed(), 0);
    	assertEquals(-.5, testObject.getRightMotorSpeed(), 0);
    }

    // @Test
    public void testExecute2 () {
//        assertEquals(0.0, testObject.getSpeed(), 0.01);
    }
}