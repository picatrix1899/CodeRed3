package com.codered.demo;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import cmn.utilslib.color.colors.LDRColor3;
import cmn.utilslib.math.matrix.Matrix4f;
import cmn.utilslib.math.vector.Vector2f;
import cmn.utilslib.math.vector.Vector3f;

import com.codered.engine.Game;
import com.codered.engine.entities.DynamicEntity;
import com.codered.engine.entities.StaticEntity;
import com.codered.engine.managing.Paths;
import com.codered.engine.managing.VAO;
import com.codered.engine.managing.World;
import com.codered.engine.fbo.FBO;
import com.codered.engine.input.Input;
import com.codered.engine.input.InputConfiguration;
import com.codered.engine.input.Key;
import com.codered.engine.managing.loader.LambdaFont;
import com.codered.engine.rendering.PrimitiveRenderer;
import com.codered.engine.rendering.TextRenderer;
import com.codered.engine.rendering.WorldRenderer;
import com.codered.engine.resource.ResourceManager;
import com.codered.engine.shaders.gui.GUIShader;
import com.codered.engine.shaders.object.SimpleObjectShader;
import com.codered.engine.shaders.postprocess.filter.PPFShader;
import com.codered.engine.shader.MalformedShaderException;
import com.codered.engine.shader.ShaderNotFoundException;
import com.codered.engine.shaders.terrain.SimpleTerrainShader;
import com.codered.engine.utils.DebugInfo;
import com.codered.engine.utils.GLUtils;
import com.codered.engine.window.Window;
import com.codered.demo.GlobalSettings.Keys;

import com.google.common.collect.Lists;


@SuppressWarnings("unused")
public class DemoGame
{

	public void init()
	{
		GL11.glClearColor(0,0,0,1);		
			
		Session.get().setWorld(new DemoWorld());
		Session.get().setPlayer(new Player());
		Session.get().getPlayer().window = new GUIIngameOverlay();
		
		PrimitiveRenderer.create();
		
		Window.named_windows.get("main").initContext();
		gui1 = new GUIIngameOverlay();
		
		Window.named_windows.get("test").initContext();
		gui2 = new GUIIngameOverlay();		

	}

	public void update()
	{
		Window.active.updateDisplay();

		if(Session.get().getPlayer().window.allowWorldProcessing())
		{
			for(DynamicEntity e : Session.get().getWorld().getDynamicEntities())
			{
				e.update(Session.get().getWorld());
			}
		}
		
		gui1.update();
		gui2.update();
	}

	GUIIngameOverlay gui1;
	GUIIngameOverlay gui2;
	
	public void render()
	{
		if(Window.active.getId() == "main")
		{
			GLUtils.bindFramebuffer(0);
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			Window.named_windows.get("main").getContext().worldRenderer.bindFramebuffer();
			
			Window.named_windows.get("main").getContext().worldRenderer.render(Session.get().getWorld(), Session.get().getPlayer().getCamera());	
			
			gui1.render();
		}
		
		if(Window.active.getId() == "test")
		{
			GLUtils.bindFramebuffer(0);
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			gui2.render();
		}
	}

	public void loadStaticResources()
	{
		try
		{
			ResourceManager.GUI.regFont("lucida", new LambdaFont(new File(Paths.p_fonts + "lucida" + Paths.e_lambdafont)).load());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
