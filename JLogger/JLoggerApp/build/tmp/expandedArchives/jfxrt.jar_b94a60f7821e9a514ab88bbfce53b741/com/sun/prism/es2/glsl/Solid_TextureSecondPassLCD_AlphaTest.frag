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
uniform sampler2D dstColor;
uniform sampler2D glyphColor;
uniform vec3 gamma;
lowp vec4 paint(vec2 texCoord, vec2 texCoord2, vec4 jslVertexColor) {
float unitXCoord = gamma.z;
vec2 dTexCoord = texCoord;
vec4 glyphClr;
dTexCoord.x = texCoord.x - unitXCoord;
glyphClr.r = texture2D(glyphColor, dTexCoord).a;
glyphClr.g = texture2D(glyphColor, texCoord).a;
dTexCoord.x = texCoord.x + unitXCoord;
glyphClr.b = texture2D(glyphColor, dTexCoord).a;
const float third = 1.0 / 3.0;
glyphClr.a = dot(glyphClr.rgb, vec3(third, third, third));
if (glyphClr.a == 0.0){
discard;}
vec4 dstClr = texture2D(dstColor, texCoord2);
dstClr = pow(dstClr, vec4(gamma.y, gamma.y, gamma.y, gamma.y));
vec4 c = jslVertexColor;
vec4 glyphMix;
glyphMix.a = glyphClr.a;
glyphMix.rgb = mix(vec3(glyphClr.a, glyphClr.a, glyphClr.a), glyphClr.rgb, dstClr.a);
c = dstClr * (1.0 - glyphClr * c.a) + c * glyphMix;
c = pow(c, vec4(gamma.x, gamma.x, gamma.x, gamma.x));
return c;
}
void main() {
gl_FragColor = paint(texCoord0, texCoord1, perVertexColor);
if (gl_FragColor.a == 0.0)discard;}
