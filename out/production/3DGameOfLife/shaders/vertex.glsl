#version 330 core

layout (location =0) in vec3 position;
layout (location =1) in vec3 inColour;

out vec3 exColour;

uniform mat4 model;
uniform mat4 view;
uniform mat4 proj;

// uniform vec3 offsets[100];

void main()
{
    //vec3 offset = offsets[gl_InstanceID];
    //gl_Position =  proj *  view *  model * vec4(position + offset, 1.0);
    gl_Position = proj * view * model * vec4(position, 1.0);
    exColour = inColour;
}