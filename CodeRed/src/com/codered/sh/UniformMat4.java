package com.codered.sh;

import org.barghos.math.matrix.Mat4f;

public class UniformMat4 extends Uniform
{

	private Mat4f mat = new Mat4f();
	
	private int location = -1;
	
	public UniformMat4(String name, Object... data)
	{
		super(name);
	}

	@Override
	public void set(Object obj)
	{
		if(!(obj instanceof Mat4f)) throw new IllegalArgumentException();
		Mat4f m = (Mat4f)obj;
		mat.set(m);
	}

	@Override
	public void load()
	{
		loadMat4(this.location, this.mat);
	}

	@Override
	public void loadUniformLocations(int shaderProgrammId)
	{
		this.location = getLocationFor(this.name, shaderProgrammId);
	}

}
