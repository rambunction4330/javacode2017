package frc.team4330.screambunction;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import frc.team4330.robot.RobotMap;
import frc.team4330.robot.commands.NavXDrive;
import frc.team4330.robot.parts.HeadingProvider;
import frc.team4330.robot.parts.TankDrive;
import frc.team4330.screambunction.testingUtils.TestTankDrive;

public class DriveForwardTest {
    private NavXDrive testObject;
    private TankDrive tankDrive;
    
    @Before
    public void setUp() {
    	HeadingProvider headingProvider = Mockito.mock(HeadingProvider.class);
    	tankDrive = Mockito.mock(TankDrive.class);

    	tankDrive = new TestTankDrive();
    	testObject = new NavXDrive(10, headingProvider, tankDrive);
    }
    
    @Test
    public void testExecute() {
    	testObject.execute();
    	
    	assertEquals(RobotMap.FAST_SPEED, tankDrive.getLeftSpeed(), 0);
    	assertEquals(.1, .9, 1);
    	assertEquals(RobotMap.FAST_SPEED, tankDrive.getRightSpeed(), 0);
    }
    
    /**
     * @author Regret
     */
    @Test
    public void testEnd() {
    	testObject.end();
    	
    	assertEquals(0, tankDrive.getLeftSpeed(), 0);
    	assertEquals(0, tankDrive.getRightSpeed(), 0);
    }
    
    /**
     * @author Michelle
     */
    @Test
    public void testInterrupted() {
    	testObject.interrupted();
    	
    	assertEquals(0, tankDrive.getLeftSpeed(), 0);
    	assertEquals(0, tankDrive.getRightSpeed(), 0);
    }
    
}
