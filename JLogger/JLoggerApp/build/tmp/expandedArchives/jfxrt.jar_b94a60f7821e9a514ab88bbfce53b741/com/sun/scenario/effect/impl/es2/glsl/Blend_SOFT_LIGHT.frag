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
float dofx(float f, float sqrtf) {
float d;
if (f <= 0.25){
d = ((16.0 * f - 12.0) * f + 4.0) * f;
}
 else {
d = sqrtf;
}
return d;
}
vec4 blend_soft_light(vec4 bot, vec4 top) {
vec4 res;
res.a = bot.a + top.a - (bot.a * top.a);
vec3 bot_np = bot.rgb / bot.a;
vec3 top_np = top.rgb / top.a;
float sqrtf = sqrt(bot_np.r);
float dr = dofx(bot_np.r, sqrtf);
sqrtf = sqrt(bot_np.g);
float dg = dofx(bot_np.g, sqrtf);
sqrtf = sqrt(bot_np.b);
float db = dofx(bot_np.b, sqrtf);
if (bot.a == 0.0){
res.r = top.r;
}
 else if (top.a == 0.0){
res.r = bot.r;
}
 else if (top_np.r <= 0.5){
res.r = bot.r + (1.0 - bot.a) * top.r - top.a * bot.r * (1.0 - 2.0 * top_np.r) * (1.0 - bot_np.r);
}
 else {
res.r = bot.r + (1.0 - bot.a) * top.r + (2.0 * top.r - top.a) * (bot.a * dr - bot.r);
}
if (bot.a == 0.0){
res.g = top.g;
}
 else if (top.a == 0.0){
res.g = bot.g;
}
 else if (top_np.g <= 0.5){
res.g = bot.g + (1.0 - bot.a) * top.g - top.a * bot.g * (1.0 - 2.0 * top_np.g) * (1.0 - bot_np.g);
}
 else {
res.g = bot.g + (1.0 - bot.a) * top.g + (2.0 * top.g - top.a) * (bot.a * dg - bot.g);
}
if (bot.a == 0.0){
res.b = top.b;
}
 else if (top.a == 0.0){
res.b = bot.b;
}
 else if (top_np.b <= 0.5){
res.b = bot.b + (1.0 - bot.a) * top.b - top.a * bot.b * (1.0 - 2.0 * top_np.b) * (1.0 - bot_np.b);
}
 else {
res.b = bot.b + (1.0 - bot.a) * top.b + (2.0 * top.b - top.a) * (bot.a * db - bot.b);
}
return res;
}
void main() {
vec4 bot = texture2D(botImg, texCoord0);
vec4 top = texture2D(topImg, texCoord1) * opacity;
gl_FragColor = blend_soft_light(bot, top);
}
