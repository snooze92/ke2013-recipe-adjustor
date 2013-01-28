package keadjustor.constraints;


public class MaximumCalories extends ParameterConstraint<Double> {

	public MaximumCalories(double value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ParameterConstraint<Double> verify(Double in) {
		if(in > this.value)
		{
			violated = true;
		}
		return this;
	}
}
