#version 330
#extension GL_ARB_separate_shader_objects: require

uniform sampler2D InSampler;

layout (location = 0) in vec2 texCoord;

layout (location = 0) out vec4 fragColor;

void main() {
    vec4 color = texture(InSampler, texCoord);
    float averageRounded = floor((color.r + color.g + color.b) / 3.0 + 0.5);

    fragColor = vec4(vec3(averageRounded), 1.0);
}
