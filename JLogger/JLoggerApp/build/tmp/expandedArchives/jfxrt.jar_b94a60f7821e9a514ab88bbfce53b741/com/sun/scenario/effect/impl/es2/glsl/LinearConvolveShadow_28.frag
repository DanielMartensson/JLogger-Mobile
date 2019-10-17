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
uniform vec4 shadowColor;
uniform vec4 weights[7];
void main() {
int i;
float sum = 0.0;
vec2 loc = texCoord0 + offset.zw;
{
sum += weights[0].x * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[0].y * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[0].z * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[0].w * texture2D(img, loc).a;
loc += offset.xy;
}
{
sum += weights[1].x * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[1].y * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[1].z * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[1].w * texture2D(img, loc).a;
loc += offset.xy;
}
{
sum += weights[2].x * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[2].y * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[2].z * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[2].w * texture2D(img, loc).a;
loc += offset.xy;
}
{
sum += weights[3].x * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[3].y * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[3].z * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[3].w * texture2D(img, loc).a;
loc += offset.xy;
}
{
sum += weights[4].x * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[4].y * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[4].z * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[4].w * texture2D(img, loc).a;
loc += offset.xy;
}
{
sum += weights[5].x * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[5].y * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[5].z * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[5].w * texture2D(img, loc).a;
loc += offset.xy;
}
{
sum += weights[6].x * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[6].y * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[6].z * texture2D(img, loc).a;
loc += offset.xy;
sum += weights[6].w * texture2D(img, loc).a;
loc += offset.xy;
}
sum = clamp(sum, 0.0, 1.0);
gl_FragColor = sum * shadowColor;
}
