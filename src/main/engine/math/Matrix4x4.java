package main.engine.math;

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

    public Matrix4x4 mulM(Matrix4x4 other) {
        double[] a = this.m;
        double[] b = other.m;

        double r00 = a[0] * b[0]  + a[1] * b[4]  + a[2] * b[8]  + a[3] * b[12];
        double r01 = a[0] * b[1]  + a[1] * b[5]  + a[2] * b[9]  + a[3] * b[13];
        double r02 = a[0] * b[2]  + a[1] * b[6]  + a[2] * b[10] + a[3] * b[14];
        double r03 = a[0] * b[3]  + a[1] * b[7]  + a[2] * b[11] + a[3] * b[15];

        double r10 = a[4] * b[0]  + a[5] * b[4]  + a[6] * b[8]  + a[7] * b[12];
        double r11 = a[4] * b[1]  + a[5] * b[5]  + a[6] * b[9]  + a[7] * b[13];
        double r12 = a[4] * b[2]  + a[5] * b[6]  + a[6] * b[10] + a[7] * b[14];
        double r13 = a[4] * b[3]  + a[5] * b[7]  + a[6] * b[11] + a[7] * b[15];

        double r20 = a[8] * b[0]  + a[9] * b[4]  + a[10] * b[8]  + a[11] * b[12];
        double r21 = a[8] * b[1]  + a[9] * b[5]  + a[10] * b[9]  + a[11] * b[13];
        double r22 = a[8] * b[2]  + a[9] * b[6]  + a[10] * b[10] + a[11] * b[14];
        double r23 = a[8] * b[3]  + a[9] * b[7]  + a[10] * b[11] + a[11] * b[15];

        double r30 = a[12] * b[0]  + a[13] * b[4]  + a[14] * b[8]  + a[15] * b[12];
        double r31 = a[12] * b[1]  + a[13] * b[5]  + a[14] * b[9]  + a[15] * b[13];
        double r32 = a[12] * b[2]  + a[13] * b[6]  + a[14] * b[10] + a[15] * b[14];
        double r33 = a[12] * b[3]  + a[13] * b[7]  + a[14] * b[11] + a[15] * b[15];

        a[0]  = r00;  a[1]  = r01;  a[2]  = r02;  a[3]  = r03;
        a[4]  = r10;  a[5]  = r11;  a[6]  = r12;  a[7]  = r13;
        a[8]  = r20;  a[9]  = r21;  a[10] = r22;  a[11] = r23;
        a[12] = r30;  a[13] = r31;  a[14] = r32;  a[15] = r33;

        return this;
    }

    public void toFloatBuffer(FloatBuffer fb) {
        fb.clear();
        for (int i = 0; i < 16; i++) {
            fb.put((float) this.m[i]);
        }
        fb.flip();
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

    public Matrix4x4 setRotationX(double angle) {
        double rad = angle * Math.PI / 180.0;
        double s = Math.sin(rad);
        double c = Math.cos(rad);

        double[] m = this.m;

        m[0]  = 1; m[1]  = 0; m[2]  =  0; m[3]  = 0;
        m[4]  = 0; m[5]  = c; m[6]  = -s; m[7]  = 0;
        m[8]  = 0; m[9]  = s; m[10] =  c; m[11] = 0;
        m[12] = 0; m[13] = 0; m[14] =  0; m[15] = 1;

        return this;
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

    public Matrix4x4 setRotationY(double angle) {
        double rad = angle * Math.PI / 180.0;
        double s = Math.sin(rad);
        double c = Math.cos(rad);

        double[] m = this.m;

        m[0]  =  c; m[1]  = 0; m[2]  = s; m[3]  = 0;
        m[4]  =  0; m[5]  = 1; m[6]  = 0; m[7]  = 0;
        m[8]  = -s; m[9]  = 0; m[10] = c; m[11] = 0;
        m[12] =  0; m[13] = 0; m[14] = 0; m[15] = 1;

        return this;
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

    public Matrix4x4 setRotationZ(double angle) {
        double rad = angle * Math.PI / 180.0;
        double s = Math.sin(rad);
        double c = Math.cos(rad);

        double[] m = this.m;

        m[0]  = c; m[1]  = -s; m[2]  = 0; m[3]  = 0;
        m[4]  = s; m[5]  =  c; m[6]  = 0; m[7]  = 0;
        m[8]  = 0; m[9]  =  0; m[10] = 1; m[11] = 0;
        m[12] = 0; m[13] =  0; m[14] = 0; m[15] = 1;

        return this;
    }

    public static Matrix4x4 createFullRotation(Vector3 rot) {
        Matrix4x4 Rx = createRotationX(rot.x);
        Matrix4x4 Ry = createRotationY(rot.y);
        Matrix4x4 Rz = createRotationZ(rot.z);

        return Rz.mul(Rx.mul(Ry)); //YXZ rotation
    }

//    public void setFullRotation(double angle) {
//        double rad = angle * Math.PI / 180.0;
//        double s = Math.sin(rad);
//        double c = Math.cos(rad);
//
//        double[] m = this.m;
//
//        m[0]  = c; m[1]  = -s; m[2]  = 0; m[3]  = 0;
//        m[4]  = s; m[5]  =  c; m[6]  = 0; m[7]  = 0;
//        m[8]  = 0; m[9]  =  0; m[10] = 1; m[11] = 0;
//        m[12] = 0; m[13] =  0; m[14] = 0; m[15] = 1;
//    }

    public static Matrix4x4 createTranslation(Vector3 shift) {
        return new Matrix4x4(new double[]{
            1, 0, 0, shift.x,
            0, 1, 0, shift.y,
            0, 0, 1, shift.z,
            0, 0, 0, 1
        });
    }

    public Matrix4x4 setTranslation(double x, double y, double z) {
        double[] m = this.m;

        m[0]  = 1; m[1]  = 0; m[2]  = 0; m[3]  = x;
        m[4]  = 0; m[5]  = 1; m[6]  = 0; m[7]  = y;
        m[8]  = 0; m[9]  = 0; m[10] = 1; m[11] = z;
        m[12] = 0; m[13] = 0; m[14] = 0; m[15] = 1;

        return this;
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

    public Matrix4x4 copy(Matrix4x4 m) {
        System.arraycopy(m.m, 0, this.m, 0, 16);

        return this;
    }
}
