#version 150

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:matrix.glsl>
#moj_import <minecraft:globals.glsl>

uniform sampler2D Sampler0;
#ifdef CUTOUT
uniform sampler2D Sampler1;
#endif

in vec4 texProj0;

#ifdef CUTOUT
in vec2 texCoord0;
#endif

mat4 end_portal_layer(float layer) {
    mat4 translate = mat4(
        1.0, 0.0, 0.0, 17.0 / layer,
        0.0, 1.0, 0.0, (2.0 + layer / 1.5) * (GameTime * 0.5),
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    );
    mat2 rotate = mat2_rotate_z(radians((layer * layer * 4321.0 + layer * 9.0) * 2.0));
    return mat4(rotate) * translate;
}

out vec4 fragColor;

void main() {
#ifdef CUTOUT
    if (texture(Sampler1, texCoord0).a < 0.1)
        discard;
#endif
    vec3 color = textureProj(Sampler0, texProj0 * end_portal_layer(1)).rgb;
    fragColor = vec4(color, 1.0);
}
