#version 330
#extension GL_ARB_separate_shader_objects: require

uniform sampler2D InSampler;
uniform sampler2D RyoikiMaskSampler;

layout(std140) uniform RyoikiGreenConfig {
    uniform float delta;
};

layout (location = 0) in vec2 texCoord;

layout (location = 0) out vec4 fragColor;

void main() {
    vec4 color = texture(InSampler, texCoord);
    vec4 mask = texture(RyoikiMaskSampler, texCoord);

    vec4 greenTintedColor = vec4(color.r / 3.0, color.g / 1.5, color.b / 3.0, 1.0);
    if (mask.a == 0) {
        fragColor = mix(color, greenTintedColor, delta);
    } else {
        vec4 intermediate = mix(greenTintedColor, vec4(0, 1, 0, 1), mask.a);
        fragColor = mix(color, intermediate, delta);
    }
}
