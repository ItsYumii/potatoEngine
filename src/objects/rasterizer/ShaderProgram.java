package objects.rasterizer;

import debug.Console;
import org.lwjgl.opengl.GL20;
import java.nio.file.Files;
import java.nio.file.Path;

public class ShaderProgram {

    public static int load(String vertexPath, String fragmentPath) {
        vertexPath   = "src/assets/shaders/" + vertexPath;
        fragmentPath = "src/assets/shaders/" + fragmentPath;

        String vertSrc;
        String fragSrc;

        try {
            // read shader files.
            vertSrc = Files.readString(Path.of(vertexPath));
            fragSrc = Files.readString(Path.of(fragmentPath));
        } catch (Exception e) {
            // shader file not found.
            Console.err("Shader files not found");
            throw new RuntimeException(e);
        }

        // compile both shaders.
        int vertId = compile(GL20.GL_VERTEX_SHADER, vertSrc);
        int fragId = compile(GL20.GL_FRAGMENT_SHADER, fragSrc);

        // create shader program
        int programId = GL20.glCreateProgram();

        // attach compiled shaders id's
        GL20.glAttachShader(programId, vertId);
        GL20.glAttachShader(programId, fragId);

        // link everything together.
        GL20.glLinkProgram(programId);

        // check linking.
        if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == 0) {
            Console.err("Shader linking failed:\n%s", GL20.glGetProgramInfoLog(programId));
            throw new RuntimeException("");
        }

        // shaders can be deleted after linking.
        GL20.glDeleteShader(vertId);
        GL20.glDeleteShader(fragId);

        return programId;
    }

    private static int compile(int type, String source) {
        // compile shaders.
        int id = GL20.glCreateShader(type);
        GL20.glShaderSource(id, source);
        GL20.glCompileShader(id);

        // check for errors.
        if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == 0) {
            Console.err("Shader compilation failed:\n%s", GL20.glGetShaderInfoLog(id));
            throw new RuntimeException("");
        }

        // return compiled shader's id.
        return id;
    }
}

