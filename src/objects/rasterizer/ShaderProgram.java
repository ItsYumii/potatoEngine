package objects.rasterizer;

import org.lwjgl.opengl.GL20;
import java.nio.file.Files;
import java.nio.file.Path;

public class ShaderProgram {

    public static int load(String vertexPath, String fragmentPath) {
        String vertSrc;
        String fragSrc;

        try {
            vertSrc = Files.readString(Path.of(vertexPath));
            fragSrc = Files.readString(Path.of(fragmentPath));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load shader files", e);
        }

        int vertId = compile(GL20.GL_VERTEX_SHADER, vertSrc);
        int fragId = compile(GL20.GL_FRAGMENT_SHADER, fragSrc);

        int programId = GL20.glCreateProgram();

        GL20.glAttachShader(programId, vertId);
        GL20.glAttachShader(programId, fragId);

        GL20.glLinkProgram(programId);

        // check linking
        if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Shader linking failed:\n" + GL20.glGetProgramInfoLog(programId));
        }

        // shaders can be deleted after linking
        GL20.glDeleteShader(vertId);
        GL20.glDeleteShader(fragId);

        return programId;
    }

    private static int compile(int type, String source) {
        int id = GL20.glCreateShader(type);
        GL20.glShaderSource(id, source);
        GL20.glCompileShader(id);

        // check errors
        if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException(
                    "Shader compilation failed:\n" + GL20.glGetShaderInfoLog(id)
            );
        }

        return id;
    }
}

