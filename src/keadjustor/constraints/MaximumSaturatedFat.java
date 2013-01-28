package keadjustor.constraints;


public class MaximumSaturatedFat extends ParameterConstraint<Double> {

	public MaximumSaturatedFat(double value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ParameterConstraint<Double> verify(Double in) {
		if(in > this.value)
			violated = true;
		else
			violated = false;		
		return this;
	}
}
