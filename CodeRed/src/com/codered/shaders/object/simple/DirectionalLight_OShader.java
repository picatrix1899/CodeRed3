package com.codered.shaders.object.simple;

import java.util.List;

import com.codered.EngineRegistry;
import com.codered.shader.UniformDirectionalLight;
import com.codered.window.WindowContext;

import cmn.utilslib.dmap.dmaps.DMap2;

public class DirectionalLight_OShader extends TexturedObjectShader
{

	public UniformDirectionalLight u_directionalLight  = new UniformDirectionalLight("directionalLight");
	
	public DirectionalLight_OShader(WindowContext context)
	{
		super(context);

		addUniform(u_directionalLight);
		
		compile();
		
		getAllUniformLocations();
	}

	public void attachShaderParts()
	{
		attachVertexShader(EngineRegistry.getShaderParts().builtIn().getVertexShader("o_directionalLight"));
		attachFragmentShader(EngineRegistry.getShaderParts().builtIn().getFragmentShader("o_directionalLight"));
	}

	public void getAttribs(List<DMap2<Integer,String>> attribs)
	{
		attribs.add(new DMap2<Integer,String>(0, "vertexPos"));
		attribs.add(new DMap2<Integer,String>(1, "texCoords"));
		attribs.add(new DMap2<Integer,String>(2, "normal"));
	}
}
