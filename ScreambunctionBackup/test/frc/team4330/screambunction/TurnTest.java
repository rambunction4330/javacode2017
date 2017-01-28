package frc.team4330.screambunction;

import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team4330.robot.commands.Turn;

public class TurnTest {
    private Turn testObject;
//    private TestAbsRobotDrive tankDrive;
    
    @Before
    public void setUp () {
//    	HeadingProvider headingProvider = Mockito.mock(HeadingProvider.class);
//    	tankDrive = new TestAbsRobotDrive();
//    	testObject = new Turn(10, headingProvider, tankDrive);
    }

    @Test
    public void testExecute () {
    	testObject.execute();
//    	assertEquals(.5, tankDrive.getLeftSpeed(), 0);
//    	assertEquals(-.5, tankDrive.getRightSpeed(), 0);
    }

    // @Test
    public void testExecute2 () {
//        assertEquals(0.0, testObject.getSpeed(), 0.01);
    }
}