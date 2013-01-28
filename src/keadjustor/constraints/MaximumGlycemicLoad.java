package keadjustor.constraints;


public class MaximumGlycemicLoad extends ParameterConstraint<Double> {

	public MaximumGlycemicLoad(double value) {
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
