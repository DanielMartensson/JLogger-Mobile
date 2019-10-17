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
uniform sampler2D inputTex;
lowp vec4 paint(vec2 texCoord) {
return texture2D(inputTex, texCoord);
}
void main() {
gl_FragColor = paint(texCoord0) * perVertexColor;
}
