package com.example.test.input;


import com.huskerdev.openglfx.canvas.GLCanvas;
import com.jme3.app.Application;
import com.jme3.input.Input;
import com.jme3.input.RawInputListener;
import com.jme3.system.SystemListener;
import javafx.scene.Node;
import javafx.scene.Scene;

/**
 * The base implementation of the {@link Input} for using in the ImageView.
 *
 * @author JavaSaBr
 */
public class JfxInput implements Input {

    // protected static final ApplicationThreadExecutor EXECUTOR = ApplicationThreadExecutor.getInstance();

    /**
     * The raw listener.
     */
    protected RawInputListener listener;

    /**
     * The input node.
     */
    protected Node node;

    /**
     * The scene.
     */
    protected Scene scene;

    /**
     * The flag of initializing this.
     */
    protected boolean initialized;
    protected final Application application;

    public JfxInput(Application systemListener) {
        application = (Application) systemListener;
    }

    /**
     * Checks of existing the node.
     *
     * @return true if the node is exist.
     */
    protected boolean hasNode() {
        return node != null;
    }

    /**
     * Gets the bound node.
     *
     * @return the bound node.
     */
    protected Node getNode() {
        return node;
    }

    /**
     * Gets the raw listener.
     *
     * @return the raw listener.
     */
    protected RawInputListener getListener() {
        return listener;
    }

    /**
     * Bind this input to the node.
     *
     * @param node the node.
     */
    public void bind(Node node) {
        this.node = node;
        this.scene = node.getScene();
    }


    @Override
    public void initialize() {
        if (isInitialized()) return;
        initializeImpl();
        initialized = true;
    }

    /**
     * Initialize.
     */
    protected void initializeImpl() {
    }

    @Override
    public void update() {
        updateImpl();
    }

    /**
     * Update.
     */
    protected void updateImpl() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setInputListener(RawInputListener listener) {
        this.listener = listener;
    }

    @Override
    public long getInputTimeNanos() {
        return System.nanoTime();
    }
}
