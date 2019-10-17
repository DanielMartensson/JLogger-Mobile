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
uniform vec2 idim;
float ellipsemask(vec2 abstco, vec2 invarcradii) {
vec2 absecctco = abstco * invarcradii;
float ecclensq = dot(absecctco, absecctco);
float delta = dot(absecctco, invarcradii) * 2.0;
return clamp(0.5 + (1.0 - ecclensq) / delta, 0.0, 1.0);
}
lowp float mask(vec2 tco, vec2 odim) {
vec2 abstco = abs(tco);
float ocov = ellipsemask(abstco, odim);
float icov = ellipsemask(abstco, idim);
return clamp(ocov - icov, 0.0, 1.0);
}
void main() {
gl_FragColor = mask(texCoord0, texCoord1) * perVertexColor;
}
