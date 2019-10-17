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
varying lowp vec4 perVertexColor;
void main() {
gl_FragColor = perVertexColor;
if (gl_FragColor.a == 0.0)discard;}
