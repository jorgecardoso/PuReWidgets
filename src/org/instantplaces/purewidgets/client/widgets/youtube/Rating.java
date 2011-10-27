/**
 * 
 */
package org.instantplaces.purewidgets.client.widgets.youtube;

/**
 * @author Jorge C. S. Cardoso
 *
 */
public class Rating {
	private float average;
	private int max;
	private int min;
	private int numRaters;
	private int normalized;
	
	public Rating(int min, int max, float average, int numRaters) {
		this.setMin(min);
		this.setMax(max);
		this.setAverage(average);
		this.setNumRaters(numRaters);
		
		this.setNormalized((int)(average/max*100));
	}

	/**
	 * @param average the average to set
	 */
	public void setAverage(float average) {
		this.average = average;
	}

	/**
	 * @return the average
	 */
	public float getAverage() {
		return average;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(int min) {
		this.min = min;
	}

	/**
	 * @return the min
	 */
	public int getMin() {
		return min;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * @return the max
	 */
	public int getMax() {
		return max;
	}

	/**
	 * @param numRaters the numRaters to set
	 */
	public void setNumRaters(int numRaters) {
		this.numRaters = numRaters;
	}

	/**
	 * @return the numRaters
	 */
	public int getNumRaters() {
		return numRaters;
	}

	/**
	 * @param normalized the normalized to set
	 */
	public void setNormalized(int normalized) {
		this.normalized = normalized;
	}

	/**
	 * @return the normalized
	 */
	public int getNormalized() {
		return normalized;
	}
}
