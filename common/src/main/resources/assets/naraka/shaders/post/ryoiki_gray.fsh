#version 330
#extension GL_ARB_separate_shader_objects: require

uniform sampler2D InSampler;
uniform sampler2D RyoikiMaskSampler;

layout(std140) uniform RyoikiGrayConfig {
    uniform float delta;
};

layout (location = 0) in vec2 texCoord;

layout (location = 0) out vec4 fragColor;

void main() {
    vec4 color = texture(InSampler, texCoord);
    vec4 mask = texture(RyoikiMaskSampler, texCoord);

    float average = (color.r + color.g + color.b) / 9.0;
    vec4 grayscaledColor = vec4(vec3(average), 1.0);
    if (mask.a == 0) {
        fragColor = mix(color, grayscaledColor, delta);
    } else {
        vec4 intermediate = mix(grayscaledColor, vec4(0, 1, 0, 1), mask.a);
        fragColor = mix(color, intermediate, delta);
    }
}
