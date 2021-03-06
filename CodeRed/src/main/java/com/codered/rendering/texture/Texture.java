package com.codered.rendering.texture;

import com.codered.ResourceHolder;
import com.codered.utils.GLCommon;

public class Texture implements ResourceHolder
{
	
	private int id;
	
	private int width;
	private int height;
	
	public Texture(int id, int width, int height)
	{
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}

	public void release(boolean forced)
	{
		GLCommon.deleteTexture(this.id);
		this.id = -1;
	}
}
