package graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Sprite {
    public final Mesh mesh;
    public Vector3f position;
    public float scale;
    public final Vector3f rotation;

    public Sprite (Mesh mesh) {
        this.mesh = mesh;
        this.position = new Vector3f();
        this.rotation = new Vector3f();
        this.scale = 1;
    }

    public Matrix4f getModelMatrix() {
        Matrix4f model = new Matrix4f();
        model.rotateX(this.rotation.x).rotateY(this.rotation.y).rotateZ(this.rotation.z);
        model.scale(this.scale);
        model.translate(this.position);
        return model;
    }

    public static Sprite fromCell(int x, int y, int z) {
        return new Sprite(null);
    }
}
