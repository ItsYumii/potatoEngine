#version 330 core

in vec2 vUV;
in vec3 vNormal;

uniform sampler2D uTexture;
uniform vec3 uLightDir;    // direction FROM light TO origin
uniform vec3 uAmbient;
uniform float uDiffuseMul;

out vec4 fragColor;

void main() {
    vec3 albedo = texture(uTexture, vUV).rgb;

    vec3 N = normalize(vNormal);
    vec3 L = normalize(-uLightDir);  // light rays go opposite uLightDir

    float diff = max(dot(N, L), 0.0) * uDiffuseMul;

    vec3 lighting = uAmbient + diff;
    lighting = clamp(lighting, 0.0, 1.0);

    fragColor = vec4(albedo * lighting, 1.0);
}
