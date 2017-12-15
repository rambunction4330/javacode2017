package frc.team4330.robot

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard


object DashboardManager {

    val left = 1
    val middle = 2
    val right = 3

    val autoManager = "!"
    val autoCommands = "?"

    private val positionChooser = SendableChooser<Int>()

    private val autoChooser = SendableChooser<String>()

    val method: String
        get() = autoChooser.selected

    val start: Int
        get() = positionChooser.selected

    init {
        positionChooser.addDefault("Middle", middle)
        positionChooser.addObject("Left", left)
        positionChooser.addObject("Right", right)
        SmartDashboard.putData("Autonomous Position", positionChooser)
    }

    init {
        autoChooser.addDefault("Use AutoManager", autoManager)
        autoChooser.addObject("Use commands", autoCommands)
        SmartDashboard.putData("Autonomous Method", autoChooser)
    }

    fun start() {
        SmartDashboard.putData("Autonomous Position", positionChooser)
        SmartDashboard.putData("Autonomous Method", autoChooser)
    }
}
