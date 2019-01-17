package com.codered.shader;

import org.barghos.core.api.tuple.ITup3R;

import cmn.utilslib.math.vector.api.Vec3fBase;

public class UniformVector3 extends Uniform
{

	public UniformVector3(String name)
	{
		super(name);
		
		addUniform("");
	}

	public void set(Vec3fBase vector)
	{
		loadVector3("", vector);
	}
	
	public void set(ITup3R vector)
	{
		loadVector3("", vector);
	}
	
}
