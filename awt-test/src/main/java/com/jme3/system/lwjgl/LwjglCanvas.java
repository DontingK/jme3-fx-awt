package com.jme3.system.lwjgl;

import com.jme3.app.Application;
import com.jme3.input.*;
import com.jme3.input.awt.AwtKeyInput;
import com.jme3.input.awt.AwtMouseInput;
import com.jme3.opencl.Context;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.RendererException;
import com.jme3.renderer.lwjgl.LwjglGL;
import com.jme3.renderer.lwjgl.LwjglGLExt;
import com.jme3.renderer.lwjgl.LwjglGLFboEXT;
import com.jme3.renderer.lwjgl.LwjglGLFboGL3;
import com.jme3.renderer.opengl.*;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.SystemListener;
import com.jme3.system.Timer;
import org.lwjgl.opengl.ARBDebugOutput;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;

public class LwjglCanvas  implements JmeContext {
    protected static final String THREAD_NAME = "jME3 Main";

    private Thread renderThread;

    private class LwjglAWTGLCanvas extends AWTGLCanvas{

        public LwjglAWTGLCanvas(GLData data) {
            super(data);
        }

        @Override
        public void initGL() {
            initContent(true);
            glClearColor(0.3f, 0.4f, 0.5f, 1);

        }

        @Override
        public void paintGL() {
            runLoop();
        }

        @Override
        public void addNotify() {
            super.addNotify();
            canvas.setFocusable(true);
            canvas.setIgnoreRepaint(true);
            renderThread = new Thread(() -> {
                while (true){
                    canvas.render();
                }
            }, THREAD_NAME);
            renderThread.start();
        }
    }


    private static final Set<String> SUPPORTED_RENDERS = new HashSet<>(Arrays.asList(
            AppSettings.LWJGL_OPENGL2,
            AppSettings.LWJGL_OPENGL30,
            AppSettings.LWJGL_OPENGL31,
            AppSettings.LWJGL_OPENGL32,
            AppSettings.LWJGL_OPENGL33,
            AppSettings.LWJGL_OPENGL40,
            AppSettings.LWJGL_OPENGL41,
            AppSettings.LWJGL_OPENGL42,
            AppSettings.LWJGL_OPENGL43,
            AppSettings.LWJGL_OPENGL44,
            AppSettings.LWJGL_OPENGL45
    ));
    private AWTGLCanvas canvas;
    private AppSettings settings;
    private SystemListener systemListener;
    private Renderer renderer;
    protected final Object createdLock = new Object();
    protected final AtomicBoolean created = new AtomicBoolean(false);
    protected final AtomicBoolean renderable = new AtomicBoolean(false);

    protected Timer timer;
    protected com.jme3.opencl.lwjgl.LwjglContext clContext;

    private AwtKeyInput keyInput;
    private AwtMouseInput mouseInput;

    public LwjglCanvas() {

    }

    @Override
    public Type getType() {
        return Type.Canvas;
    }

    @Override
    public void setSettings(AppSettings settings) {
        GLData data = new GLData();
        data.majorVersion = 3;
        data.minorVersion = 3;
        data.profile = GLData.Profile.CORE;
        data.samples = settings.getSamples();
        this.settings = settings;
        canvas=new LwjglAWTGLCanvas(data);
    }

    @Override
    public SystemListener getSystemListener() {
        return systemListener;
    }

    @Override
    public void setSystemListener(SystemListener listener) {
        this.systemListener = listener;
    }

    @Override
    public AppSettings getSettings() {
        return settings;
    }

    @Override
    public Renderer getRenderer() {
        return renderer;
    }

    @Override
    public Context getOpenCLContext() {
        return null;
    }

    @Override
    public MouseInput getMouseInput() {
        return mouseInput;
    }

    @Override
    public KeyInput getKeyInput() {
        return keyInput;
    }

    @Override
    public JoyInput getJoyInput() {
        return null;
    }

    @Override
    public TouchInput getTouchInput() {
        return null;
    }

    @Override
    public Timer getTimer() {
        return timer;
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public boolean isCreated() {
        return created.get();
    }

    @Override
    public boolean isRenderable() {
        return renderable.get();
    }

    @Override
    public void setAutoFlushFrames(boolean enabled) {

    }

    @Override
    public void create(boolean waitFor) {

    }


    @Override
    public void restart() {

    }

    @Override
    public void destroy(boolean waitFor) {

    }

    @Override
    public int getFramebufferHeight() {
        return 200;
    }

    @Override
    public int getFramebufferWidth() {
        return 200;
    }

    @Override
    public int getWindowXPosition() {
        return 0;
    }

    @Override
    public int getWindowYPosition() {
        return 0;
    }


    private void initContent(boolean first) {
        final String renderer = settings.getRenderer();
        final GLCapabilities capabilities = createCapabilities(!renderer.equals(AppSettings.LWJGL_OPENGL2));

        if (!capabilities.OpenGL20) {
            throw new RendererException("OpenGL 2.0 or higher is required for jMonkeyEngine");
        } else if (!SUPPORTED_RENDERS.contains(renderer)) {
            throw new UnsupportedOperationException("Unsupported renderer: " + renderer);
        }

        if (first) {
            GL gl = new LwjglGL();
            GLExt glext = new LwjglGLExt();
            GLFbo glfbo;

            if (capabilities.OpenGL30) {
                glfbo = new LwjglGLFboGL3();
            } else {
                glfbo = new LwjglGLFboEXT();
            }

            if (settings.isGraphicsDebug()) {
                gl = (GL) GLDebug.createProxy(gl, gl, GL.class, GL2.class, GL3.class, GL4.class);
                glext = (GLExt) GLDebug.createProxy(gl, glext, GLExt.class);
                glfbo = (GLFbo) GLDebug.createProxy(gl, glfbo, GLFbo.class);
            }

            if (settings.isGraphicsTiming()) {
                GLTimingState timingState = new GLTimingState();
                gl = (GL) GLTiming.createGLTiming(gl, timingState, GL.class, GL2.class, GL3.class, GL4.class);
                glext = (GLExt) GLTiming.createGLTiming(glext, timingState, GLExt.class);
                glfbo = (GLFbo) GLTiming.createGLTiming(glfbo, timingState, GLFbo.class);
            }

            if (settings.isGraphicsTrace()) {
                gl = (GL) GLTracer.createDesktopGlTracer(gl, GL.class, GL2.class, GL3.class, GL4.class);
                glext = (GLExt) GLTracer.createDesktopGlTracer(glext, GLExt.class);
                glfbo = (GLFbo) GLTracer.createDesktopGlTracer(glfbo, GLFbo.class);
            }

            this.renderer = new GLRenderer(gl, glext, glfbo);
            if (this.settings.isGraphicsDebug()) ((GLRenderer) this.renderer).setDebugEnabled(true);
        }
        this.renderer.initialize();

        if (capabilities.GL_ARB_debug_output && settings.isGraphicsDebug()) {
            System.out.println("===============");
            System.out.println("GL_ARB_debug_output");
            System.out.println("===============");
            ARBDebugOutput.glDebugMessageCallbackARB(new LwjglGLDebugOutputHandler(), 0);
        }

        this.renderer.setMainFrameBufferSrgb(settings.isGammaCorrection());
        this.renderer.setLinearizeSrgbImages(settings.isGammaCorrection());

        if (first) {
            // Init input
            keyInput =new AwtKeyInput();
            mouseInput = new AwtMouseInput();
            keyInput.setInputSource(canvas);
            mouseInput.setInputSource(canvas);
//            keyInput.bind(getGlCanvas());
//            mouseInput.bind(getGlCanvas());
//
//            if (joyInput != null) {
//                joyInput.initialize();
//            }
        }


        systemListener.initialize();

        renderable.set(true);

    }

    private void runLoop() {

        systemListener.update();

        //交换缓冲
        canvas.swapBuffers();

        if (renderer != null) {
            renderer.postFrame();
        }
    }

    public AWTGLCanvas getGlCanvas() {
        return canvas;
    }


}
