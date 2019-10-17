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
uniform sampler2D maskInput;
lowp float mask(vec2 texCoord) {
return texture2D(maskInput, texCoord).a;
}
uniform sampler2D inputTex;
uniform vec4 content;
lowp vec4 paint(vec2 texCoord) {
texCoord = fract(texCoord);
texCoord = vec2(content.x, content.y) + texCoord * vec2(content.z, content.w);
return texture2D(inputTex, texCoord);
}
void main() {
gl_FragColor = mask(texCoord0) * paint(texCoord1) * perVertexColor;
}
