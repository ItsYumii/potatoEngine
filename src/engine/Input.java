package engine;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LAST;

public final class Input {
    private static final boolean[]
            keys  = new boolean[GLFW_KEY_LAST + 1],
            mouse = new boolean[GLFW_MOUSE_BUTTON_LAST + 1];

    public static void setKey(int key, boolean down) {
        if (key >= 0 && key < keys.length) keys[key] = down;
    }

    public static void setMouse(int mouseKey, boolean down) {
        if (mouseKey >= 0 && mouseKey < mouse.length) mouse[mouseKey] = down;
    }

    public static boolean isKeyDown(int key) {return key >= 0 && key < keys.length && keys[key];}

    public static boolean isMouseDown(int mouseKey) {return mouseKey >= 0 && mouseKey < mouse.length && mouse[mouseKey];}
}
