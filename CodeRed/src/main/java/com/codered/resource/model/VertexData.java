package com.codered.resource.model;

import org.barghos.core.tuple.tuple2.Tup2fR;
import org.barghos.core.tuple.tuple3.Tup3fR;
import org.barghos.math.vector.vec2.Vec2;
import org.barghos.math.vector.vec2.Vec2R;
import org.barghos.math.vector.vec3.Vec3;
import org.barghos.math.vector.vec3.Vec3R;

public class VertexData
{
	private Vec3 position = new Vec3();
	private Vec3 normal = new Vec3();
	private Vec3 tangent = new Vec3();
	private Vec2 uv = new Vec2();
	
	public VertexData(Tup3fR position, Tup3fR normal, Tup3fR tangent, Tup2fR uv)
	{
		this.position.set(position);
		this.normal.set(normal);
		this.tangent.set(tangent);
		this.uv.set(uv);
	}
	
	public Vec3R getPosition()
	{
		return this.position;
	}
	
	public Vec3R getNormal()
	{
		return this.normal;
	}
	
	public Vec3R getTangent()
	{
		return this.tangent;
	}
	
	public Vec2R getUV()
	{
		return this.uv;
	}
}