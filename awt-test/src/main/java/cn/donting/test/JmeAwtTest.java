package cn.donting.test;

import com.jme3.system.JmeContext;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

import javax.swing.*;
import java.awt.*;

public class JmeAwtTest {
    public static void main(String[] args) {
        JFrame frame = new JFrame("AWT test");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.pack();
        frame.setSize(200,200);
        GLData data = new GLData();
        data.majorVersion = 3;
        data.minorVersion = 3;
        data.profile = GLData.Profile.CORE;
        data.samples = 4;
        JmeTest jmeTest = new JmeTest();
        jmeTest.start(JmeContext.Type.Canvas);
        com.jme3.system.lwjgl.LwjglCanvas context1 = (com.jme3.system.lwjgl.LwjglCanvas)jmeTest.getContext();
        AWTGLCanvas glCanvas = context1.getGlCanvas();
        frame.add(glCanvas);
        frame.setVisible(true);
        glCanvas.setSize(200,200);
//        Runnable renderLoop = new Runnable() {
//            @Override
//            public void run() {
//                if (!glCanvas.isValid()) {
//                    GL.setCapabilities(null);
//                    return;
//                }
//                glCanvas.render();
//                SwingUtilities.invokeLater(this);
//            }
//        };
//        SwingUtilities.invokeLater(renderLoop);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true){
//                    glCanvas.render();
//                }
//            }
//        }).start();
    }
}
