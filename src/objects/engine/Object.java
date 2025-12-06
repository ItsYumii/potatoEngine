package objects.engine;

import math.Matrix4x4;
import math.Vector3;
import objects.rasterizer.MeshGL;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.glBindTexture;

public class Object {

    protected final Vector3 pos, rot;

    private final static List<Object> objects = new ArrayList<>();
    private final MeshGL mesh;
    private final int textureID;

    private final Matrix4x4
        rotXMatrix  = new Matrix4x4(),
        rotYMatrix  = new Matrix4x4(),
        rotZMatrix  = new Matrix4x4(),

        rotMatrix   = new Matrix4x4(),
        posMatrix   = new Matrix4x4(),

        modelMatrix = new Matrix4x4(),
        mvpMatrix   = new Matrix4x4();

    private final FloatBuffer
        fbModel = BufferUtils.createFloatBuffer(16),
        fbMvp   = BufferUtils.createFloatBuffer(16);


    public Object(Vector3 pos, Vector3 rot, int textureID, String modelPath) {
        this.textureID = textureID;
        this.pos = pos;
        this.rot = rot;

        Model model = new Model(modelPath);
        this.mesh = MeshGL.buildMesh(model.getTriangles());

        objects.add(this);
    }

    protected void draw(Matrix4x4 viewProj, int shaderProgram, int locMvp, int locModel) {

        // build model = translation * rotation (row-major, v on the right)
        this.rotXMatrix.setRotationX(this.rot.x);
        this.rotYMatrix.setRotationY(this.rot.y);
        this.rotZMatrix.setRotationZ(this.rot.z);

        this.rotMatrix
                .copy(this.rotYMatrix)
                .mulM(this.rotXMatrix)
                .mulM(this.rotZMatrix);

        this.posMatrix.setTranslation(this.pos.x, this.pos.y, this.pos.z);

        this.modelMatrix
                .copy(this.posMatrix)
                .mulM(this.rotMatrix);

        // MVP = P * V * M
        this.mvpMatrix
                .copy(viewProj)
                .mulM(this.modelMatrix);

        GL20.glUseProgram(shaderProgram);

        if (locMvp >= 0) {
            this.mvpMatrix.toFloatBuffer(this.fbMvp);
            GL20.glUniformMatrix4fv(locMvp, true, this.fbMvp);
        }

        if (locModel >= 0) {
            this.modelMatrix.toFloatBuffer(this.fbModel);
            GL20.glUniformMatrix4fv(locModel, true, this.fbModel);
        }

        glBindTexture(GL_TEXTURE_2D, this.textureID);

        // bind VAO & draw
        GL30.glBindVertexArray(mesh.vaoId);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, mesh.vertexCount);
        GL30.glBindVertexArray(0);
    }

    public static void drawAllObjects(Matrix4x4 viewProj, int shaderProgram, int locMvp, int locModel) {
        for(Object obj : objects)
            obj.draw(viewProj, shaderProgram, locMvp, locModel);
    }
}
