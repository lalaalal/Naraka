#version 330

uniform sampler2D DiffuseSampler;

uniform float delta;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
    float average = (color.r + color.g + color.b) / 9.0;
    if (color.r + color.b > color.g || color.g < 0.67) {
        vec4 grayscaledColor = vec4(vec3(average), 1.0);
        fragColor = mix(color, grayscaledColor, delta);
    } else {
        fragColor = color;
    }
}
