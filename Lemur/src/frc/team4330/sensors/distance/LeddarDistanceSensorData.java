package frc.team4330.sensors.distance;

public final class LeddarDistanceSensorData implements Comparable<LeddarDistanceSensorData> {
	
	@Override
	public int compareTo(LeddarDistanceSensorData o) {
		return this.segmentNumber - o.getSegmentNumber();
	}

	private int segmentNumber;
	private int distanceInCentimeters;
	private double amplitude;
	
	public LeddarDistanceSensorData(int segmentNumber, int distanceInCentimeters, double amplitude) {
		this.segmentNumber = segmentNumber;
		this.distanceInCentimeters = distanceInCentimeters;
		this.amplitude = amplitude;
	}
	

	/**
	 * The Leddar distance sensor has 16 segments
	 * @return the segment number: 0 based index with values 0-15
	 */
	public int getSegmentNumber() {
		return segmentNumber;
	}

	/**
	 * 
	 * @return the distance measurement in centimeters
	 */
	public int getDistanceInCentimeters() {
		return distanceInCentimeters;
	}

	/**
	 * 
	 * @return the strength of the signal, max value is 1024
	 */
	public double getAmplitude() {
		return amplitude;
	}
	
	public String toString() {
		return ("{Seg=" + segmentNumber + " Dist=" + distanceInCentimeters + " Amp=" + amplitude + "}");
	}

}
