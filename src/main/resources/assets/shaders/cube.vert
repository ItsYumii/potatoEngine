#version 150

in vec3 inPos;
in vec2 inUV;
in vec3 inNormal;

uniform mat4 uMVP;
uniform mat4 uModel;
uniform mat4 uCamera;
uniform vec3 uLightDir;

out vec2 vUV;
out vec3 vNormal;
out vec3 vLightDir;

void main() {
    vUV = inUV;
    vNormal = mat3(uModel) * inNormal;
    vLightDir = mat3(transpose(uCamera)) * uLightDir;
    gl_Position = uMVP * vec4(inPos, 1.0);
}