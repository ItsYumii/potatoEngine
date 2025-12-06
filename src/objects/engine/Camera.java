package objects.engine;

import math.Matrix4x4;
import math.Vector2;
import math.Vector3;
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

    public Matrix4x4 getViewMatrix() {
        this.rotXMatrix.setRotationX(-this.rot.x);
        this.rotYMatrix.setRotationY(-this.rot.y);
        this.rotZMatrix.setRotationZ(-this.rot.z);

        this.rotMatrix.copy(this.rotZMatrix);
        this.rotMatrix.mulM(this.rotXMatrix);
        this.rotMatrix.mulM(this.rotYMatrix);

        Vector3 negPos = new Vector3(-this.pos.x, -this.pos.y, -this.pos.z);
        this.posMatrix.setTranslation(negPos);

        this.modelMatrix.copy(this.rotMatrix);
        this.modelMatrix.mulM(this.posMatrix);

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
