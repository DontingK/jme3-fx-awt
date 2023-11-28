package cn.donting.test;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

public class JmeTest extends SimpleApplication {
    private Geometry geom;

    public JmeTest() {
         settings = new AppSettings(true);
        settings.setGammaCorrection(false);

    }

    /**
     * 初始化3D场景，显示一个方块。
     */
    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.Black);
        flyCam.setDragToRotate(true);
        // #1 创建一个方块形状的网格
        Mesh box = new Box(1, 1, 1);

        // #2 加载一个感光材质
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Diffuse",ColorRGBA.Red);
        // #3 创建一个几何体，应用刚才和网格和材质。
        geom = new Geometry("Box");
        geom.setMesh(box);
        geom.setMaterial(mat);

        // #4 创建一束阳光，并让它斜向下照射，好使我们能够看清那个方块。
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1, -2, -3));
        sun.setColor(ColorRGBA.Red);
        // #5 将方块和都添加到场景图中
        rootNode.attachChild(geom);
        rootNode.addLight(sun);

    }

    /**
     * 主循环
     */
    @Override
    public void simpleUpdate(float deltaTime) {
        // 旋转速度：每秒360°
        float speed = FastMath.PI/2;
        // 让方块匀速旋转
        geom.rotate(0, deltaTime * speed, 0);
    }

    public static void main(String[] args) {

        new JmeTest().start();
    }
}
