import debug.Console;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11C;



void main() {
    long window;
    if(!GLFW.glfwInit()) return;

    window = GLFW.glfwCreateWindow(1280, 720, "potato engine v0.0000000000000000000001 lul", 0, 0);

    if (window == 0) {
        GLFW.glfwTerminate();
        return;
    }

    GLFW.glfwMakeContextCurrent(window);
    GL.createCapabilities();
    GL11C.glClearColor(0.18f, 0.18f, 0.18f, 1.0f);

    while (!GLFW.glfwWindowShouldClose(window)) {
        GL11C.glClear(GL11C.GL_COLOR_BUFFER_BIT);

        GLFW.glfwSwapBuffers(window);

        GLFW.glfwPollEvents();
    }

    GLFW.glfwTerminate();
}
