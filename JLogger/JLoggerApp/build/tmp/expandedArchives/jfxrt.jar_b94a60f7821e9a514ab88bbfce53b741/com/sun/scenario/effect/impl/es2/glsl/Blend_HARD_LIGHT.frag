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
vec4 blend_hard_light(vec4 bot, vec4 top) {
vec4 res;
res.a = bot.a + top.a - (bot.a * top.a);
float halftopa = 0.5 * top.a;
if (top.r > halftopa){
res.r = top.r + bot.a * (top.r - top.a) - bot.r * (2.0 * top.r - top.a - 1.0);
}
 else {
res.r = 2.0 * bot.r * top.r + bot.r * (1.0 - top.a) + top.r * (1.0 - bot.a);
}
if (top.g > halftopa){
res.g = top.g + bot.a * (top.g - top.a) - bot.g * (2.0 * top.g - top.a - 1.0);
}
 else {
res.g = 2.0 * bot.g * top.g + bot.g * (1.0 - top.a) + top.g * (1.0 - bot.a);
}
if (top.b > halftopa){
res.b = top.b + bot.a * (top.b - top.a) - bot.b * (2.0 * top.b - top.a - 1.0);
}
 else {
res.b = 2.0 * bot.b * top.b + bot.b * (1.0 - top.a) + top.b * (1.0 - bot.a);
}
return res;
}
void main() {
vec4 bot = texture2D(botImg, texCoord0);
vec4 top = texture2D(topImg, texCoord1) * opacity;
gl_FragColor = blend_hard_light(bot, top);
}
