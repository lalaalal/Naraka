#version 330
#extension GL_ARB_separate_shader_objects: require

#include <minecraft:fog.glsl>
#include <minecraft:projection.glsl>
#include <minecraft:dynamictransforms.glsl>

layout (location = 0) in vec3 Position;
#ifdef CUTOUT
layout (location = 1) in vec2 UV0;
#endif

layout (location = 0) out vec4 texProj0;

layout (location = 1) out float sphericalVertexDistance;
layout (location = 2) out float cylindricalVertexDistance;

#ifdef CUTOUT
layout (location = 3) out vec2 texCoord0;
#endif

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    texProj0 = projection_from_position(gl_Position);
    sphericalVertexDistance = fog_spherical_distance(Position);
    cylindricalVertexDistance = fog_cylindrical_distance(Position);

    #ifdef CUTOUT
    texCoord0 = UV0;
    #endif
}
