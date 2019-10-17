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
uniform sampler2D imageTex;
uniform sampler2D maskTex;
lowp vec4 paint(vec2 imgCoord, vec2 maskCoord) {
return texture2D(imageTex, imgCoord) * texture2D(maskTex, maskCoord).a;
}
void main() {
gl_FragColor = paint(texCoord0, texCoord1) * perVertexColor;
}
