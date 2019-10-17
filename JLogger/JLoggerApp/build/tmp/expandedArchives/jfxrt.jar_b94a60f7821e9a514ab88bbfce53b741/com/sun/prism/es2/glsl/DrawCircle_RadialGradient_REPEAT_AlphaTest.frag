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
varying vec2 texCoord1;
varying lowp vec4 perVertexColor;
uniform vec4 jsl_pixCoordOffset;
uniform vec2 idim;
lowp float mask(vec2 tco, vec2 odim) {
float lensq_m_25 = dot(tco, tco) - 0.25;
float ocov = clamp(0.5 * (odim.x + 1.0 - lensq_m_25 / odim.x), 0.0, odim.y);
float icov = clamp(0.5 * (idim.x + 1.0 - lensq_m_25 / idim.x), 0.0, idim.y);
return ocov - icov;
}
const int MAX_FRACTIONS = 12;
const float TEXTURE_WIDTH = 16.0;
const float FULL_TEXEL_X = 1.0 / TEXTURE_WIDTH;
const float HALF_TEXEL_X = FULL_TEXEL_X / 2.0;
uniform vec4 fractions[12];
uniform sampler2D colors;
uniform float offset;
vec4 sampleGradient(float dist) {
int i;
float relFraction = 0.0;
{
relFraction += clamp((dist - fractions[0].x) * fractions[0].y, 0.0, 1.0);
}
{
relFraction += clamp((dist - fractions[1].x) * fractions[1].y, 0.0, 1.0);
}
{
relFraction += clamp((dist - fractions[2].x) * fractions[2].y, 0.0, 1.0);
}
{
relFraction += clamp((dist - fractions[3].x) * fractions[3].y, 0.0, 1.0);
}
{
relFraction += clamp((dist - fractions[4].x) * fractions[4].y, 0.0, 1.0);
}
{
relFraction += clamp((dist - fractions[5].x) * fractions[5].y, 0.0, 1.0);
}
{
relFraction += clamp((dist - fractions[6].x) * fractions[6].y, 0.0, 1.0);
}
{
relFraction += clamp((dist - fractions[7].x) * fractions[7].y, 0.0, 1.0);
}
{
relFraction += clamp((dist - fractions[8].x) * fractions[8].y, 0.0, 1.0);
}
{
relFraction += clamp((dist - fractions[9].x) * fractions[9].y, 0.0, 1.0);
}
{
relFraction += clamp((dist - fractions[10].x) * fractions[10].y, 0.0, 1.0);
}
float tc = HALF_TEXEL_X + (FULL_TEXEL_X * relFraction);
return texture2D(colors, vec2(tc, offset));
}
vec4 cycleNone(float dist) {
if (dist <= 0.0){
return texture2D(colors, vec2(0.0, offset));
}
 else if (dist >= 1.0){
return texture2D(colors, vec2(1.0, offset));
}
 else {
return sampleGradient(dist);
}
}
vec4 cycleReflect(float dist) {
dist = 1.0 - (abs(fract(dist * 0.5) - 0.5) * 2.0);
return sampleGradient(dist);
}
vec4 cycleRepeat(float dist) {
dist = fract(dist);
return sampleGradient(dist);
}
uniform vec4 m0;
uniform vec4 m1;
uniform vec3 precalc;
uniform vec3 perspVec;
lowp vec4 paint(vec2 winCoord) {
vec3 fragCoord = vec3(winCoord.x, winCoord.y, 1.0);
float wdist = dot(fragCoord, perspVec);
float x = dot(fragCoord, m0.xyz) / wdist + m0.w;
float y = dot(fragCoord, m1.xyz) / wdist + m1.w;
float xfx = x - precalc.x;
float dist = (precalc.x * xfx + sqrt(xfx * xfx + y * y * precalc.y)) * precalc.z;
return cycleRepeat(dist);
}
void main() {vec2 pixcoord = vec2(
    gl_FragCoord.x-jsl_pixCoordOffset.x,
    ((jsl_pixCoordOffset.z-gl_FragCoord.y)*jsl_pixCoordOffset.w)-jsl_pixCoordOffset.y);

gl_FragColor = mask(texCoord0, texCoord1) * paint(pixcoord) * perVertexColor;
if (gl_FragColor.a == 0.0)discard;}
