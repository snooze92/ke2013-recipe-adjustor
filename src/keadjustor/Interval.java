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
	public double check(double val) {
		if (val < min) return min - val;
		if (val > max) return val - max;
		return 0.0;
	}
	
	@Override
	public String toString() {
		return String.format("[ %.2f, %.2f ]", min, max);
	}
}
