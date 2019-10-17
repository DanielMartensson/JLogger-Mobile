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
uniform sampler2D origImg;
uniform sampler2D mapImg;
uniform vec4 sampletx;
uniform vec4 imagetx;
uniform float wrap;
void main() {
vec4 off = texture2D(mapImg, texCoord1);
vec2 loc = texCoord0 + sampletx.zw * (off.xy + sampletx.xy);
loc -= wrap * floor(loc);
loc = imagetx.xy + (loc * imagetx.zw);
gl_FragColor = texture2D(origImg, loc);
}
