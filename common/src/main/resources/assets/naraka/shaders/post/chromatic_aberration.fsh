#version 330
#extension GL_ARB_separate_shader_objects: require

uniform sampler2D InSampler;

layout(std140) uniform ChromaticAberrationConfig {
    uniform float time;
    uniform float intensity;
    uniform float distortion;
    uniform float zoom;
};

layout (location = 0) in vec2 texCoord;

layout (location = 0) out vec4 fragColor;

void main() {
    vec2 center = texCoord - vec2(0.5);

    float distanceSquare = dot(center, center);
    float distortionFactor = 1.0 + (distanceSquare * distortion);

    float zoomFactor = 1.0 - zoom;

    vec2 transformedUV = (center * distortionFactor * zoomFactor) + vec2(0.5);

    transformedUV = clamp(transformedUV, vec2(0.0), vec2(1.0));

    vec2 transformedCenter = transformedUV - vec2(0.5);
    float distance = length(transformedCenter);

    float pulse = sin(time) * 0.2 + 0.8;
    float impactAmount = intensity * 0.8 * pulse;

    vec2 offset = (transformedCenter * distance * impactAmount);

    float r = texture(InSampler, transformedUV + offset).r;
    float g = texture(InSampler, transformedUV).g;
    float b = texture(InSampler, transformedUV - offset).b;
    float a = texture(InSampler, transformedUV).a;

    fragColor = vec4(r, g, b, a);
}