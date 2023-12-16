package com.example.test;

import com.huskerdev.openglfx.canvas.GLCanvas;
import com.huskerdev.openglfx.canvas.GLCanvasAnimator;
import com.huskerdev.openglfx.canvas.GLProfile;
import com.huskerdev.openglfx.canvas.events.GLInitializeEvent;
import com.huskerdev.openglfx.canvas.events.GLRenderEvent;
import com.huskerdev.openglfx.lwjgl2.LWJGL2Executor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.*;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        GLCanvas glCanvas =new GLCanvas(LWJGL2Executor.LWJGL2_MODULE, GLProfile.Compatibility, true);
        glCanvas.setAnimator(new GLCanvasAnimator());
        Scene scene = new Scene(glCanvas, 320, 240);
        glCanvas.setAnimator(new GLCanvasAnimator(144));
        glCanvas.addOnInitEvent(new Consumer<GLInitializeEvent>() {
            @Override
            public void accept(GLInitializeEvent glInitializeEvent) {
                System.out.println("init");
            }
        });
        glCanvas.addOnReshapeEvent(glReshapeEvent -> {
            System.out.println(glReshapeEvent);
        });
        glCanvas.addOnRenderEvent(new Consumer<GLRenderEvent>() {
            long l=System.currentTimeMillis();

            @Override
            public void accept(GLRenderEvent event) {
                long l1 = System.currentTimeMillis();
                if (l1-l>1000) {
                    System.out.println("fps:"+event.fps);
                    System.out.println("w:"+glCanvas.getWidth());
                    l=l1;
                }
                int w = event.width;
                int h = event.height;
                float aspect = (float) w / h;
                double now = System.currentTimeMillis() * 0.001;
                float width = (float) Math.abs(Math.sin(now * 0.3));
                glClear(GL_COLOR_BUFFER_BIT);
                glViewport(0, 0, w, h);
                glBegin(GL_QUADS);
                glColor3f(0.4f, 0.6f, 0.8f);
                glVertex2f(-0.75f * width / aspect, 0.0f);
                glVertex2f(0, -0.75f);
                glVertex2f(+0.75f * width/ aspect, 0);
                glVertex2f(0, +0.75f);
                glEnd();
            }
        });
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
//        System.setProperty("prism.order", "es2");
        System.setProperty("prism.vsync", "false");
        launch();
    }
}