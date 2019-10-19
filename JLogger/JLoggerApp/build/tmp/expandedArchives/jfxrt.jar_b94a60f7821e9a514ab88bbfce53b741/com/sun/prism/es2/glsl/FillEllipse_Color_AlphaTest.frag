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
lowp float mask(vec2 ecctco, vec2 invarcradii) {
float ecclensq = dot(ecctco, ecctco);
float pix = dot(abs(ecctco), invarcradii);
return clamp(0.5 + (1.0 + 0.25 * pix * pix - ecclensq) / (2.0 * pix), 0.0, 1.0);
}
void main() {
gl_FragColor = mask(texCoord0, texCoord1) * perVertexColor;
if (gl_FragColor.a == 0.0)discard;}
