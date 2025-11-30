package math;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.Arrays;

public class Matrix4x4 {

    // Row-major: m[row * 4 + col]
    public double[] m = new double[16];

    public Matrix4x4(double[] m) {
        System.arraycopy(m, 0, this.m, 0, 16);
    }

    public Matrix4x4() {
        Arrays.fill(m, 0);
    }

    public static Matrix4x4 identity() {
        return new Matrix4x4(new double[]{
                1,0,0,0,
                0,1,0,0,
                0,0,1,0,
                0,0,0,1
        });
    }

    // row-major multiply: out = this * b
    public Matrix4x4 mul(Matrix4x4 b) {
        double[] out = new double[16];
        double[] a = this.m;
        double[] c = b.m;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                out[row * 4 + col] =
                    a[row * 4    ] * c[col     ] +
                    a[row * 4 + 1] * c[col + 4 ] +
                    a[row * 4 + 2] * c[col + 8 ] +
                    a[row * 4 + 3] * c[col + 12];
            }
        }

        return new Matrix4x4(out);
    }

    // dump row-major, let GL transpose
    public static FloatBuffer toFloatBuffer(Matrix4x4 mat) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        for (int i = 0; i < 16; i++) {
            fb.put((float) mat.m[i]);
        }
        fb.flip();
        return fb;
    }

    public static Matrix4x4 createScale(Vector3 scale) {
        return new Matrix4x4(new double[]{
                scale.x, 0,       0,       0,
                0,       scale.y, 0,       0,
                0,       0,       scale.z, 0,
                0,       0,       0,       1
        });
    }

    public static Matrix4x4 createRotationX(double angle) {
        double rad = angle * Math.PI / 180.0;
        double s = Math.sin(rad);
        double c = Math.cos(rad);

        return new Matrix4x4(new double[]{
                1, 0,  0, 0,
                0, c, -s, 0,
                0, s,  c, 0,
                0, 0,  0, 1
        });
    }

    public static Matrix4x4 createRotationY(double angle) {
        double rad = angle * Math.PI / 180.0;
        double s = Math.sin(rad);
        double c = Math.cos(rad);

        return new Matrix4x4(new double[]{
                c, 0, s, 0,
                0, 1, 0, 0,
                -s, 0, c, 0,
                0, 0, 0, 1
        });
    }

    public static Matrix4x4 createRotationZ(double angle) {
        double rad = angle * Math.PI / 180.0;
        double s = Math.sin(rad);
        double c = Math.cos(rad);

        return new Matrix4x4(new double[]{
                c, -s, 0, 0,
                s,  c, 0, 0,
                0,  0, 1, 0,
                0,  0, 0, 1
        });
    }

    public static Matrix4x4 createFullRotation(Vector3 rot) {
        Matrix4x4 Rx = createRotationX(rot.x);
        Matrix4x4 Ry = createRotationY(rot.y);
        Matrix4x4 Rz = createRotationZ(rot.z);

        return Rz.mul(Rx.mul(Ry)); //YXZ rotation
    }

    public static Matrix4x4 createTranslation(Vector3 shift) {
        return new Matrix4x4(new double[]{
            1, 0, 0, shift.x,
            0, 1, 0, shift.y,
            0, 0, 1, shift.z,
            0, 0, 0, 1
        });
    }

    public static Matrix4x4 createProjection(double fovDeg, double width, double height, double near, double far) {

        double aspect = width / height;
        double f = 1.0 / Math.tan((fovDeg * 0.5) * Math.PI / 180.0);
        double nf = 1.0 / (near - far);

        return new Matrix4x4(new double[]{
            f / aspect, 0,                  0,                        0,
                     0, f,                  0,                        0,
                     0, 0,  (far + near) * nf,  (2.0 * far * near) * nf,
                     0, 0,                 -1,                        0
        });
    }
}
