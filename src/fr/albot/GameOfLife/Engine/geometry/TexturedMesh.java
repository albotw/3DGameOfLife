package fr.albot.GameOfLife.Engine.geometry;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class TexturedMesh extends Mesh {

    protected int tex_vboID;

    public TexturedMesh(float[] positions, float[] texCoords, int[] indices) {
        super(positions, (float[]) null, indices);
        FloatBuffer texBuffer = null;
        try {
            glBindVertexArray(this.vaoID);
            this.tex_vboID = glGenBuffers();
            texBuffer = MemoryUtil.memAllocFloat(texCoords.length);
            texBuffer.put(texCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, this.tex_vboID);
            glBufferData(GL_ARRAY_BUFFER, this.tex_vboID, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (texBuffer != null) {
                MemoryUtil.memFree(texBuffer);
            }
        }
    }
}
