package frc.team4330.robot.subsystems

import edu.wpi.first.wpilibj.Compressor
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.DoubleSolenoid.Value
import edu.wpi.first.wpilibj.Solenoid

class Pneumatics
/**
 * Use this method so that multiple instances of Pneumatics are not created.
 * @param solenoid2
 *
 * @return the instance of the pneumatics method
 */
/*public static Pneumatics getInstance() {
		if (instance == null) {
			instance = new Pneumatics();
		}
		return instance;

	    return instance == null ? instance = new Pneumatics(new Compressor(RobotMap.COMPRESSOR), new Solenoid(RobotMap.KICKER_SOL)) : instance;
	}*/
(
        //	private static Pneumatics instance;

        // private Timer time;
        var comp: Compressor, private val anothaone: DoubleSolenoid, //private final Solenoid kicker;
        private val bigone: DoubleSolenoid)//		comp.start();
//	instance = this;
{


    fun BigoneIn() {
        bigone.set(Value.kForward)
    }

    fun BigoneOut() {
        bigone.set(Value.kReverse)
    }

    fun BigoneOff() {
        bigone.set(Value.kOff)
    }


    fun AnothaoneIn() {
        anothaone.set(Value.kForward)
    }

    fun AnothaoneOut() {
        anothaone.set(Value.kReverse)
    }

    fun AnothaoneOff() {
        anothaone.set(Value.kOff)
    }

    fun disabled() {
        comp.stop()
        //		kicker.set();
    }

    //	public void checkStatus() {
    //		if (comp.getPressureSwitchValue()) comp.stop();
    //		else comp.start();
    //	}

    /*
	 * public void start() { time.reset(); time.start(); }
	 * 
	 * public double getTime() { return time.get(); }
	 */

}