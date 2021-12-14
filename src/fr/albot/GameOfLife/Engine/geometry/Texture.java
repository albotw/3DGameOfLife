package fr.albot.GameOfLife.Engine.geometry;

import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static fr.albot.GameOfLife.Engine.GL.Util.ioResourceToByteBuffer;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Texture {
    private int handle;
    private ByteBuffer image;

    public Texture(String path) {
        ByteBuffer imageBuffer = null;
        try {
            imageBuffer = ioResourceToByteBuffer(path, 8 * 1024);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (MemoryStack stack = stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            if (!stbi_info_from_memory(imageBuffer, w, h, comp)) {
                throw new RuntimeException("impossible de lire les métadonnées de l'image: " + stbi_failure_reason());
            } else {
                System.out.println("OK: " + stbi_failure_reason());
            }

            this.image = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
            if (this.image == null) {
                throw new RuntimeException("impossible de charger l'image: " + stbi_failure_reason());
            }
        }
    }
}
