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
lowp float mask(vec2 tco, vec2 dim) {
float lensq = dot(tco, tco);
return clamp(0.5 * (dim.x + 1.0 - (lensq - 0.25) / dim.x), 0.0, dim.y);
}
void main() {
gl_FragColor = mask(texCoord0, texCoord1) * perVertexColor;
if (gl_FragColor.a == 0.0)discard;}
