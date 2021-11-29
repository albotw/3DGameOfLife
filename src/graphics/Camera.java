package graphics;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static CONFIG.CONFIG.WINDOW_HEIGHT;
import static CONFIG.CONFIG.WINDOW_WIDTH;

public class Camera {
    private final Vector3f position;
    private final Vector3f target;
    private final Vector3f up;

    private double azimuth;
    private double polar;

    private double theta = 0.0f;
    private double phi = 0.0f;

    private final double rotationRadius;

    public Camera(Vector3f position, double rotationRadius)
    {
        this.position = position;
        this.target = new Vector3f(0.0f, 0.0f, 0.0f);
        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.rotationRadius = rotationRadius;
        this.azimuth = 0.0f;
        this.polar = 0.0f;
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(this.position, this.target, this.up);
    }

    public void rotateAzimuth(double angle) {
        this.azimuth += angle;
        this.azimuth  = this.azimuth % (2 * Math.PI);
        if (this.azimuth < 0.0f) {
            this.azimuth = (2 * Math.PI) + this.azimuth;
        }
    }

    public void rotatePolar(double angle) {
        this.polar += angle;

        double cap = (float) (Math.PI / 2.0f - 0.001f);
        if (this.polar > cap) { this.polar = cap;}
        if (this.polar < -cap) {this.polar = -cap;}
    }

    public void rotate(double angleX, double angleY) {
        rotateAzimuth(angleX);
        rotatePolar(angleY);

        this.position.x = (float) (this.rotationRadius * Math.cos(this.polar) * Math.cos(this.azimuth));
        this.position.y = (float) (this.rotationRadius * Math.sin(this.polar));
        this.position.z = (float) (this.rotationRadius * Math.cos(this.polar) * Math.sin(this.azimuth));
    }

    public void _rotate(double angleX, double angleY)
    {
            System.out.println("X: " + angleX + " | y: " + angleY);

            //double deltaX = Math.PI) / WINDOW_WIDTH;
            //double deltaY = (2 * Math.PI) / WINDOW_HEIGHT

            this.theta = angleX / Math.PI;
            this.phi = angleY / (2 * Math.PI);

            System.out.println("theta: " + theta + " | phi: " + phi);

            float theta_x = (float) (Math.cos(theta) * this.position.x + Math.sin(theta) * this.position.z);
            float theta_z = (float) ((float) -Math.sin(theta) * this.position.x + Math.cos(theta) * this.position.z);

            this.position.x = theta_x;
            this.position.z = theta_z;
            this.position.y = this.position.y;

            this.up.x = theta_x;
            this.up.z = theta_z;
            this.up.y = this.position.y;

            float phi_y = (float) (Math.cos(phi) * this.position.y + Math.sin(phi) * this.position.z);
            float phi_z = (float) (-Math.sin(phi) * this.position.y + Math.cos(phi) * this.position.z);
            this.position.x = theta_x;
            this.position.z = phi_z;
            this.position.y = phi_y;

            this.up.x = this.up.x;
            this.up.z = -this.position.y;
            this.up.y = this.position.z;
    }
}
