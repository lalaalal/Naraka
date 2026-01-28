#version 330

uniform sampler2D DiffuseSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
    if (color.r + color.b > color.g || color.g < 0.67) {
        fragColor = vec4(color.r / 3.0, color.g / 2.0, color.b / 3.0, 1.0);
    } else {
        fragColor = color;
    }
}
