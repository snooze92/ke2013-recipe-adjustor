package keadjustor.constraints;


public class MaximumProtein extends ParameterConstraint<Double> {

	public MaximumProtein(double value) {
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
