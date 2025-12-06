package engine;

import math.Vector2;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.*;

public final class InputController {

    private InputController() {}

    private static final Vector2 mouseMove = Vector2.ZERO.copy();



    public static void initController(long window) {
        // ---- KEYBOARD INPUT ----
        glfwSetKeyCallback(window, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                    glfwSetWindowShouldClose(window, true);
                }

                if (action == GLFW_PRESS) {
                    Input.setKey(key, true);
                } else if (action == GLFW_RELEASE) {
                    Input.setKey(key, false);
                }
            }
        });

        // ---- MOUSE MOVEMENT ----
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        if (glfwRawMouseMotionSupported()) {
            glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);
        }

        double[] lastX = {0};
        double[] lastY = {0};
        final boolean[] firstMouse = {true};

        glfwSetCursorPosCallback(window, (window_WHY_DO_I_NEED_TO_INITIALIZE_YOU_SECOND_TIME_WTF, xPos, yPos) -> {

            if (firstMouse[0]) {
                lastX[0] = xPos;
                lastY[0] = yPos;
                firstMouse[0] = false;
            }

            double dx = lastX[0] - xPos;
            double dy = lastY[0] - yPos;

            lastX[0] = xPos;
            lastY[0] = yPos;

            mouseMove.y = dx * Settings.getSensitivity();
            mouseMove.x = dy * Settings.getSensitivity();
        });


        // ---- MOUSE BUTTONS ----
        glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int key, int action, int mods) {
                if (action == GLFW_PRESS) {
                    Input.setMouse(key, true);
                } else if (action == GLFW_RELEASE) {
                    Input.setMouse(key, false);
                }
            }
        });
    }

    public static Vector2 getMouseMove() {
        return mouseMove;
    }

    public static void setMouseMove(double x, double y) {
        mouseMove.x = x;
        mouseMove.y = y;
    }
}
