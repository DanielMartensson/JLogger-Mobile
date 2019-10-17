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
lowp float mask(vec2 tco, vec2 tcc) {
vec2 cov = clamp(tcc + 0.5 - abs(tco), 0.0, 1.0);
cov = min(cov, tcc * 2.0);
return cov.x * cov.y;
}
void main() {
gl_FragColor = mask(texCoord0, texCoord1) * perVertexColor;
if (gl_FragColor.a == 0.0)discard;}
