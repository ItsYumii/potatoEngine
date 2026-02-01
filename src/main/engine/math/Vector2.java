package main.engine.math;

import main.engine.core.debug.Console;

import java.util.Arrays;

public class Vector2 {

    public double x, y;

    public static final Vector2 ZERO = new Vector2(0, 0),
            INF = new Vector2(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    private static final double EPS = 1e-9;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(Vector2 v) {
        return new Vector2(this.x + v.x, this.y + v.y);
    }
    public Vector2 add(double k) {
        return new Vector2(this.x + k, this.y + k);
    }

    public Vector2 sub(Vector2 v) {
        return new Vector2(this.x - v.x, this.y - v.y);
    }
    public Vector2 sub(double k) {
        return new Vector2(this.x - k, this.y - k);
    }

    public Vector2 mul(double k) {
        return new Vector2(k * this.x, k * this.y);
    }
    public Vector2 mul(Vector2 v) {
        return new Vector2(this.x * v.x, this.y * v.y);
    }

    public Vector2 div(double k) {
        if (Math.abs(k) < EPS) {
            Console.warn("Division by near-zero. (%s)", Double.toString(k));
            return INF.copy();
        }
        return new Vector2(this.x / k, this.y / k);
    }
    public Vector2 div(Vector2 v) {
        double rx, ry;

        if (Math.abs(v.x) < EPS) {
            Console.warn("Division by near-zero x: %s", v.toString());
            rx = Double.POSITIVE_INFINITY;
        } else rx = this.x / v.x;

        if (Math.abs(v.y) < EPS) {
            Console.warn("Division by near-zero y: %s", v.toString());
            ry = Double.POSITIVE_INFINITY;
        } else ry = this.y / v.y;

        return new Vector2(rx, ry);
    }

    public Vector2 floor() {
        return new Vector2(Math.floor(this.x), Math.floor(this.y));
    }
    public Vector2 sqrt() {
        return new Vector2(Math.sqrt(this.x), Math.sqrt(this.y));
    }
    public Vector2 abs() {
        return new Vector2(Math.abs(this.x), Math.abs(this.y));
    }
    public Vector2 pow(double k) {
        return new Vector2(Math.pow(this.x, k), Math.pow(this.y, k));
    }

//    public double dot(Vector2 v) {
//        return this.x * v.x + this.y * v.y;
//    }
//
//    public Vector2 cross(Vector2 v) {
//        return new Vector2(this.y * v.z - this.z * v.y, this.z * v.x - this.x * v.z, this.x * v.y - this.y * v.x);
//    }

    public double len() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vector2 normalize() {
        double len = this.len();
        if (len < EPS) {
            return ZERO.copy();
        }
        return this.div(len);
    }

    public Vector2 copy() {
        return new Vector2(this.x, this.y);
    }
    public static Vector2 copy(double[] d) {
        if(d.length < 2) {
            Console.err("Array passed has less then 2 arguments (%s)", Arrays.toString(d));
            System.exit(-1);
        } else if (d.length > 2) Console.warn("Array passed has more then 2 arguments (%s)", Arrays.toString(d));

        return new Vector2(d[0], d[1]);
    }

    // this is here so HashSet can compare Vectors as values, not their memory addresses.
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector2 v)) return false;
        return this.x == v.x && this.y == v.y;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(this.x) ^ Double.hashCode(this.y);
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
