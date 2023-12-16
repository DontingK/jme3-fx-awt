//package com.jme3.system.lwjgl;
//
//import com.example.test.input.JfxKeyInput;
//import com.example.test.input.JfxMouseInput;
//import com.huskerdev.openglfx.canvas.GLCanvas;
//import com.huskerdev.openglfx.canvas.GLCanvasAnimator;
//import com.huskerdev.openglfx.canvas.GLProfile;
//import com.huskerdev.openglfx.lwjgl2.LWJGL2Executor;
//import com.jme3.app.Application;
//import com.jme3.input.JoyInput;
//import com.jme3.input.KeyInput;
//import com.jme3.input.MouseInput;
//import com.jme3.input.TouchInput;
//import com.jme3.opencl.Context;
//import com.jme3.renderer.Renderer;
//import com.jme3.renderer.RendererException;
//import com.jme3.renderer.lwjgl.LwjglGL;
//import com.jme3.renderer.lwjgl.LwjglGLExt;
//import com.jme3.renderer.lwjgl.LwjglGLFboEXT;
//import com.jme3.renderer.lwjgl.LwjglGLFboGL3;
//import com.jme3.renderer.opengl.*;
//import com.jme3.system.AppSettings;
//import com.jme3.system.JmeContext;
//import com.jme3.system.SystemListener;
//import com.jme3.system.Timer;
//import javafx.scene.Scene;
//import javafx.scene.input.KeyEvent;
//import org.lwjgl.opengl.ARBDebugOutput;
//import org.lwjgl.opengl.ARBDebugOutputCallback;
//import org.lwjgl.opengl.GLContext;
//
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//public class LwjglCanvasv3 implements JmeContext {
//    private static final Set<String> SUPPORTED_RENDERS = new HashSet<>(Arrays.asList(
//            AppSettings.LWJGL_OPENGL2,
//            AppSettings.LWJGL_OPENGL30,
//            AppSettings.LWJGL_OPENGL31,
//            AppSettings.LWJGL_OPENGL32,
//            AppSettings.LWJGL_OPENGL33,
//            AppSettings.LWJGL_OPENGL40,
//            AppSettings.LWJGL_OPENGL41,
//            AppSettings.LWJGL_OPENGL42,
//            AppSettings.LWJGL_OPENGL43,
//            AppSettings.LWJGL_OPENGL44,
//            AppSettings.LWJGL_OPENGL45
//    ));
//    private GLCanvas canvas;
//    private AppSettings settings;
//    private SystemListener systemListener;
//    private Renderer renderer;
//    protected final Object createdLock = new Object();
//    protected final AtomicBoolean created = new AtomicBoolean(false);
//    protected final AtomicBoolean renderable = new AtomicBoolean(false);
//
//    protected Timer timer;
//    protected LwjglContext clContext;
//
//    private JfxKeyInput keyInput;
//    private JfxMouseInput mouseInput;
//
//    public LwjglCanvasv3() {
//
//    }
//
//    @Override
//    public Type getType() {
//        return Type.Canvas;
//    }
//
//    @Override
//    public void setSettings(AppSettings settings) {
//
//        this.settings = settings;
//        int samples = settings.getSamples();
//        canvas =new GLCanvas(LWJGL2Executor.LWJGL2_MODULE, GLProfile.Compatibility,false,samples,false);
//        canvas.setAnimator(new GLCanvasAnimator(144));
//        canvas.addOnInitEvent(glInitializeEvent -> {
//            initContent(true);
//            Scene scene = canvas.getScene();
//            scene.addEventHandler(KeyEvent.KEY_PRESSED,canvas.getOnKeyPressed());
//            scene.addEventHandler(KeyEvent.KEY_RELEASED,canvas.getOnKeyReleased());
//        });
//        canvas.addOnReshapeEvent(glReshapeEvent -> {
//            System.out.println(Thread.currentThread());
//            System.out.println("Reshape:"+glReshapeEvent.height+","+glReshapeEvent.width);
//            if (systemListener!=null) {
//                systemListener.reshape(glReshapeEvent.width,glReshapeEvent.height);
//            }
//        });
//        canvas.addOnRenderEvent(glRenderEvent -> {
//            runLoop();
//        });
//    }
//
//    @Override
//    public SystemListener getSystemListener() {
//        return systemListener;
//    }
//
//    @Override
//    public void setSystemListener(SystemListener listener) {
//        this.systemListener = listener;
//    }
//
//    @Override
//    public AppSettings getSettings() {
//        return settings;
//    }
//
//    @Override
//    public Renderer getRenderer() {
//        return renderer;
//    }
//
//    @Override
//    public Context getOpenCLContext() {
//        return null;
//    }
//
//    @Override
//    public MouseInput getMouseInput() {
//        return mouseInput;
//    }
//
//    @Override
//    public KeyInput getKeyInput() {
//        return keyInput;
//    }
//
//    @Override
//    public JoyInput getJoyInput() {
//        return null;
//    }
//
//    @Override
//    public TouchInput getTouchInput() {
//        return null;
//    }
//
//    @Override
//    public Timer getTimer() {
//        return timer;
//    }
//
//    @Override
//    public void setTitle(String title) {
//
//    }
//
//    @Override
//    public boolean isCreated() {
//        return created.get();
//    }
//
//    @Override
//    public boolean isRenderable() {
//        return renderable.get();
//    }
//
//    @Override
//    public void setAutoFlushFrames(boolean enabled) {
//
//    }
//
//    @Override
//    public void create(boolean waitFor) {
//    }
//
//
//    @Override
//    public void restart() {
//
//    }
//
//    @Override
//    public void destroy(boolean waitFor) {
//
//    }
//
//    @Override
//    public int getFramebufferHeight() {
//        return 0;
//    }
//
//    @Override
//    public int getFramebufferWidth() {
//        return 0;
//    }
//
//    @Override
//    public int getWindowXPosition() {
//        return 0;
//    }
//
//    @Override
//    public int getWindowYPosition() {
//        return 0;
//    }
//
//    protected int[] getGLVersion(String renderer) {
//        int maj = -1, min = -1;
//        switch (settings.getRenderer()) {
//            case AppSettings.LWJGL_OPENGL2:
//                maj = 2;
//                min = 0;
//                break;
//            case AppSettings.LWJGL_OPENGL30:
//                maj = 3;
//                min = 0;
//                break;
//            case AppSettings.LWJGL_OPENGL31:
//                maj = 3;
//                min = 1;
//                break;
//            case AppSettings.LWJGL_OPENGL32:
//                maj = 3;
//                min = 2;
//                break;
//            case AppSettings.LWJGL_OPENGL33:
//                maj = 3;
//                min = 3;
//                break;
//            case AppSettings.LWJGL_OPENGL40:
//                maj = 4;
//                min = 0;
//                break;
//            case AppSettings.LWJGL_OPENGL41:
//                maj = 4;
//                min = 1;
//                break;
//            case AppSettings.LWJGL_OPENGL42:
//                maj = 4;
//                min = 2;
//                break;
//            case AppSettings.LWJGL_OPENGL43:
//                maj = 4;
//                min = 3;
//                break;
//            case AppSettings.LWJGL_OPENGL44:
//                maj = 4;
//                min = 4;
//                break;
//            case AppSettings.LWJGL_OPENGL45:
//                maj = 4;
//                min = 5;
//                break;
//        }
//        return maj == -1 ? null : new int[] { maj, min };
//    }
//
//    private void initContent(boolean first) {
//        if (!GLContext.getCapabilities().OpenGL20) {
//            throw new RendererException("OpenGL 2.0 or higher is "
//                    + "required for jMonkeyEngine");
//        }
//
//        int version[] = getGLVersion(settings.getRenderer());
//        if (version != null) {
//            if (first) {
//                GL gl = new LwjglGL();
//                GLExt glext = new LwjglGLExt();
//                GLFbo glfbo;
//
//                if (GLContext.getCapabilities().OpenGL30) {
//                    glfbo = new LwjglGLFboGL3();
//                } else {
//                    glfbo = new LwjglGLFboEXT();
//                }
//
//                if (settings.isGraphicsDebug()) {
//                    gl = (GL) GLDebug.createProxy(gl, gl, GL.class, GL2.class, GL3.class, GL4.class);
//                    glext = (GLExt) GLDebug.createProxy(gl, glext, GLExt.class);
//                    glfbo = (GLFbo) GLDebug.createProxy(gl, glfbo, GLFbo.class);
//                }
//                if (settings.isGraphicsTiming()) {
//                    GLTimingState timingState = new GLTimingState();
//                    gl = (GL) GLTiming.createGLTiming(gl, timingState, GL.class, GL2.class, GL3.class, GL4.class);
//                    glext = (GLExt) GLTiming.createGLTiming(glext, timingState, GLExt.class);
//                    glfbo = (GLFbo) GLTiming.createGLTiming(glfbo, timingState, GLFbo.class);
//                }
//                if (settings.isGraphicsTrace()) {
//                    gl = (GL) GLTracer.createDesktopGlTracer(gl, GL.class, GL2.class, GL3.class, GL4.class);
//                    glext = (GLExt) GLTracer.createDesktopGlTracer(glext, GLExt.class);
//                    glfbo = (GLFbo) GLTracer.createDesktopGlTracer(glfbo, GLFbo.class);
//                }
//                renderer = new GLRenderer(gl, glext, glfbo);
//            }
//            renderer.initialize();
//        } else {
//            throw new UnsupportedOperationException("Unsupported renderer: " + settings.getRenderer());
//        }
//        if (GLContext.getCapabilities().GL_ARB_debug_output && settings.isGraphicsDebug()) {
//            ARBDebugOutput.glDebugMessageCallbackARB(new ARBDebugOutputCallback(new LwjglGLDebugOutputHandler()));
//        }
//
//        this.renderer.setMainFrameBufferSrgb(settings.isGammaCorrection());
//        this.renderer.setLinearizeSrgbImages(settings.isGammaCorrection());
//
//        if (first) {
//            // Init input
//            keyInput = new JfxKeyInput((Application)systemListener);
//            mouseInput = new JfxMouseInput((Application)systemListener);
//            keyInput.bind(getGlCanvas());
//            mouseInput.bind(getGlCanvas());
////
////            if (joyInput != null) {
////                joyInput.initialize();
////            }
//        }
//
//
//        systemListener.initialize();
//
//        renderable.set(true);
//
//    }
//
//    private void runLoop() {
//        systemListener.update();
//        // Subclasses just call GLObjectManager. Clean up objects here.
//        // It is safe ... for now.
//        if (renderer != null) {
//            renderer.postFrame();
//        }
//    }
//
//    public GLCanvas getGlCanvas() {
//        return canvas;
//    }
//}
