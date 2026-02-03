package main.java.main.engine.math;

import main.java.main.engine.core.debug.Console;

import java.util.Arrays;

public class Vector3 {

    public double x, y, z;

    public static final Vector3
        ZERO = new Vector3(0, 0, 0),
        INF = new Vector3(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

    private static final double EPS = 1e-9;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 add(Vector3 v) {
        return new Vector3(this.x + v.x, this.y + v.y, this.z + v.z);
    }
    public Vector3 add(Vector2 v) {
        return new Vector3(this.x + v.x, this.y + v.y, this.z);
    }
    public Vector3 add(double k) {
        return new Vector3(this.x + k, this.y + k, this.z + k);
    }

    public void addM(Vector3 v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
    }
    public void addM(Vector2 v) {
        this.x += v.x;
        this.y += v.y;
    }
    public void addM(double k) {
        this.x += k;
        this.y += k;
        this.z += k;
    }

    public Vector3 sub(Vector3 v) {
        return new Vector3(this.x - v.x, this.y - v.y, this.z - v.z);
    }
    public Vector3 sub(double k) {
        return new Vector3(this.x - k, this.y - k, this.z - k);
    }

    public Vector3 mul(double k) {
        return new Vector3(k * this.x, k * this.y, k * this.z);
    }
    public Vector3 mul(Vector3 v) {
        return new Vector3(this.x * v.x, this.y * v.y, this.z * v.z);
    }

    public Vector3 div(double k) {
        if (Math.abs(k) < EPS) {
            Console.warn("Division by near-zero. (%s)", Double.toString(k));
            return INF.copy();
        }
        return new Vector3(this.x / k, this.y / k, this.z / k);
    }
    public Vector3 div(Vector3 v) {
        double rx, ry, rz;

        if (Math.abs(v.x) < EPS) {
            Console.warn("Division by near-zero x: %s", v.toString());
            rx = Double.POSITIVE_INFINITY;
        } else rx = this.x / v.x;

        if (Math.abs(v.y) < EPS) {
            Console.warn("Division by near-zero y: %s", v.toString());
            ry = Double.POSITIVE_INFINITY;
        } else ry = this.y / v.y;

        if (Math.abs(v.z) < EPS) {
            Console.warn("Division by near-zero z: %s", v.toString());
            rz = Double.POSITIVE_INFINITY;
        } else rz = this.z / v.z;

        return new Vector3(rx, ry, rz);
    }

    public Vector3 floor() {
        return new Vector3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
    }
    public Vector3 sqrt() {
        return new Vector3(Math.sqrt(this.x), Math.sqrt(this.y), Math.sqrt(this.z));
    }
    public Vector3 abs() {
        return new Vector3(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }
    public Vector3 pow(double k) {
        return new Vector3(Math.pow(this.x, k), Math.pow(this.y, k), Math.pow(this.z, k));
    }

    public double dot(Vector3 v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public Vector3 cross(Vector3 v) {
        return new Vector3(this.y * v.z - this.z * v.y, this.z * v.x - this.x * v.z, this.x * v.y - this.y * v.x);
    }

    public double len() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vector3 normalize() {
        double len = this.len();
        if (len < EPS) {
            return ZERO.copy();
        }
        return this.div(len);
    }

    public Vector3 copy() {
        return new Vector3(this.x, this.y, this.z);
    }
    public static Vector3 copy(double[] d) {
        if(d.length < 3) {
            Console.err("Array passed has less then 3 arguments (%s)", Arrays.toString(d));
            System.exit(-1);
        } else if (d.length > 3) Console.warn("Array passed has more then 3 arguments (%s)", Arrays.toString(d));

        return new Vector3(d[0], d[1], d[2]);
    }
    public void copy(Vector3 v) {
        this.x = v.x; this.y = v.y; this.z = v.z;
    }

    // this is here so HashSet can compare Vectors as values, not their memory addresses.
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector3 v)) return false;
        return this.x == v.x && this.y == v.y && this.z == v.z;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(this.x) ^ Double.hashCode(this.y) ^ Double.hashCode(this.z);
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
}
