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

private static double deltaTime = 0;
private static long window;

// These live HERE, inside Game
private static int shaderProgram;
private static int locMvp;
private static int locTexture;

private static Cube cube;

void main() {
    if (!initScreen()) {
        debug.Console.err("Unexpected error while initializing the screen.");
        return;
    }

    // --- SHADERS ---
    shaderProgram = ShaderProgram.load("cube.vert", "cube.frag");
    locMvp    = GL20.glGetUniformLocation(shaderProgram, "uMVP");
    locTexture = GL20.glGetUniformLocation(shaderProgram, "uTexture");

    GL20.glUseProgram(shaderProgram);
    GL20.glUniform1i(locTexture, 0); // sampler uses texture unit 0

    // --- TEXTURE --- (loaded here, but passed into Cube)
    int textureId = loadTexture("kot.png");

    // --- CUBE ---
    // put cube 3 units IN FRONT of the camera along -Z
    cube = new Cube(
            new Vector3(0, 0, -3),          // 🔹 was (0,0,0), move to -3 in Z
            new Vector3(0.5, 0.5, 0.5),
            1,
            textureId   // or 0 if you're still on magenta shader
    );

    double last = GLFW.glfwGetTime();

    while (!GLFW.glfwWindowShouldClose(window)) {
        double now = GLFW.glfwGetTime();
        deltaTime = now - last;
        last = now;

        update();
        render();

        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    GLFW.glfwTerminate();
}

private static void update() {
    cube.rot.x += 15 * deltaTime;
    cube.rot.y += 15 * deltaTime;
    cube.rot.z += 15 * deltaTime;
}

private void render() {
    GL11C.glClear(GL11C.GL_COLOR_BUFFER_BIT | GL11C.GL_DEPTH_BUFFER_BIT);

    // perspective projection
    Matrix4x4 proj = Matrix4x4.createProjection(
            60.0,          // FOV in degrees (60 is fine)
            1280.0,
            720.0,
            0.1,
            100.0
    );

    // camera at origin looking -Z for now
    Matrix4x4 view = Matrix4x4.identity();

    // P * V
    Matrix4x4 viewProj = proj.mul(view);

    cube.draw(viewProj, shaderProgram, locMvp);
}




private static boolean initScreen() {
    if (!GLFW.glfwInit()) return false;

    window = GLFW.glfwCreateWindow(
            1280, 720,
            "potato engine v0.0000000000000000000001 lul",
            0, 0
    );

    if (window == 0) {
        GLFW.glfwTerminate();
        return false;
    }

    GLFW.glfwMakeContextCurrent(window);
    GL.createCapabilities();

    GL11C.glViewport(0, 0, 1280, 720);
    GL11C.glClearColor(0.18f, 0.18f, 0.18f, 1.0f);
    GL11C.glEnable(GL11C.GL_DEPTH_TEST);
    GL11C.glEnable(GL11C.GL_CULL_FACE);  // extra paranoia

    return true;
}

// same loadTexture() I showed before, lives HERE
private static int loadTexture(String path) {
    int texId;

    try (MemoryStack stack = MemoryStack.stackPush()) {
        IntBuffer w = stack.mallocInt(1);
        IntBuffer h = stack.mallocInt(1);
        IntBuffer channels = stack.mallocInt(1);

        STBImage.stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = STBImage.stbi_load(path, w, h, channels, 4);
        if (image == null) {
            throw new RuntimeException("Failed to load texture: " + path +
                    "\n" + STBImage.stbi_failure_reason());
        }

        texId = GL11C.glGenTextures();
        GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, texId);

        GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MIN_FILTER, GL11C.GL_NEAREST);
        GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MAG_FILTER, GL11C.GL_NEAREST);

        GL11C.glTexImage2D(
                GL11C.GL_TEXTURE_2D,
                0,
                GL11C.GL_RGBA8,
                w.get(0),
                h.get(0),
                0,
                GL11C.GL_RGBA,
                GL11C.GL_UNSIGNED_BYTE,
                image
        );

        STBImage.stbi_image_free(image);
    }

    return texId;
}