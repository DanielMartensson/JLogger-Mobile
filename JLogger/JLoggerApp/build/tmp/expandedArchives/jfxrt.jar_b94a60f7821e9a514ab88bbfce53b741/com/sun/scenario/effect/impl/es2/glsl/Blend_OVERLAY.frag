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
vec4 blend_overlay(vec4 bot, vec4 top) {
vec4 res;
res.a = bot.a + top.a - bot.a * top.a;
vec3 mask = ceil(bot.rgb - bot.a * 0.5);
vec3 adjbot = abs(bot.rgb - mask * bot.a);
vec3 adjtop = abs(top.rgb - mask * top.a);
res.rgb = (2.0 * adjbot + 1.0 - bot.a) * adjtop + (1.0 - top.a) * adjbot;
res.rgb = abs(res.rgb - mask * res.a);
return res;
}
void main() {
vec4 bot = texture2D(botImg, texCoord0);
vec4 top = texture2D(topImg, texCoord1) * opacity;
gl_FragColor = blend_overlay(bot, top);
}
