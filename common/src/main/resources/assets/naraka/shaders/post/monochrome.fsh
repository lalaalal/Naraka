#version 330

uniform sampler2D InSampler;

in vec2 texCoord;

layout (std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 InSize;
};

out vec4 fragColor;

void main() {
    vec2 sizeRatio = OutSize / InSize;
    vec4 color = texture(InSampler, texCoord);
    float averageRounded = floor((color.r + color.g + color.b) / 3.0 + 0.5);

    fragColor = vec4(vec3(averageRounded), 1.0);
}
