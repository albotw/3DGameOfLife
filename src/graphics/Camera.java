package graphics;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static CONFIG.CONFIG.WINDOW_HEIGHT;
import static CONFIG.CONFIG.WINDOW_WIDTH;

public class Camera {
    private Vector3f position;
    private Vector3f target;
    private Vector3f up;

    private double prevX;
    private double prevY;

    private double rotationRadius;
    private Matrix4f viewMatrix;

    public Camera(Vector3f position, double rotationRadius)
    {
        this.position = position;
        this.target = new Vector3f(0.0f, 0.0f, 0.0f);
        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.rotationRadius = rotationRadius;
        this.viewMatrix = new Matrix4f().setLookAt(this.position, this.target, this.up);
    }

    public Matrix4f getViewMatrix() {
        return this.viewMatrix;
    }

    public void updateViewMatrix() {
        this.viewMatrix.setLookAt(this.position, this.target, this.up);
    }

    public Vector3f getViewDir() {
        return this.viewMatrix.transpose().getRow(2, new Vector3f()).negate();
    }


    public void rotate(double angleX, double angleY)
    {
        double deltaX = (2 * Math.PI) / WINDOW_WIDTH;
        double deltaY = (2 * Math.PI) / WINDOW_HEIGHT;
        angleX = angleX * deltaX;
        angleY = angleY * deltaY;

        Vector3f direction = this.position.sub(this.target);
        Vector3f right = direction.cross(this.up).normalize();
        //this.up = right.cross(right, direction).normalize();

        System.out.println("right: " + right);
        this.position.rotateAxis((float) angleX, right.x, right.y, right.z).mul(this.position);

        this.position.rotateAxis((float) angleY, this.up.x, this.up.y, this.up.z).mul(this.position);

        System.out.println(this.position);
        System.out.println();

        this.updateViewMatrix();
    }
}
