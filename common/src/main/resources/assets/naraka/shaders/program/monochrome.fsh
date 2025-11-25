#version 330

uniform sampler2D DiffuseSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
    float averageRounded = floor((color.r + color.g + color.b) / 3.0 + 0.5);

    fragColor = vec4(vec3(averageRounded), 1.0);
}
