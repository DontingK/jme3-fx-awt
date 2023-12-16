package com.example.test;

import com.huskerdev.openglfx.canvas.GLCanvas;
import com.jme3.system.JmeContext;
import com.jme3.system.lwjgl.LwjglCanvas;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GLTest2Application extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        JmeTest jmeTest = new JmeTest();
        jmeTest.start(JmeContext.Type.Canvas);
        LwjglCanvas context1 = (LwjglCanvas)jmeTest.getContext();
        GLCanvas glCanvas = context1.getGlCanvas();

//        Pane pane = new Pane();
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(glCanvas);
        AnchorPane.setRightAnchor(glCanvas,0.);
        AnchorPane.setTopAnchor(glCanvas,0.);
        AnchorPane.setLeftAnchor(glCanvas,0.);
        AnchorPane.setBottomAnchor(glCanvas,0.);

//        anchorPane.getChildren().add(glCanvas);
        Scene scene = new Scene(anchorPane, 720, 480);
        stage.setTitle("JME openglfx lwjgl3 test");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
//        System.setProperty("prism.order", "es2");
        System.setProperty("prism.vsync", "false");
        launch();
    }
}