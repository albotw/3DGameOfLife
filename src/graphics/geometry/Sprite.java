package graphics.geometry;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Sprite {
    public final Mesh mesh;
    public Vector3f position;
    public float scale;
    public final Vector3f rotation;
    public final boolean wireframe;
    public boolean hidden;

    public Sprite(Mesh mesh, boolean wireframe) {
        this.mesh = mesh;
        this.wireframe = wireframe;
        this.position = new Vector3f();
        this.rotation = new Vector3f();
        this.scale = 1;
        this.hidden = false;
    }

    public Matrix4f getModelMatrix() {
        Matrix4f model = new Matrix4f();
        model.rotateX(this.rotation.x).rotateY(this.rotation.y).rotateZ(this.rotation.z);
        model.translate(this.position);
        model.scale(this.scale);
        return model;
    }

    public void moveTo(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }
}
