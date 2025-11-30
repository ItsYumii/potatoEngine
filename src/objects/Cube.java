package objects;

import math.Matrix4x4;
import math.Vector2;
import math.Vector3;
import objects.rasterizer.MeshGL;
import objects.rasterizer.Triangle;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Cube {

    private final List<Triangle> triangles = new ArrayList<>();
    private Vector3 pos;
    private int texture;
    private MeshGL mesh;
    public Vector3 rot = new Vector3(0, 0, 0);

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

    public void draw(Matrix4x4 viewProj, int shaderProgram, int locMvp) {
        Matrix4x4 rotY  = Matrix4x4.createFullRotation(this.rot);
        Matrix4x4 trans = Matrix4x4.createTranslation(this.pos);

// model = rotation * translation (row-major, v on right)
        Matrix4x4 model = trans.mul(rotY);   // or rotY.mul(trans) depending what you want

        Matrix4x4 mvp = viewProj.mul(model);


        GL20.glUseProgram(shaderProgram);

        if (locMvp >= 0) {
            java.nio.FloatBuffer fb = Matrix4x4.toFloatBuffer(mvp);
            // we’re using row-major on CPU, so tell GL to transpose
            glUniformMatrix4fv(locMvp, true, fb);
        }

        // still magenta, so no texture binding yet
        GL30.glBindVertexArray(mesh.vaoId);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, mesh.vertexCount);
        GL30.glBindVertexArray(0);
    }



}
