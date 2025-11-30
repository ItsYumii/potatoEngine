#version 330 core

layout(location = 0) in vec3 inPos;
layout(location = 1) in vec2 inUV;
layout(location = 2) in vec3 inNormal;

uniform mat4 uMVP;
uniform mat4 uModel; // to transform normals if you want

out vec2 vUV;
out vec3 vNormal;

void main() {
    vUV = inUV;
    // for now assume no non-uniform scale → just pass through
    vNormal = mat3(uModel) * inNormal;
    gl_Position = uMVP * vec4(inPos, 1.0);
}
