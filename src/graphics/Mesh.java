package graphics;

import org.lwjgl.system.MemoryUtil;

import java.nio.*;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private final int vaoID;
    private final int pos_vboID;
    private final int color_vboID;
    private final int idx_vboID;

    private final int vertexCount;
    private final boolean wireframe;

    public Mesh(float[] positions, float[] colours, int[] indices, boolean wireframe)
    {
        this.wireframe = wireframe;
        FloatBuffer posBuffer = null;
        FloatBuffer colourBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            this.vertexCount = indices.length;

            this.vaoID = glGenVertexArrays();
            glBindVertexArray(this.vaoID);

            // Position VBO
            this.pos_vboID = glGenBuffers();
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, this.pos_vboID);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Colour VBO
            this.color_vboID = glGenBuffers();
            colourBuffer = MemoryUtil.memAllocFloat(colours.length);
            colourBuffer.put(colours).flip();
            glBindBuffer(GL_ARRAY_BUFFER, color_vboID);
            glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

            // Index VBO
            this.idx_vboID = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.idx_vboID);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (colourBuffer != null) {
                MemoryUtil.memFree(colourBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    public void render()
    {
        glBindVertexArray(getVaoID());
        glEnableVertexAttribArray(0);
        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);
    }

    public boolean isWireframe() {
        return this.wireframe;
    }

    public int getVaoID()
    {
        return vaoID;
    }

    public int getVertexCount()
    {
        return vertexCount;
    }

    public void flush()
    {
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(pos_vboID);
        glDeleteBuffers(color_vboID);
        glDeleteBuffers(idx_vboID);

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);
    }
}
