#version 400 core

layout(points) in;
layout(triangle_strip, max_vertices = 5) out;

in vec2 pass_scale[];
in vec4 pass_uvs[];

out vec2 texCoords;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

vec4 getScreenCoord(vec4 v)
{
//	float sx = 1280;
//	float sy = 720;
	
//	float ix = v.x;
//	float iy = v.y;
	
//	float x = ((ix/sx)*2-1);
//	float y = -((iy/sy)*2-1);
	
//	return vec4(x, y, v.z, v.w);

v = v * vec4(1,-1,1,1);

	return projectionMatrix * viewMatrix * transformationMatrix * v; 
}

vec4 getScreenCoord1(vec4 v)
{
	float sx = 1280;
	float sy = 720;
	
	float ix = v.x;
	float iy = v.y;

	float x = ((ix/sx)*2-1);
	float y = -((iy/sy)*2-1);
	
	return vec4(x, y, v.z, v.w);

//	return projectionMatrix * viewMatrix * trans v; 
}

void main()
{

		vec3 scale = vec3(pass_scale[0], 0);

		texCoords = pass_uvs[0].xy;
	
        gl_Position = getScreenCoord((gl_in[0].gl_Position + vec4(-scale.x, scale.y, scale.z, 0.0)));
        EmitVertex();
        
        texCoords = pass_uvs[0].zy;
        
        gl_Position = getScreenCoord((gl_in[0].gl_Position + vec4(scale.x, scale.y, scale.z, 0.0)));
        EmitVertex();

		texCoords = pass_uvs[0].zw;

        gl_Position = getScreenCoord((gl_in[0].gl_Position + vec4(scale.x, -scale.y, scale.z, 0.0)));
        EmitVertex();
		
		texCoords = pass_uvs[0].xw;
		
        gl_Position = getScreenCoord((gl_in[0].gl_Position + vec4(-scale.x, -scale.y, scale.z, 0.0)));
        EmitVertex();

		texCoords = pass_uvs[0].xy;

        gl_Position = getScreenCoord((gl_in[0].gl_Position + vec4(-scale.x, scale.y, scale.z, 0.0)));
        EmitVertex();
	
	EndPrimitive();
}
