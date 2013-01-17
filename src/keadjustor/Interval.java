package keadjustor;

public class Interval {
	private double min;
	private double max;
	
	public Interval(double min, double max) {
		this.min = min;
		this.max = max;
	}

	public double getMin() {
		return min;
	}
	public double getMax() {
		return max;
	}
	
	// < 0 if val is too small
	// = 0 if ok
	// > 0 if val is too big
	public int check(double val) {
		if (val < min) return -1;
		if (val > max) return 1;
		return 0;
	}
	
	@Override
	public String toString() {
		return String.format("[ %.2f, %.2f ]", min, max);
	}
}
