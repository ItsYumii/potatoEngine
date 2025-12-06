package objects.rasterizer;

import math.Vector2;
import math.Vector3;

public class Triangle {

    private final double repeat;
    private final Vector3 aw, bw, cw;
    private final Vector2 auv, buv, cuv;

    public Triangle(double repeat, Vector3 aw, Vector3 bw, Vector3 cw, Vector2 auv, Vector2 buv, Vector2 cuv) {
        this.repeat = repeat;
        this.aw = aw;
        this.bw = bw;
        this.cw = cw;
        this.auv = auv;
        this.buv = buv;
        this.cuv = cuv;
    }

    public Triangle(double repeat, double[] aw, double[] bw, double[] cw, double[] auv, double[] buv, double[] cuv) {
        this.repeat = repeat;
        this.aw  = Vector3.copy(aw);
        this.bw  = Vector3.copy(bw);
        this.cw  = Vector3.copy(cw);
        this.auv = Vector2.copy(auv);
        this.buv = Vector2.copy(buv);
        this.cuv = Vector2.copy(cuv);
    }


    public double getRepeat() { return repeat; }
    public Vector3 getA() { return aw; }
    public Vector3 getB() { return bw; }
    public Vector3 getC() { return cw; }
    public Vector2 getAuv() { return auv; }
    public Vector2 getBuv() { return buv; }
    public Vector2 getCuv() { return cuv; }
}
