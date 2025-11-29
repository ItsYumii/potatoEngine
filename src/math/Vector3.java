package math;

import debug.Console;

public class Vector3 {

    public double x, y, z;

    public static final Vector3 ZERO = new Vector3(0, 0, 0),
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
    public Vector3 add(double k) {
        return new Vector3(this.x + k, this.y + k, this.z + k);
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
            Console.warn("Close to 0 division. (%s)", Double.toString(.0000000001));
            Console.info("Returning Infinite Vector3.");
            return INF.copy();
        }
        return new Vector3(this.x / k, this.y / k, this.z / k);
    }
    public Vector3 div(Vector3 v) {
        return new Vector3(this.x / v.x, this.y / v.y, this.z / v.z);
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
        return this.div(len);
    }

    public Vector3 copy() {
        return new Vector3(this.x, this.y, this.z);
    }

    // this is here so HashSet can compare Vectors as values, not their memory addresses.
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector3 v)) return false;
        return this.x == v.x && this.y == v.y && this.z == v.z;
    }

}
