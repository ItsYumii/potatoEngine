package main.java.main.engine.render;

import main.java.main.engine.core.debug.Console;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL11C.GL_NEAREST;
import static org.lwjgl.opengl.GL11C.GL_REPEAT;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL11C.GL_RGBA8;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11C.glBindTexture;
import static org.lwjgl.opengl.GL11C.glTexImage2D;
import static org.lwjgl.opengl.GL11C.glTexParameteri;

public class Texture {

    private final static List<Texture> allTextures = new ArrayList<>();
    private final String path;
    private final int id;

    public Texture(String texturePath, int textureID) {
        this.path = texturePath;
        this.id   = textureID;

        allTextures.add(this);
    }

    public static Texture loadTexture(String texturePath) {

        Texture t;
        if((t = checkIfTextureExists(texturePath)) != null) {
            return t;
        }

        int texId;
        String filePath = "src/main/resources/assets/textures/" + texturePath + ".png";

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            STBImage.stbi_set_flip_vertically_on_load(true);
            ByteBuffer image = STBImage.stbi_load(filePath, w, h, channels, 4);
            if (image == null) {
                Console.err("Path to texture invalid (%s)", filePath);
                image = STBImage.stbi_load("src/main/resources/assets/textures/no_texture.png", w, h, channels, 4);
                if (image == null) {
                    Console.err("Missing texture loading failed", filePath);
                    System.exit(-1);
                }
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

        return new Texture(texturePath, texId);
    }

    private static Texture checkIfTextureExists(String texturePath) {
        for(Texture texture : allTextures)
            if(Objects.equals(texture.path, texturePath)) return texture;

        return null;
    }

    public int getID() {
        return this.id;
    }
}
