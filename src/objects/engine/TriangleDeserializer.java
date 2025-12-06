package objects.engine;

import com.google.gson.*;
import math.Vector2;
import math.Vector3;
import objects.rasterizer.Triangle;

import java.lang.reflect.Type;

class TriangleDeserializer implements JsonDeserializer<Triangle> {

    @Override
    public Triangle deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        double[] v1 = toDoubleArray(obj.getAsJsonArray("v1")),
                 v2 = toDoubleArray(obj.getAsJsonArray("v2")),
                 v3 = toDoubleArray(obj.getAsJsonArray("v3")),
                 uv1 = toDoubleArray(obj.getAsJsonArray("uv1")),
                 uv2 = toDoubleArray(obj.getAsJsonArray("uv2")),
                 uv3 = toDoubleArray(obj.getAsJsonArray("uv3"));

        return new Triangle(
                1,
                v1, v2, v3,
                uv1, uv2, uv3
        );
    }

    private double[] toDoubleArray(JsonArray arr) {
        double[] result = new double[arr.size()];

        for(int i = 0; i < arr.size(); i++) result[i] = arr.get(i).getAsDouble();
        return result;
    }
}
