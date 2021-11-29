package graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Vector3f position;
    private Vector3f target;
    private Vector3f up;

    private double prevX;
    private double prevY;

    private double rotationRadius;

    public Camera(Vector3f position, double rotationRadius)
    {
        this.position = position;
        this.target = new Vector3f(0.0f, 0.0f, 0.0f);
        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.rotationRadius = rotationRadius;
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().setLookAt(this.position, this.target, this.up);
    }

    public void rotate(double angleX, double angleY)
    {
        if (angleX != 0.0 && angleY != 0.0)
        {
            double theta = angleX * Math.PI;
            double phi = angleY * 2 * Math.PI;

            this.position.x =(float) (this.rotationRadius * Math.sin(theta) * Math.cos(phi));
            this.position.z = (float) (this.rotationRadius * Math.sin(theta) * Math.sin(phi));
            this.position.y = (float) (this.rotationRadius * Math.cos(theta));
        }
    }
}
