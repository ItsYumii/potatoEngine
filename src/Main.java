import debug.Console;
import math.Matrix4x4;
import math.Vector3;
import objects.Cube;
import objects.rasterizer.ShaderProgram;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL20;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11C.*;

private static double deltaTime = 0;
private static double deltaTimeAccumulator;
private static double fpsCounter = 0;
private static long window;

private static int shaderProgram;
private static int locMvp;
private static int locModel;

private static Cube cube;
private static Cube cube1;
private static Cube cube2;
private static Cube cube3;

private static List<Cube> cubes = new ArrayList<>();

void main() {
    if (!initScreen()) {
        Console.err("Unexpected error while initializing the screen.");
        return;
    }

    initShaders();

    for(int i = 0; i < 10000; i++) {
        cubes.add(
            new Cube(
                new Vector3(0, 0, -3),
                new Vector3(0.5, 0.5, 0.5),
                1,
                loadTexture("kot.png")
            )
        );
    }


    double last = GLFW.glfwGetTime();

    while (!GLFW.glfwWindowShouldClose(window)) {
        double now = GLFW.glfwGetTime();
        deltaTime = now - last;
        last = now;

        deltaTimeAccumulator += deltaTime;
        fpsCounter++;
        if(deltaTimeAccumulator > 1) {
            Console.info("FPS: %f", fpsCounter);
            fpsCounter = fpsCounter * (deltaTimeAccumulator - 1);
            deltaTimeAccumulator -= 1;
        }

        update();
        render();

        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    GLFW.glfwTerminate();
}

private static void update() {
    for(Cube c : cubes) {
        c.rot.x += 15 * deltaTime;
        c.rot.y += 15 * deltaTime;
        c.rot.z += 15 * deltaTime;
    }
}

private static void render() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    Matrix4x4 proj = Matrix4x4.createProjection(60.0, 1280.0, 720.0, 0.1, 1000.0);

    Matrix4x4 view = Matrix4x4.identity();
    Matrix4x4 viewProj = proj.mul(view);

    for(Cube c : cubes) {
        c.draw(viewProj, shaderProgram, locMvp, locModel);
    }
}

private static void initShaders() {
    // loading and compiling main fragment and vertex shader.
    shaderProgram = ShaderProgram.load("cube.vert", "cube.frag");

    // initializing default values for default shader.
    locMvp            = GL20.glGetUniformLocation(shaderProgram, "uMVP");
    locModel          = GL20.glGetUniformLocation(shaderProgram, "uModel");
    int locTexture    = GL20.glGetUniformLocation(shaderProgram, "uTexture");
    int locAmbient    = GL20.glGetUniformLocation(shaderProgram, "uAmbient");
    int locLightDir   = GL20.glGetUniformLocation(shaderProgram, "uLightDir");
    int locDiffuseMul = GL20.glGetUniformLocation(shaderProgram, "uDiffuseMul");

    // only to initialize values. it's not really used here rn.
    GL20.glUseProgram(shaderProgram);

    // texture slot OpenGL will use to save and rad textures.
    GL20.glUniform1i(locTexture, 0);

    // the direction of ambient light (from the camera)
    GL20.glUniform3f(locLightDir, 0f, 0f, -1f);

    // ambient light (how much light there naturally is)
    GL20.glUniform3f(locAmbient, 0.4f, 0.4f, 0.4f);
    GL20.glUniform1f(locDiffuseMul, 1.5f);
}

private static boolean initScreen() {
    if (!GLFW.glfwInit()) {
        return false;
    }

    window = GLFW.glfwCreateWindow(
            1280, 720,
            "potato engine",
            0, 0
    );

    if (window == 0) {
        GLFW.glfwTerminate();
        return false;
    }

    GLFW.glfwMakeContextCurrent(window);
    GLFW.glfwSwapInterval(0);
    GL.createCapabilities();

    glViewport(0, 0, 1280, 720);
    glClearColor(0.18f, 0.18f, 0.18f, 1.0f);
    glEnable(GL_DEPTH_TEST);
    glEnable(GL_CULL_FACE);  // extra paranoia

    return true;
}

private static int loadTexture(String path) {
    int texId;
    path = "src/assets/textures/" + path;

    try (MemoryStack stack = MemoryStack.stackPush()) {
        IntBuffer w = stack.mallocInt(1);
        IntBuffer h = stack.mallocInt(1);
        IntBuffer channels = stack.mallocInt(1);

        STBImage.stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = STBImage.stbi_load(path, w, h, channels, 4);
        if (image == null) {
            Console.err("Path to texture invalid (%s)", path);
            image = STBImage.stbi_load("src/assets/textures/no_texture.png", w, h, channels, 4);
        }

        texId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w.get(0), h.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

        STBImage.stbi_image_free(image);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    return texId;
}
