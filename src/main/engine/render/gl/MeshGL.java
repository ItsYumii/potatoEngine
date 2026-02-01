package main.engine.render.gl;

import main.engine.math.Vector2;
import main.engine.math.Vector3;
import main.engine.render.Triangle;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import java.nio.FloatBuffer;
import java.util.List;

public class MeshGL {
    public final int vaoId;
    public final int vboId;
    public final int vertexCount;

    public MeshGL(int vaoId, int vboId, int vertexCount) {
        this.vaoId = vaoId;
        this.vboId = vboId;
        this.vertexCount = vertexCount;
    }

    public static MeshGL buildMesh(List<Triangle> tris) {

        // don't even ask me what's going on here. I have no fucking idea. fuck OpenGL - that's all i have to say.
        float[] data = new float[tris.size() * 3 * 8]; // 8 floats per vertex now

        int i = 0;
        for (Triangle t : tris) {
            Vector3 a = t.getA();
            Vector3 b = t.getB();
            Vector3 c = t.getC();

            Vector2 auv = t.getAuv();
            Vector2 buv = t.getBuv();
            Vector2 cuv = t.getCuv();

            // compute face normal: (b - a) x (c - a)
            Vector3 ab = b.sub(a);
            Vector3 ac = c.sub(a);
            Vector3 n = ab.cross(ac).normalize();

            // A
            data[i++] = (float)a.x;
            data[i++] = (float)a.y;
            data[i++] = (float)a.z;
            data[i++] = (float)(auv.x * t.getRepeat());
            data[i++] = (float)(auv.y * t.getRepeat());
            data[i++] = (float)n.x;
            data[i++] = (float)n.y;
            data[i++] = (float)n.z;

            // B
            data[i++] = (float)b.x;
            data[i++] = (float)b.y;
            data[i++] = (float)b.z;
            data[i++] = (float)(buv.x * t.getRepeat());
            data[i++] = (float)(buv.y * t.getRepeat());
            data[i++] = (float)n.x;
            data[i++] = (float)n.y;
            data[i++] = (float)n.z;

            // C
            data[i++] = (float)c.x;
            data[i++] = (float)c.y;
            data[i++] = (float)c.z;
            data[i++] = (float)(cuv.x * t.getRepeat());
            data[i++] = (float)(cuv.y * t.getRepeat());
            data[i++] = (float)n.x;
            data[i++] = (float)n.y;
            data[i++] = (float)n.z;
        }

        int vao = GL30.glGenVertexArrays();
        int vbo = GL15.glGenBuffers();

        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data).flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

        int stride = 8 * Float.BYTES;

        // layout(location = 0) vec3 inPos;
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, stride, 0L);
        GL20.glEnableVertexAttribArray(0);

        // layout(location = 1) vec2 inUV;
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, stride, 3L * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        // layout(location = 2) vec3 inNormal;
        GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, stride, 5L * Float.BYTES);
        GL20.glEnableVertexAttribArray(2);


        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        int vertexCount = tris.size() * 3;

        return new MeshGL(vao, vbo, vertexCount);
    }
}
