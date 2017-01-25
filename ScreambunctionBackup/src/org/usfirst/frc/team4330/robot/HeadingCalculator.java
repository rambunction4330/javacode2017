package org.usfirst.frc.team4330.robot;

public class HeadingCalculator {

	/**
	 * Calculate the course changed needed to go from current heading to desired heading.  Both
	 * the current heading and desired heading should be in degrees with 360 degrees in a revolution.
	 * The calculation will work even if the current heading or the desired heading have 
	 * accumulation of multiple revolutions resulting in values less than -180 or greater than 180.
	 * @param currentHeading
	 * @param desiredHeading
	 * @return the course change with -180 < value <= 180.  Positive value means to turn right
	 * to come to desired heading and negative value means to turn left to come to desired heading.
	 */
	public static double calculateCourseChange ( double currentHeading, double desiredHeading ) {

		double desiredPositive = normalize(desiredHeading);
		double currentPositive = normalize(currentHeading);

		if (desiredPositive < 0) {
			desiredPositive += 360;
		} 

		if ( currentPositive < 0 ) {
			currentPositive += 360;
		}

		double courseChange = desiredPositive - currentPositive;

		if ( courseChange <= -180 ) {
			courseChange += 360;
		} else if ( courseChange > 180 ) {
			courseChange -= 360;
		}

		return courseChange;
	}

	/**
	 * 
	 * @param raw
	 * @return raw value normalized so -180 < value <= 180
	 */
	public static double normalize(double raw) {

		if (raw > 0) {
			raw = raw % 360;

			if (raw > 180) {
				raw -= 360;
			}
		} else {
			raw = raw % 360;

			if (raw < -180) {
				raw += 360;
			}
		}

		if ( raw == -180 ) {
			raw = 180;
		}

		return raw;
	}

}
