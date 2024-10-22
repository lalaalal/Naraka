#version 150

uniform sampler2D SamplerBackground;
uniform sampler2D SamplerForeground;

uniform float GameTime;
uniform int LonginusLayers;

in vec4 textureProjection;

const vec3[] COLORS = vec3[](
    vec3(0.07),
    vec3(0.06),
    vec3(0.07),
    vec3(0.08),
    vec3(0.09),
    vec3(0.10),
    vec3(0.11),
    vec3(0.12),
    vec3(0.14),
    vec3(0.16),
    vec3(0.19),
    vec3(0.22),
    vec3(0.27),
    vec3(0.32),
    vec3(0.49),
    vec3(0.71)
);

const mat4 SCALE_TRANSLATE = mat4(
    0.5, 0.0, 0.0, 0.25,
    0.0, 0.5, 0.0, 0.25,
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
);

mat2 mat2_rotate_z(float radians) {
    return mat2(
        cos(radians), -sin(radians),
        sin(radians), cos(radians)
    );
}

mat4 end_portal_layer(float layer) {
    mat4 translate = mat4(
        1.0, 0.0, 0.0, 17.0 / layer,
        0.0, 1.0, 0.0, (2.0 + layer / 1.5) * (GameTime * 1.5),
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    );

    mat2 rotate = mat2_rotate_z(radians((layer * layer * 4321.0 + layer * 9.0) * 2.0));

    mat2 scale = mat2((4.5 - layer / 4.0) * 2.0);

    return mat4(scale * rotate) * translate * SCALE_TRANSLATE;
}

out vec4 fragColor;

void main() {
    vec3 color = textureProj(SamplerBackground, textureProjection).rgb * COLORS[0];
    for (int i = 0; i < LonginusLayers; i++)
    color += textureProj(SamplerForeground, textureProjection * end_portal_layer(float(i + 1))).rgb * COLORS[i];
    fragColor = vec4(color, 1.0);
}
