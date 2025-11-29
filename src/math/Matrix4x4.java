package math;

import java.util.Arrays;

public class Matrix4x4 {

    public double[] m = new double[16];

    public Matrix4x4(double[] m) {
        System.arraycopy(m, 0, this.m, 0, 16);
    }

    public Matrix4x4() {
        Arrays.fill(m, 0);
    }

    public static Matrix4x4 createRotationX(double angle) {
        double angleRad = angle * Math.PI / 180.0,
               s = Math.sin(angleRad),
               c = Math.cos(angleRad);

        return new Matrix4x4(new double[]{
            1, 0,  0, 0,
            0, c, -s, 0,
            0, s,  c, 0,
            0, 0,  0, 1
        });
    }

    public static Matrix4x4 createRotationY(double angle) {
        double angleRad = angle * Math.PI / 180.0,
                s = Math.sin(angleRad),
                c = Math.cos(angleRad);

        return new Matrix4x4(new double[]{
             c, 0, s, 0,
             0, 1, 0, 0,
            -s, 0, c, 0,
             0, 0, 0, 1
        });
    }

    public static Matrix4x4 createRotationZ(double angle) {
        double angleRad = angle * Math.PI / 180.0,
                s = Math.sin(angleRad),
                c = Math.cos(angleRad);

        return new Matrix4x4(new double[]{
            c, -s, 0, 0,
            s,  c, 0, 0,
            0,  0, 1, 0,
            0,  0, 0, 1
        });
    }

    public static Matrix4x4 createTranslation(Vector3 shift) {
        return new Matrix4x4(new double[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                shift.x, shift.y, shift.z, 1
        });
    }
}
