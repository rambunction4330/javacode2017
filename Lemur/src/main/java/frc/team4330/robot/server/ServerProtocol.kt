package frc.team4330.robot.server

import frc.team4330.robot.Robot

class ServerProtocol {

    fun processDataRequests(requestSeg: String): String? {
        val output: String?

        if (requestSeg.equals("DATA", ignoreCase = true))
            output = ("[" + Robot.gyro.velocityX + ", "
                    + Robot.gyro.velocityY + ", "
                    + Robot.gyro.angle + ", "
                    + Robot.gyro.rate + "]")
        else
            output = null


        return output
    }
}
