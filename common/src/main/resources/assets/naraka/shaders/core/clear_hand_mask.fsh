#version 330
#extension GL_ARB_separate_shader_objects : require

uniform sampler2D InSampler;

layout (location = 0) in vec2 texCoord;

layout (location = 0) out vec4 fragColor;

void main() {
    float handDepth = texture(InSampler, texCoord).r;

    if (handDepth <= 0.0)
    discard;

    fragColor = vec4(0.0);
}