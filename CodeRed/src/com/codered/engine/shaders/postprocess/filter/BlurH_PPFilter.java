package com.codered.engine.shaders.postprocess.filter;

import com.codered.engine.shader.Shader.Attrib;
import com.codered.engine.shader.Shader.FragmentShader;
import com.codered.engine.shader.Shader.VertexShader;

@VertexShader("ppf_blurHorizontal")
@FragmentShader("ppf_blur")
@Attrib(pos=0, var="pos")
public class BlurH_PPFilter extends PPFShader
{
	




	protected void getAllUniformLocations()
	{
		addUniform("frame");
		addUniform("targetWidth");
	}
	
	public void use()
	{
		start();
		loadTexture("frame", 0, getInput("frame"));
		loadFloat("targetWidth", getInput("targetWidth"));
	}
}
