package com.codered.engine.shader;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import com.codered.engine.managing.Texture;
import com.codered.engine.window.WindowContext;
import com.google.common.collect.Maps;

import cmn.utilslib.color.colors.api.IColor3Base;
import cmn.utilslib.dmap.dmaps.DMap2;
import cmn.utilslib.essentials.Auto;
import cmn.utilslib.math.matrix.Matrix4f;
import cmn.utilslib.math.vector.api.Vec2fBase;
import cmn.utilslib.math.vector.api.Vec3fBase;
import cmn.utilslib.math.vector.api.Vec4fBase;

public abstract class ShaderProgram
{
	private FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	protected int programID;
	
	private List<ShaderPart> geometryShaders = Auto.ArrayList();
	private List<ShaderPart> vertexShaders = Auto.ArrayList();
	private List<ShaderPart> fragmentShaders = Auto.ArrayList();
	
	private HashMap<String,Integer> uniforms = Maps.newHashMap();
	
	private HashMap<String,Object> inputs = Maps.newHashMap();
	
	protected WindowContext context;
	
	public ShaderProgram(WindowContext context)
	{
		this.context = context;
	}
	
	public void setInput(String name, Object val)
	{
		this.inputs.put(name, val);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getInput(String name)
	{
		return (T) this.inputs.get(name);
	}
	
	public void compile()
	{
		this.programID = GL20.glCreateProgram();

		attachShaderParts();
		
		for(ShaderPart p : this.geometryShaders)
			GL20.glAttachShader(programID, p.getId());
		
		for(ShaderPart p  : this.vertexShaders)
			GL20.glAttachShader(programID, p.getId());

		for(ShaderPart p  : this.fragmentShaders)
			GL20.glAttachShader(programID, p.getId());

		ArrayList<DMap2<Integer,String>> attribs = new ArrayList<DMap2<Integer,String>>();
		
		getAttribs(attribs);
		
		for(DMap2<Integer,String> attrib : attribs)
		{
			bindAttribute(attrib.getA(), attrib.getB());
		}
		
		GL20.glLinkProgram(this.programID);
		
		GL20.glValidateProgram(this.programID);
	}
	
	protected void attachGeometryShader(ShaderPart part)
	{
		this.geometryShaders.add(part);
	}
	
	protected void attachFragmentShader(ShaderPart part)
	{
		this.fragmentShaders.add(part);
	}
	
	protected void attachVertexShader(ShaderPart part)
	{
		this.vertexShaders.add(part);
	}

	public abstract void use();

	public abstract void attachShaderParts();
	
	public abstract void getAttribs(List<DMap2<Integer,String>> attribs);
	
	public void start()
	{
		GL20.glUseProgram(this.programID);
	}
	
	public void stop()
	{
		GL20.glUseProgram(0);
	}
	
	public void cleanup()
	{
		GL20.glUseProgram(0);
		
		for(ShaderPart p : this.geometryShaders)
			GL20.glDetachShader(this.programID, p.getId());
		
		for(ShaderPart p : this.vertexShaders)
			GL20.glDetachShader(this.programID, p.getId());
		
		for(ShaderPart p : this.fragmentShaders)
			GL20.glDetachShader(this.programID, p.getId());
		
		GL20.glDeleteProgram(this.programID);
		
		this.geometryShaders.clear();
		this.vertexShaders.clear();
		this.fragmentShaders.clear();
	}
	
	protected void bindAttribute(int attrib, String var)
	{
		GL20.glBindAttribLocation(this.programID, attrib, var);
	}
	
	protected int getUniformLocation(String uniform)
	{
		return GL20.glGetUniformLocation(this.programID, uniform);
	}
	
	protected void addUniform(String uniform)
	{
		this.uniforms.put(uniform, GL20.glGetUniformLocation(this.programID, uniform));
	}
	
	
	protected abstract void getAllUniformLocations();
	
	
	
	protected void loadFloat(int location, float val)
	{
		GL20.glUniform1f(location, val);
	}
	
	protected void loadColor3(int location, IColor3Base val)
	{
		GL20.glUniform3f(location, val.getUnityR(), val.getUnityG(), val.getUnityB());
	}
	
	protected void loadVector2(int location, Vec2fBase val)
	{
		GL20.glUniform2f(location,val.getX(), val.getY());
	}
	
	protected void loadVector3(int location, Vec3fBase val)
	{
		GL20.glUniform3f(location, val.getX(), val.getY(), val.getZ());
	}
	
	protected void loadBoolean(int location, boolean val)
	{
		loadFloat(location, val ? 1 : 0);
	}
	
	protected void loadInt(int location, int val)
	{
		GL20.glUniform1i(location, val);
	}
	
	protected void loadMatrix(int location, Matrix4f val)
	{
		matrixBuffer.put(val.getColMajor());
		
		matrixBuffer.flip();
		
		GL20.glUniformMatrix4fv(location, false, matrixBuffer);
		
		matrixBuffer = BufferUtils.createFloatBuffer(16);
	}
	
	protected void loadFloat(String uniform, float val)
	{
		GL20.glUniform1f(this.uniforms.get(uniform), val);
	}
	
	protected void loadVector2(String uniform, Vec2fBase val)
	{
		GL20.glUniform2f(this.uniforms.get(uniform),val.getX(), val.getY());
	}
	
	protected void loadColor3(String uniform, IColor3Base val)
	{
		GL20.glUniform3f(this.uniforms.get(uniform), val.getUnityR(), val.getUnityG(), val.getUnityB());
	}
	
	protected void loadVector3(String uniform, Vec3fBase val)
	{
		GL20.glUniform3f(this.uniforms.get(uniform), val.getX(), val.getY(), val.getZ());
	}
	
	protected void loadVector4(String uniform, Vec4fBase val)
	{
		GL20.glUniform4f(this.uniforms.get(uniform), val.getX(), val.getY(), val.getZ(), val.getA());
	}
	
	protected void loadBoolean(String uniform, boolean val)
	{
		loadFloat(this.uniforms.get(uniform), val ? 1 : 0);
	}
	
	protected void loadInt(String uniform, int val)
	{
		GL20.glUniform1i(this.uniforms.get(uniform), val);
	}
	
	protected void loadMatrix(String uniform, Matrix4f val)
	{
		matrixBuffer.put(val.getColMajor());
		
		matrixBuffer.flip();
		
		GL20.glUniformMatrix4fv(this.uniforms.get(uniform), false, matrixBuffer);
		
		matrixBuffer = BufferUtils.createFloatBuffer(16);
	}
	
	protected void loadTextureId(String uniform, int attrib, int texture)
	{
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + attrib);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		loadInt(uniform, attrib);
	}
	
	protected void loadTexture(String uniform, int attrib, Texture texture)
	{
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + attrib);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
		loadInt(uniform, attrib);
	}
	
	protected void loadTextureMSId(String uniform, int attrib, int texture)
	{
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + attrib);
		GL11.glBindTexture(GL32.GL_TEXTURE_2D_MULTISAMPLE, texture);
		loadInt(uniform, attrib);
	}
	
	protected void loadTextureMS(String uniform, int attrib, Texture texture)
	{
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + attrib);
		GL11.glBindTexture(GL32.GL_TEXTURE_2D_MULTISAMPLE, texture.getId());
		loadInt(uniform, attrib);
	}
}