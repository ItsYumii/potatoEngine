package objects;

import math.Matrix4x4;
import math.Vector2;
import math.Vector3;
import objects.rasterizer.MeshGL;
import objects.rasterizer.Triangle;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.glBindTexture;

public class Cube {

    private final List<Triangle> triangles = new ArrayList<>();
    private final MeshGL mesh;
    private final int texture;
    private Vector3 pos;
    public Vector3 rot = new Vector3(0, 0, 0);

    private final Matrix4x4 rotXMatrix = new Matrix4x4();
    private final Matrix4x4 rotYMatrix = new Matrix4x4();
    private final Matrix4x4 rotZMatrix = new Matrix4x4();

    private final Matrix4x4 rotMatrix = new Matrix4x4();
    private final Matrix4x4 posMatrix = new Matrix4x4();

    private final Matrix4x4 modelMatrix = new Matrix4x4();
    private final Matrix4x4 mvpMatrix   = new Matrix4x4();

    private final FloatBuffer fbModel = BufferUtils.createFloatBuffer(16);
    private final FloatBuffer fbMvp   = BufferUtils.createFloatBuffer(16);


    public Cube(Vector3 pos, Vector3 sizeFromCenter, int textureRepeat, int texture) {
        // South
        triangles.add(new Triangle(textureRepeat, new Vector3(-sizeFromCenter.x, -sizeFromCenter.y, -sizeFromCenter.z), new Vector3(-sizeFromCenter.x,  sizeFromCenter.y, -sizeFromCenter.z), new Vector3( sizeFromCenter.x,  sizeFromCenter.y, -sizeFromCenter.z), new Vector2(0, 0), new Vector2(0, 1), new Vector2(1, 1)));
        triangles.add(new Triangle(textureRepeat, new Vector3(-sizeFromCenter.x, -sizeFromCenter.y, -sizeFromCenter.z), new Vector3( sizeFromCenter.x,  sizeFromCenter.y, -sizeFromCenter.z), new Vector3( sizeFromCenter.x, -sizeFromCenter.y, -sizeFromCenter.z), new Vector2(0, 0), new Vector2(1, 1), new Vector2(1, 0)));

        // East
        triangles.add(new Triangle(textureRepeat, new Vector3( sizeFromCenter.x, -sizeFromCenter.y, -sizeFromCenter.z), new Vector3( sizeFromCenter.x,  sizeFromCenter.y, -sizeFromCenter.z), new Vector3( sizeFromCenter.x,  sizeFromCenter.y,  sizeFromCenter.z), new Vector2(0, 0), new Vector2(0, 1), new Vector2(1, 1)));
        triangles.add(new Triangle(textureRepeat, new Vector3( sizeFromCenter.x, -sizeFromCenter.y, -sizeFromCenter.z), new Vector3( sizeFromCenter.x,  sizeFromCenter.y,  sizeFromCenter.z), new Vector3( sizeFromCenter.x, -sizeFromCenter.y,  sizeFromCenter.z), new Vector2(0, 0), new Vector2(1, 1), new Vector2(1, 0)));

        // North
        triangles.add(new Triangle(textureRepeat, new Vector3( sizeFromCenter.x, -sizeFromCenter.y,  sizeFromCenter.z), new Vector3( sizeFromCenter.x,  sizeFromCenter.y,  sizeFromCenter.z), new Vector3(-sizeFromCenter.x,  sizeFromCenter.y,  sizeFromCenter.z), new Vector2(0, 0), new Vector2(0, 1), new Vector2(1, 1)));
        triangles.add(new Triangle(textureRepeat, new Vector3( sizeFromCenter.x, -sizeFromCenter.y,  sizeFromCenter.z), new Vector3(-sizeFromCenter.x,  sizeFromCenter.y,  sizeFromCenter.z), new Vector3(-sizeFromCenter.x, -sizeFromCenter.y,  sizeFromCenter.z), new Vector2(0, 0), new Vector2(1, 1), new Vector2(1, 0)));

        // West
        triangles.add(new Triangle(textureRepeat, new Vector3(-sizeFromCenter.x, -sizeFromCenter.y,  sizeFromCenter.z), new Vector3(-sizeFromCenter.x,  sizeFromCenter.y,  sizeFromCenter.z), new Vector3(-sizeFromCenter.x,  sizeFromCenter.y, -sizeFromCenter.z), new Vector2(0, 0), new Vector2(0, 1), new Vector2(1, 1)));
        triangles.add(new Triangle(textureRepeat, new Vector3(-sizeFromCenter.x, -sizeFromCenter.y,  sizeFromCenter.z), new Vector3(-sizeFromCenter.x,  sizeFromCenter.y, -sizeFromCenter.z), new Vector3(-sizeFromCenter.x, -sizeFromCenter.y, -sizeFromCenter.z), new Vector2(0, 0), new Vector2(1, 1), new Vector2(1, 0)));

        // Top
        triangles.add(new Triangle(textureRepeat, new Vector3(-sizeFromCenter.x,  sizeFromCenter.y, -sizeFromCenter.z), new Vector3(-sizeFromCenter.x,  sizeFromCenter.y,  sizeFromCenter.z), new Vector3( sizeFromCenter.x,  sizeFromCenter.y,  sizeFromCenter.z), new Vector2(0, 0), new Vector2(0, 1), new Vector2(1, 1)));
        triangles.add(new Triangle(textureRepeat, new Vector3(-sizeFromCenter.x,  sizeFromCenter.y, -sizeFromCenter.z), new Vector3( sizeFromCenter.x,  sizeFromCenter.y,  sizeFromCenter.z), new Vector3( sizeFromCenter.x,  sizeFromCenter.y, -sizeFromCenter.z), new Vector2(0, 0), new Vector2(1, 1), new Vector2(1, 0)));

        // Bottom
        triangles.add(new Triangle(textureRepeat, new Vector3( sizeFromCenter.x, -sizeFromCenter.y,  sizeFromCenter.z), new Vector3(-sizeFromCenter.x, -sizeFromCenter.y,  sizeFromCenter.z), new Vector3(-sizeFromCenter.x, -sizeFromCenter.y, -sizeFromCenter.z), new Vector2(0, 0), new Vector2(0, 1), new Vector2(1, 1)));
        triangles.add(new Triangle(textureRepeat, new Vector3( sizeFromCenter.x, -sizeFromCenter.y,  sizeFromCenter.z), new Vector3(-sizeFromCenter.x, -sizeFromCenter.y, -sizeFromCenter.z), new Vector3( sizeFromCenter.x, -sizeFromCenter.y, -sizeFromCenter.z), new Vector2(0, 0), new Vector2(1, 1), new Vector2(1, 0)));

        this.texture = texture;
        this.pos = pos;
        this.mesh = MeshGL.buildMesh(triangles);
    }


    public void draw(Matrix4x4 viewProj, int shaderProgram, int locMvp, int locModel) {

        // build model = translation * rotation (row-major, v on the right)
        this.rotXMatrix.setRotationX(this.rot.x);
        this.rotYMatrix.setRotationY(this.rot.y);
        this.rotZMatrix.setRotationZ(this.rot.z);

        this.rotMatrix.copy(this.rotYMatrix);
        this.rotMatrix.mulM(this.rotXMatrix);
        this.rotMatrix.mulM(this.rotZMatrix);

        this.posMatrix.setTranslation(this.pos);

        this.modelMatrix.copy(this.posMatrix);
        this.modelMatrix.mulM(this.rotMatrix);

        // MVP = P * V * M
        this.mvpMatrix.copy(viewProj);
        this.mvpMatrix.mulM(this.modelMatrix);

        // build model = translation * rotation (row-major, v on the right)
//        Matrix4x4 rotMat  = Matrix4x4.createFullRotation(this.rot);
//        Matrix4x4 trans   = Matrix4x4.createTranslation(this.pos);
//        Matrix4x4 model   = this.modelMatrix;

        // MVP = P * V * M
//        Matrix4x4 mvp = mvpMatrix;

        GL20.glUseProgram(shaderProgram);

        if (locMvp >= 0) {
//            FloatBuffer fbMvp = Matrix4x4.toFloatBuffer(mvp);
            this.mvpMatrix.toFloatBuffer(this.fbMvp);
            GL20.glUniformMatrix4fv(locMvp, true, this.fbMvp);
        }

        if (locModel >= 0) {
//            FloatBuffer fbModel = Matrix4x4.toFloatBuffer(model);
            this.modelMatrix.toFloatBuffer(this.fbModel);
            GL20.glUniformMatrix4fv(locModel, true, this.fbModel);
        }

        glBindTexture(GL_TEXTURE_2D, this.texture);

        // bind VAO & draw
        GL30.glBindVertexArray(mesh.vaoId);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, mesh.vertexCount);
        GL30.glBindVertexArray(0);
    }

}
