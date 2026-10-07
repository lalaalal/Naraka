#version 330
#extension GL_ARB_separate_shader_objects: require

uniform sampler2D InSampler;

layout (location = 0) in vec2 texCoord;

layout (location = 0) out vec4 fragColor;

void main() {
    vec4 color = texture(InSampler, texCoord);
    float average = (color.r + color.g + color.b) / 9.0;
    if (color.r + color.b > color.g || color.g < 0.67) {
        fragColor = vec4(vec3(average), 1.0);
    } else {
        fragColor = color;
    }
}
