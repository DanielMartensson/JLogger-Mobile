#ifdef GL_ES
#extension GL_OES_standard_derivatives : enable
#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
precision highp int;
#else
precision mediump float;
precision mediump int;
#endif
#else
#define highp
#define mediump
#define lowp
#endif
varying vec2 texCoord0;
uniform sampler2D img;
uniform int count;
uniform vec4 offset;
uniform vec4 weights[16];
void main() {
int i;
vec4 tmp = vec4(0.0, 0.0, 0.0, 0.0);
vec2 loc = texCoord0 + offset.zw;
{
tmp += weights[0].x * texture2D(img, loc);
loc += offset.xy;
tmp += weights[0].y * texture2D(img, loc);
loc += offset.xy;
tmp += weights[0].z * texture2D(img, loc);
loc += offset.xy;
tmp += weights[0].w * texture2D(img, loc);
loc += offset.xy;
}
{
tmp += weights[1].x * texture2D(img, loc);
loc += offset.xy;
tmp += weights[1].y * texture2D(img, loc);
loc += offset.xy;
tmp += weights[1].z * texture2D(img, loc);
loc += offset.xy;
tmp += weights[1].w * texture2D(img, loc);
loc += offset.xy;
}
{
tmp += weights[2].x * texture2D(img, loc);
loc += offset.xy;
tmp += weights[2].y * texture2D(img, loc);
loc += offset.xy;
tmp += weights[2].z * texture2D(img, loc);
loc += offset.xy;
tmp += weights[2].w * texture2D(img, loc);
loc += offset.xy;
}
{
tmp += weights[3].x * texture2D(img, loc);
loc += offset.xy;
tmp += weights[3].y * texture2D(img, loc);
loc += offset.xy;
tmp += weights[3].z * texture2D(img, loc);
loc += offset.xy;
tmp += weights[3].w * texture2D(img, loc);
loc += offset.xy;
}
{
tmp += weights[4].x * texture2D(img, loc);
loc += offset.xy;
tmp += weights[4].y * texture2D(img, loc);
loc += offset.xy;
tmp += weights[4].z * texture2D(img, loc);
loc += offset.xy;
tmp += weights[4].w * texture2D(img, loc);
loc += offset.xy;
}
{
tmp += weights[5].x * texture2D(img, loc);
loc += offset.xy;
tmp += weights[5].y * texture2D(img, loc);
loc += offset.xy;
tmp += weights[5].z * texture2D(img, loc);
loc += offset.xy;
tmp += weights[5].w * texture2D(img, loc);
loc += offset.xy;
}
{
tmp += weights[6].x * texture2D(img, loc);
loc += offset.xy;
tmp += weights[6].y * texture2D(img, loc);
loc += offset.xy;
tmp += weights[6].z * texture2D(img, loc);
loc += offset.xy;
tmp += weights[6].w * texture2D(img, loc);
loc += offset.xy;
}
{
tmp += weights[7].x * texture2D(img, loc);
loc += offset.xy;
tmp += weights[7].y * texture2D(img, loc);
loc += offset.xy;
tmp += weights[7].z * texture2D(img, loc);
loc += offset.xy;
tmp += weights[7].w * texture2D(img, loc);
loc += offset.xy;
}
{
tmp += weights[8].x * texture2D(img, loc);
loc += offset.xy;
tmp += weights[8].y * texture2D(img, loc);
loc += offset.xy;
tmp += weights[8].z * texture2D(img, loc);
loc += offset.xy;
tmp += weights[8].w * texture2D(img, loc);
loc += offset.xy;
}
{
tmp += weights[9].x * texture2D(img, loc);
loc += offset.xy;
tmp += weights[9].y * texture2D(img, loc);
loc += offset.xy;
tmp += weights[9].z * texture2D(img, loc);
loc += offset.xy;
tmp += weights[9].w * texture2D(img, loc);
loc += offset.xy;
}
{
tmp += weights[10].x * texture2D(img, loc);
loc += offset.xy;
tmp += weights[10].y * texture2D(img, loc);
loc += offset.xy;
tmp += weights[10].z * texture2D(img, loc);
loc += offset.xy;
tmp += weights[10].w * texture2D(img, loc);
loc += offset.xy;
}
{
tmp += weights[11].x * texture2D(img, loc);
loc += offset.xy;
tmp += weights[11].y * texture2D(img, loc);
loc += offset.xy;
tmp += weights[11].z * texture2D(img, loc);
loc += offset.xy;
tmp += weights[11].w * texture2D(img, loc);
loc += offset.xy;
}
{
tmp += weights[12].x * texture2D(img, loc);
loc += offset.xy;
tmp += weights[12].y * texture2D(img, loc);
loc += offset.xy;
tmp += weights[12].z * texture2D(img, loc);
loc += offset.xy;
tmp += weights[12].w * texture2D(img, loc);
loc += offset.xy;
}
{
tmp += weights[13].x * texture2D(img, loc);
loc += offset.xy;
tmp += weights[13].y * texture2D(img, loc);
loc += offset.xy;
tmp += weights[13].z * texture2D(img, loc);
loc += offset.xy;
tmp += weights[13].w * texture2D(img, loc);
loc += offset.xy;
}
{
tmp += weights[14].x * texture2D(img, loc);
loc += offset.xy;
tmp += weights[14].y * texture2D(img, loc);
loc += offset.xy;
tmp += weights[14].z * texture2D(img, loc);
loc += offset.xy;
tmp += weights[14].w * texture2D(img, loc);
loc += offset.xy;
}
{
tmp += weights[15].x * texture2D(img, loc);
loc += offset.xy;
tmp += weights[15].y * texture2D(img, loc);
loc += offset.xy;
tmp += weights[15].z * texture2D(img, loc);
loc += offset.xy;
tmp += weights[15].w * texture2D(img, loc);
loc += offset.xy;
}
gl_FragColor = tmp;
}
