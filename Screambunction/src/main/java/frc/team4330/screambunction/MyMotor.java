package frc.team4330.screambunction;

import edu.wpi.first.wpilibj.Jaguar;
import jaci.openrio.toast.lib.registry.Registrar;

public class MyMotor {

    private Jaguar myMotor = Registrar.jaguar(0);

    public void runMotor ( ) {
        myMotor.set(1.0);
    }

    public void stopMotor ( ) {
        myMotor.set(0.0);
    }

    public double getSpeed ( ) {
        return myMotor.getSpeed();
    }
}
