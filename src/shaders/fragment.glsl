#version 330 core

in vec2 TexCoord;
in  vec3 exColour;
out vec4 fragColor;

uniform sampler2D Texture;

void main()
{
    fragColor = texture(Texture, TexCoord) * vec4(exColour, 1.0);
}