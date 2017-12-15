package frc.team4330.robot.subsystems

import edu.wpi.first.wpilibj.Relay
import edu.wpi.first.wpilibj.Relay.Direction
import edu.wpi.first.wpilibj.Relay.Value
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team4330.sensors.vision.VisionComms

class VisionSystem : Subsystem() {

    private val visLift: VisionComms
    private val visBoiler: VisionComms? = null
    private val ledSwitch: Relay

    val boilerAngle: Double?
        get() = visBoiler!!.retrieveData()[VISION_KEY]

    //		System.out.println("No " + VISION_KEY + " messages.");
    val liftAngle: Double?
        get() {
            val values = visLift.retrieveData()
            return if (values.containsKey(VISION_KEY)) {
                values[VISION_KEY]?.minus(3)
            } else null
        }

    val liftAngleCentered: Double?
        get() = 4.0

    val boilerData: Map<String, Double>
        get() = visBoiler!!.retrieveData()

    val liftData: Map<String, Double>
        get() = visLift.retrieveData()

    init {
        visLift = VisionComms("tegra-ubuntu", 9001)
        //		visBoiler = new VisionComms("tegra-ubuntu", 9002);

        ledSwitch = Relay(3, Direction.kForward)
        ledSwitch.set(Value.kForward)
    }

    fun startUp() {
        try {
            //			visBoiler.startUp();
            visLift.startUp()
            ledSwitch.set(Value.kForward)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun shutDown() {
        try {

            visLift.shutDown()
            //			visBoiler.shutDown();
            ledSwitch.set(Value.kOff)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun ledOn() {
        ledSwitch.set(Value.kForward)
    }

    override fun initDefaultCommand() {}

    companion object {

        val VISION_KEY = "rb"
    }

}
