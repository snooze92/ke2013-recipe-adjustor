package keadjustor.constraints;


public class MinimumCalories extends ParameterConstraint<Double> {

	public MinimumCalories(double value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ParameterConstraint<Double> verify(Double in) {
		if(in < this.value)
			violated = true;
		else
			violated = false;		
		return this;
	}
}
