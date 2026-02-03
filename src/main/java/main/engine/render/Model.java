package main.java.main.engine.render;

import com.google.gson.*;
import main.java.main.engine.assets.serialization.TriangleDeserializer;
import main.java.main.engine.core.debug.Console;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Model {

    private final List<Triangle> triangles = new ArrayList<>();

    public Model(String model) {
        loadFromJson(model);
    }

    private void loadFromJson(String jsonPath) {
        try {
            String json = Files.readString(Path.of("src/main/resources/assets/models/" + jsonPath + ".json"));

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Triangle.class, new TriangleDeserializer())
                    .create();

            JsonObject obj = gson.fromJson(json, JsonObject.class);
            JsonArray array = obj.getAsJsonArray("model");

            for (JsonElement e : array) {
                Triangle t = gson.fromJson(e, Triangle.class);

//                applyTransform(t);

                triangles.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.err("Model not found %s", jsonPath);
        }
    }

    public List<Triangle> getTriangles() {return this.triangles;}

//    private void applyTransform(Triangle t) {
//        scaleAndMove(t.aw);
//        scaleAndMove(t.bw);
//        scaleAndMove(t.cw);
//    }

//    private void scaleAndMove(Vector3 v) {
//        v.x = v.x * size.x + position.x;
//        v.y = v.y * size.y + position.y;
//        v.z = v.z * size.z + position.z;
//    }
}
