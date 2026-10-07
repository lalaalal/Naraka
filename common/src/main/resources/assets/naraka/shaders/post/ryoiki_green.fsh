#version 330
#extension GL_ARB_separate_shader_objects: require

uniform sampler2D InSampler;

layout (location = 0) in vec2 texCoord;

layout (location = 0) out vec4 fragColor;

void main() {
    vec4 color = texture(InSampler, texCoord);
    if (color.r + color.b > color.g || color.g < 0.67) {
        fragColor = vec4(color.r / 3.0, color.g / 2.0, color.b / 3.0, 1.0);
    } else {
        fragColor = color;
    }
}
