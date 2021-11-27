package graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Vector3f position;
    private Vector3f target;
    private Vector3f up;

    private double rotationRadius;

    public Camera(Vector3f position, double rotationRadius)
    {
        this.position = position;
        this.target = new Vector3f(0.0f, 0.0f, 0.0f);
        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.rotationRadius = rotationRadius;
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(this.position, this.target, this.up);
    }

    public void rotate(double angleX, double angleY)
    {
        double x = Math.sin(angleX)* this.rotationRadius;
        double y =  Math.cos(angleY) * this.rotationRadius;

        this.position.x = (float) x;
        this.position.y = (float) y;
        System.out.println(this.position);
    }
}
