package com.codered.demo;

import java.io.File;

import org.lwjgl.opengl.GL11;

import com.codered.demo.GlobalSettings.Keys;
import com.codered.engine.BuiltInShaders;
import com.codered.engine.entities.Camera;
import com.codered.engine.entities.StaticEntity;
import com.codered.engine.input.InputConfiguration;
import com.codered.engine.input.Key;
import com.codered.engine.light.AmbientLight;
import com.codered.engine.light.DirectionalLight;
import com.codered.engine.managing.Material;
import com.codered.engine.managing.Paths;
import com.codered.engine.managing.loader.MaterialLoader;
import com.codered.engine.managing.loader.TextureLoader;
import com.codered.engine.managing.loader.data.OBJFile;
import com.codered.engine.managing.loader.data.TextureData;
import com.codered.engine.managing.models.Mesh;
import com.codered.engine.managing.models.TexturedModel;
import com.codered.engine.shaders.object.SimpleObjectShader;
import com.codered.engine.shaders.object.simple.AmbientLight_OShader;
import com.codered.engine.shaders.object.simple.DirectionalLight_N_OShader;
import com.codered.engine.shaders.object.simple.DirectionalLight_OShader;
import com.codered.engine.utils.GLUtils;
import com.codered.engine.utils.MathUtils;
import com.codered.engine.utils.TextureUtils;
import com.codered.engine.utils.WindowHint;
import com.codered.engine.utils.WindowHint.GLProfile;
import com.codered.engine.window.WindowRoutine;

import cmn.utilslib.color.colors.LDRColor3;
import cmn.utilslib.math.matrix.Matrix4f;
import cmn.utilslib.math.vector.Vector3f;

public class DemoWindowContext1 extends WindowRoutine
{
	private Mesh mesh = new Mesh();
	private Material mat;
	private TexturedModel model;
	private StaticEntity entity;
	
	private Matrix4f projection;
	
	private AmbientLight ambient;
	private DirectionalLight directionalLight;
	
	private Player player;
	
	public void initWindowHints()
	{
		WindowHint.resizable(false);
		WindowHint.glVersion("4.2");
		WindowHint.glProfile(GLProfile.CORE);
		WindowHint.depthBits(24);
		WindowHint.doubleBuffering(true);
		WindowHint.samples(16);
	}

	public void init()
	{
		InputConfiguration config = new InputConfiguration();
		config.registerKey(Keys.k_forward);
		config.registerKey(Keys.k_back);
		config.registerKey(Keys.k_left);
		config.registerKey(Keys.k_right);
		config.registerKey(Keys.k_up);
		config.registerKey(Keys.k_exit);
		config.registerKey(Keys.k_turnLeft);
		config.registerKey(Keys.k_turnRight);
		config.registerKey(Keys.k_delete);

		config.registerButton(Keys.b_moveCam);
		config.registerButton(Keys.b_fire);
		
		this.context.getInputManager().setConfiguration(config);
		
		this.context.getInputManager().keyStroke.addHandler((a,b) -> {if(a.response.keyPresent(Key.ESCAPE)) this.context.getWindow().setWindowShouldClose(); });
		
		BuiltInShaders.init();
		
		this.context.addShader(AmbientLight_OShader.class);
		this.context.addShader(DirectionalLight_N_OShader.class);
		this.context.addShader(DirectionalLight_OShader.class);
		
		this.projection = MathUtils.createProjectionMatrix(this.context.getSize(), 60, 45, 0.1f, 1000);
		
		OBJFile obj = new OBJFile();
		obj.load(new File(Paths.p_models + "crate.obj"));
		this.mesh.loadFromObj(obj);
		
		this.mat = MaterialLoader.load("crate");
		
		this.model = new TexturedModel(this.mesh, this.mat);
		
		this.entity = new StaticEntity(this.model, new Vector3f(0,0,-40), -45, 45, 0);
		
		TextureData tdata = TextureLoader.loadTexture("crate");
		TextureData tdata2 = TextureLoader.loadTexture("crate_normal");
		
		this.context.getResourceManager().WORLD.regTexture("crate", TextureUtils.genTexture(tdata, this.context));
		this.context.getResourceManager().WORLD.regTexture("crate_normal", TextureUtils.genTexture(tdata2, this.context));
		
		this.player = new Player();
		
		this.ambient = new AmbientLight(new LDRColor3(120, 100, 100), 1);
		
		this.directionalLight = new DirectionalLight(200, 100, 100, 2, 1.0f, -1.0f, 0);
	} 

	public void render(double delta)
	{
		GLUtils.toggleMultisample(true);
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GLUtils.toggleDepthTest(true);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		
		renderObject(this.entity, this.player.getCamera(), this.context.getShader(AmbientLight_OShader.class));			
		
		GLUtils.toggleBlend(true);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);		
		
		renderObject(this.entity, this.player.getCamera(), this.context.getShader(DirectionalLight_N_OShader.class));
		

		GLUtils.toggleBlend(false);
		GLUtils.toggleDepthTest(false);

		GLUtils.toggleMultisample(false);
	}

	private void renderObject(StaticEntity e, Camera c, SimpleObjectShader oShader)
	{
		oShader.u_camera.set(c);
		oShader.u_T_model.set(e.getTransformationMatrix());
		oShader.u_T_projection.set(this.projection);
		
		oShader.setInput("material", e.getModel().getTexture());
		
		oShader.setInput("directionalLight", this.directionalLight);
		oShader.setInput("ambientLight", this.ambient);
		
		oShader.use();			
		{	
			GLUtils.bindVAO(e.getModel().getModel().getVAO(), 0, 1, 2, 3);
			
			GL11.glDrawElements(GL11.GL_TRIANGLES, e.getModel().getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		}
		oShader.stop();	
	}

	public void preloadResources()
	{
		BuiltInShaders.preloadResources();
	}

}