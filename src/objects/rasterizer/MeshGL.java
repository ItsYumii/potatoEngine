package objects.rasterizer;

import math.Vector2;
import math.Vector3;
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
        float[] data = new float[tris.size() * 3 * 5];

        int i = 0;
        for (Triangle t : tris) {
            // vertex A
            Vector3 a = t.getA();
            Vector2 auv = t.getAuv();
            data[i++] = (float) a.x;
            data[i++] = (float) a.y;
            data[i++] = (float) a.z;
            data[i++] = (float) (auv.x * t.getRepeat());
            data[i++] = (float) (auv.y * t.getRepeat());

            // vertex B
            Vector3 b = t.getB();
            Vector2 buv = t.getBuv();
            data[i++] = (float) b.x;
            data[i++] = (float) b.y;
            data[i++] = (float) b.z;
            data[i++] = (float) (buv.x * t.getRepeat());
            data[i++] = (float) (buv.y * t.getRepeat());

            // vertex C
            Vector3 c = t.getC();
            Vector2 cuv = t.getCuv();
            data[i++] = (float) c.x;
            data[i++] = (float) c.y;
            data[i++] = (float) c.z;
            data[i++] = (float) (cuv.x * t.getRepeat());
            data[i++] = (float) (cuv.y * t.getRepeat());
        }

        int vao = GL30.glGenVertexArrays();
        int vbo = GL15.glGenBuffers();

        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data).flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

        int stride = 5 * Float.BYTES;

        // WHAT THE FUCK IS LAYOUT 0 AND 1

        // layout(location = 0) vec3 inPos;
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, stride, 0L);
        GL20.glEnableVertexAttribArray(0);

        // layout(location = 1) vec2 inUV;
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, stride, 3L * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        int vertexCount = tris.size() * 3;

        return new MeshGL(vao, vbo, vertexCount);
    }
}
