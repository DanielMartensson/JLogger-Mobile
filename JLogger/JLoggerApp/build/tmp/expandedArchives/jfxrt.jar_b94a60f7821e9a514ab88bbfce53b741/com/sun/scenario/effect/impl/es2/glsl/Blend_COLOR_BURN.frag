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
uniform sampler2D botImg;
uniform sampler2D topImg;
uniform float opacity;
vec4 blend_color_burn(vec4 bot, vec4 top) {
vec4 res;
res.a = bot.a + top.a - (bot.a * top.a);
res.rgb = (1.0 - top.a) * bot.rgb + (1.0 - bot.a) * top.rgb;
float proda = bot.a * top.a;
float topa2 = top.a * top.a;
vec3 tmp;
if (bot.a == bot.r){
tmp.r = proda;
}
 else if (top.r == 0.0){
tmp.r = 0.0;
}
 else {
tmp.r = topa2 * (bot.a - bot.r) / top.r;
if (tmp.r >= proda){
tmp.r = 0.0;
}
 else {
tmp.r = proda - tmp.r;
}
}
if (bot.a == bot.g){
tmp.g = proda;
}
 else if (top.g == 0.0){
tmp.g = 0.0;
}
 else {
tmp.g = topa2 * (bot.a - bot.g) / top.g;
if (tmp.g >= proda){
tmp.g = 0.0;
}
 else {
tmp.g = proda - tmp.g;
}
}
if (bot.a == bot.b){
tmp.b = proda;
}
 else if (top.b == 0.0){
tmp.b = 0.0;
}
 else {
tmp.b = topa2 * (bot.a - bot.b) / top.b;
if (tmp.b >= proda){
tmp.b = 0.0;
}
 else {
tmp.b = proda - tmp.b;
}
}
res.rgb = res.rgb + tmp;
return res;
}
void main() {
vec4 bot = texture2D(botImg, texCoord0);
vec4 top = texture2D(topImg, texCoord1) * opacity;
gl_FragColor = blend_color_burn(bot, top);
}
