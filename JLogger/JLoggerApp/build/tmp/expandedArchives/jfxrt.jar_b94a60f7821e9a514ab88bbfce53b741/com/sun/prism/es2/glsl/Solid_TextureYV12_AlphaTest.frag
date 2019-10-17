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
varying lowp vec4 perVertexColor;
uniform sampler2D lumaTex;
uniform sampler2D crTex;
uniform sampler2D cbTex;
uniform sampler2D alphaTex;
uniform vec4 lumaAlphaScale;
uniform vec4 cbCrScale;
const float Y_ADJUST = 16.0 / 255.0;
lowp vec4 paint(vec2 texCoord) {
vec2 imgCoord;
float luma = 1.1644 * (texture2D(lumaTex, texCoord * lumaAlphaScale.xy).a - Y_ADJUST);
float cb = texture2D(cbTex, texCoord * cbCrScale.xy).a - 0.5;
float cr = texture2D(crTex, texCoord * cbCrScale.zw).a - 0.5;
vec4 RGBA;
RGBA.r = luma + (1.5966 * cr);
RGBA.g = luma - (0.392 * cb) - (0.8132 * cr);
RGBA.b = luma + (2.0184 * cb);
if (lumaAlphaScale.z > 0.0){
RGBA.a = texture2D(alphaTex, texCoord * lumaAlphaScale.zw).a;
RGBA.rgb *= RGBA.a;
}
 else {
RGBA.a = 1.0;
}
return RGBA;
}
void main() {
gl_FragColor = paint(texCoord0) * perVertexColor;
if (gl_FragColor.a == 0.0)discard;}
