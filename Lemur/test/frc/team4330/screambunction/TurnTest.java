package frc.team4330.screambunction;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import frc.team4330.robot.RobotMap;
import frc.team4330.robot.commands.GyroTurn;
import frc.team4330.robot.parts.HeadingProvider;
import frc.team4330.robot.parts.TankDrive;
import frc.team4330.screambunction.testingUtils.TestTankDrive;

public class TurnTest {
    private GyroTurn testObject;
    private TankDrive tankDrive;
//    TankDrive test;
    
    @Before
    public void setUp () {
    	HeadingProvider headingProvider = Mockito.mock(HeadingProvider.class);
    	tankDrive = new TestTankDrive();
    	
    	testObject = new GyroTurn(10, headingProvider, tankDrive);
    }

    @Test
    public void testExecute () {
    	testObject.execute();
    	assertEquals(-RobotMap.TEST_SPEED, tankDrive.getLeftSpeed(), 0);
    	assertEquals(RobotMap.TEST_SPEED, tankDrive.getRightSpeed(), 0);
    }
}