#version 330 core

layout(location = 0) in vec3 inPos;
layout(location = 1) in vec2 inUV;

uniform mat4 uMVP;

out vec2 vUV;

void main() {
    vUV = inUV;
    gl_Position = uMVP * vec4(inPos, 1.0);
}
