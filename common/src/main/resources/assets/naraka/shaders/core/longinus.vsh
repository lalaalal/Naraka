#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:projection.glsl>
#moj_import <minecraft:dynamictransforms.glsl>

in vec3 Position;
#ifdef CUTOUT
in vec2 UV0;
#endif

out vec4 texProj0;
out float sphericalVertexDistance;
out float cylindricalVertexDistance;

#ifdef CUTOUT
out vec2 texCoord0;
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
