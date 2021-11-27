package graphics;

import org.joml.Vector3f;

public class Sprite {
    private final Mesh mesh;

    public Vector3f position;
    public float scale;
    public final Vector3f rotation;

    public Sprite (Mesh mesh) {
        this.mesh = mesh;
        this.position = new Vector3f();
        this.rotation = new Vector3f();
        this.scale = 1;
    }

    public static Sprite fromCell(int x, int y, int z) {
        return new Sprite(null);
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }
}
