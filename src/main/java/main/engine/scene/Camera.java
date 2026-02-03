package main.java.main.engine.scene;

import main.java.main.engine.math.Matrix4x4;
import main.java.main.engine.math.Vector2;
import main.java.main.engine.math.Vector3;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class Camera {

    private final Vector3
        rot = new Vector3(0, 0, 0),
        pos = new Vector3(0, 0, 0);

    private final Matrix4x4
        rotXMatrix = new Matrix4x4(),
        rotYMatrix = new Matrix4x4(),
        rotZMatrix = new Matrix4x4(),

        rotMatrix = new Matrix4x4(),
        posMatrix = new Matrix4x4(),

        modelMatrix = new Matrix4x4();

    private final FloatBuffer fbModel = BufferUtils.createFloatBuffer(16);

    private Object parent = null;

    public Camera() {}

    public Camera(Object parent) {
        this.parent = parent;
    }

    private void goToParent() {
        if(this.parent != null)
            this.pos.copy(this.parent.pos);
    }
    public Matrix4x4 getViewMatrix() {
        this.rotXMatrix.setRotationX(-this.rot.x);
        this.rotYMatrix.setRotationY(-this.rot.y);
        this.rotZMatrix.setRotationZ(-this.rot.z);

        this.rotMatrix
            .copy(this.rotZMatrix)
            .mulM(this.rotXMatrix)
            .mulM(this.rotYMatrix);

        goToParent();
        this.posMatrix.setTranslation(-this.pos.x, -this.pos.y, -this.pos.z);

        this.modelMatrix
            .copy(this.rotMatrix)
            .mulM(this.posMatrix);

        return this.modelMatrix;
    }


    public FloatBuffer getViewFloatBuffer() {
        this.modelMatrix.toFloatBuffer(this.fbModel);
        return this.fbModel;
    }

    public void addCameraRotation(Vector2 shift) {
        this.rot.addM(shift);
    }

    public void addCameraRotation(Vector3 shift) {
        this.rot.addM(shift);
    }

    public void setCameraRotation(Vector2 shift) {
        this.rot.addM(shift);
    }

    public void setCameraRotation(Vector3 shift) {
        this.rot.copy(shift);
    }

    public void setCameraPosition(Vector3 shift) {
        this.pos.copy(shift);
    }

    public Vector3 getRotation() {
        return this.rot;
    }

    public Vector3 getPosition() {
        return this.pos;
    }
}
