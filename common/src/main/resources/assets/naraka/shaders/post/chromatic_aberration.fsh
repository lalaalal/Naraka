#version 330
#extension GL_ARB_separate_shader_objects: require

uniform sampler2D InSampler;

layout(std140) uniform AberrationConfig {
    uniform float aberrationTime;
    uniform float impactIntensity;
};

layout (location = 0) in vec2 texCoord;

layout (location = 0) out vec4 fragColor;

void main() {
    vec2 center = texCoord - vec2(0.5);
    float distance = length(center);

    float pulse = sin(aberrationTime) * 0.2 + 0.8;
    float impactAmount = impactIntensity * 0.8 * pulse;

    vec2 offset = (center * distance * impactAmount);

    float r = texture(InSampler, texCoord + offset).r;
    float g = texture(InSampler, texCoord).g;
    float b = texture(InSampler, texCoord - offset).b;
    float a = texture(InSampler, texCoord).a;

    fragColor = vec4(r, g, b, a);
}