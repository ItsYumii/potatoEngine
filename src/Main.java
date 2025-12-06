import debug.Console;
import engine.Input;
import engine.InputController;
import engine.Settings;
import math.Matrix4x4;
import math.Vector2;
import math.Vector3;
import objects.Cube;
import objects.engine.Camera;
import objects.engine.Object;
import objects.rasterizer.ShaderProgram;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;

import static objects.rasterizer.Texture.loadTexture;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

private static double deltaTime = 0;
private static double deltaTimeAccumulator;
private static double fpsCounter = 0;
private static long window;

private static int shaderProgram;
private static int locMvp;
private static int locCamera;
private static int locModel;

//private static final List<Cube> cubes = new ArrayList<>();

public static final Camera camera = new Camera();
private static final Vector3 cameraPos = new Vector3(0, 0, 0);

void main() {
    if (!initScreen()) {
        Console.err("Unexpected error while initializing the screen.");
        return;
    }

    initShaders();

//    for(int i = 0; i < 1; i++) {
//        cubes.add(
//            new Cube(
//                new Vector3(0, 0, -3),
//                new Vector3(0.5, 0.5, 0.5),
//                1,
//                loadTexture("kot.png").getID()
//            )
//        );
//    }

    new Object(new Vector3(0, 0, -3), new Vector3(0, 0, 180), loadTexture("kot").getID(), "pea_shooter");

    double last = GLFW.glfwGetTime();

    while (!GLFW.glfwWindowShouldClose(window)) {
        double now = GLFW.glfwGetTime();
        deltaTime = now - last;
        last = now;

        deltaTimeAccumulator += deltaTime;
        fpsCounter++;
        while(deltaTimeAccumulator > 1) {
            Console.info("FPS: %f", fpsCounter);
            fpsCounter = fpsCounter * (deltaTimeAccumulator - 1);
            deltaTimeAccumulator -= 1;
        }

//        System.out.println(mouseMove);

        update();
        render();

        GLFW.glfwSwapBuffers(window);
        InputController.setMouseMove(0, 0);
        GLFW.glfwPollEvents();
    }

    GLFW.glfwTerminate();
}

private static void update() {
    // for debug and util a real player isn't made:
    double angle = camera.getRotation().y * Math.PI / 180.0;

    if(Input.isKeyDown(GLFW_KEY_W)) {
        cameraPos.x -= Math.sin(angle) * 5 * deltaTime;
        cameraPos.z -= Math.cos(angle) * 5 * deltaTime;
    }
    if(Input.isKeyDown(GLFW_KEY_S)) {
        cameraPos.x += Math.sin(angle) * 5 * deltaTime;
        cameraPos.z += Math.cos(angle) * 5 * deltaTime;
    }
    if(Input.isKeyDown(GLFW_KEY_A)) {
        cameraPos.x -= Math.cos(angle) * 5 * deltaTime;
        cameraPos.z += Math.sin(angle) * 5 * deltaTime;
    }
    if(Input.isKeyDown(GLFW_KEY_D)) {
        cameraPos.x += Math.cos(angle) * 5 * deltaTime;
        cameraPos.z -= Math.sin(angle) * 5 * deltaTime;
    }
    if(Input.isKeyDown(GLFW_KEY_SPACE)) cameraPos.y += 5 * deltaTime;
    if(Input.isKeyDown(GLFW_KEY_LEFT_SHIFT)) cameraPos.y -= 5 * deltaTime;

    camera.addCameraRotation(InputController.getMouseMove().mul(deltaTime));
    camera.setCameraPosition(cameraPos);

//    for(Cube c : cubes) {
//        c.rot.x += 15 * deltaTime;
//        c.rot.y += 15 * deltaTime;
//        c.rot.z += 15 * deltaTime;
//    }
}

private static void render() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    Matrix4x4 proj = Matrix4x4.createProjection(60.0, 1280.0, 720.0, 0.1, 1000.0);

    Matrix4x4 view = camera.getViewMatrix();
    Matrix4x4 viewProj = proj.mul(view);

    if (locCamera >= 0) {
        GL20.glUniformMatrix4fv(locCamera, true, camera.getViewFloatBuffer());
    }

    Object.drawAllObjects(viewProj, shaderProgram, locMvp, locModel);

//    for(Cube c : cubes) {
//        c.draw(viewProj, shaderProgram, locMvp, locModel);
//    }
}

private static void initShaders() {
    // loading and compiling main fragment and vertex shader.
    shaderProgram = ShaderProgram.load("cube.vert", "cube.frag");

    // initializing default values for default shader.
    locMvp            = GL20.glGetUniformLocation(shaderProgram, "uMVP");
    locModel          = GL20.glGetUniformLocation(shaderProgram, "uModel");
    locCamera         = GL20.glGetUniformLocation(shaderProgram, "uCamera");
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
    glEnable(GL_CULL_FACE);
    InputController.initController(window);

    return true;
}
