package keadjustor.constraints;


public abstract class ParameterConstraint<T> extends RecipeConstraint {
	protected T value;	
	public abstract ParameterConstraint<T> verify(T in);
	
	public ParameterConstraint(T value) {
		this.value = value;
	}

	public T getValue()
	{
		return value;
	}
	
	public String toString()
	{
		return this.getClass().toString() + "(" + value.toString() + ")";
	}
}
