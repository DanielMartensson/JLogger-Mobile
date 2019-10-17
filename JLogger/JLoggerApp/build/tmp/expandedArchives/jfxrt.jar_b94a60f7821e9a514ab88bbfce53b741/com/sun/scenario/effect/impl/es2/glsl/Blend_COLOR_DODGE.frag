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
vec4 blend_color_dodge(vec4 bot, vec4 top) {
vec4 res;
res.a = bot.a + top.a - (bot.a * top.a);
res.rgb = (1.0 - top.a) * bot.rgb + (1.0 - bot.a) * top.rgb;
float proda = bot.a * top.a;
vec3 tmp;
if (bot.r == 0.0){
tmp.r = 0.0;
}
 else if (top.a == top.r){
tmp.r = proda;
}
 else {
tmp.r = top.a * top.a * bot.r / (top.a - top.r);
if (tmp.r > proda){
tmp.r = proda;
}
}
if (bot.g == 0.0){
tmp.g = 0.0;
}
 else if (top.a == top.g){
tmp.g = proda;
}
 else {
tmp.g = top.a * top.a * bot.g / (top.a - top.g);
if (tmp.g > proda){
tmp.g = proda;
}
}
if (bot.b == 0.0){
tmp.b = 0.0;
}
 else if (top.a == top.b){
tmp.b = proda;
}
 else {
tmp.b = top.a * top.a * bot.b / (top.a - top.b);
if (tmp.b > proda){
tmp.b = proda;
}
}
res.rgb = res.rgb + tmp;
return res;
}
void main() {
vec4 bot = texture2D(botImg, texCoord0);
vec4 top = texture2D(topImg, texCoord1) * opacity;
gl_FragColor = blend_color_dodge(bot, top);
}
