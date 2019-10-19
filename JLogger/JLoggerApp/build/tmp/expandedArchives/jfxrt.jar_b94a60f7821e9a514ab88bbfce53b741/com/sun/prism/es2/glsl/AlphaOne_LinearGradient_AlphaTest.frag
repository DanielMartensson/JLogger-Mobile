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
uniform sampler2D colors;
uniform vec4 content;
lowp vec4 paint(vec2 texCoord) {
vec2 fractvals = vec2(fract(texCoord.x), clamp(texCoord.x, 0.0, 1.0));
vec2 clrCoord = vec2(content.x + dot(fractvals, content.zw), content.y);
return texture2D(colors, clrCoord);
}
void main() {
gl_FragColor = paint(texCoord1) * perVertexColor;
if (gl_FragColor.a == 0.0)discard;}
