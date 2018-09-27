package com.codered.shaders.object.simple;

import java.util.List;

import com.codered.window.WindowContext;

import cmn.utilslib.dmap.dmaps.DMap2;

public class No_OShader extends TexturedObjectShader
{

	public No_OShader(WindowContext context)
	{
		super(context);
		
		compile();

		getAllUniformLocations();
	}

	public void use()
	{
		start();
		
		super.use();
	}

	public void attachShaderParts()
	{
		attachVertexShader(this.context.getShaderParts().builtIn().getVertexShader("o_no"));
		attachFragmentShader(this.context.getShaderParts().builtIn().getFragmentShader("o_no"));
	}

	public void getAttribs(List<DMap2<Integer,String>> attribs)
	{
		attribs.add(new DMap2<Integer,String>(0, "vertexPos"));
		attribs.add(new DMap2<Integer,String>(1, "texCoords"));
	}
	
}
