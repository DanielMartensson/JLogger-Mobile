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
lowp float mask(vec2 tco, vec2 odim) {
vec2 ocov = clamp(odim + 0.5 - abs(tco), 0.0, 1.0);
vec2 icov = clamp(idim + 0.5 - abs(tco), 0.0, 1.0);
ocov = min(ocov, odim * 2.0);
icov = min(icov, idim * 2.0);
return ocov.x * ocov.y - icov.x * icov.y;
}
void main() {
gl_FragColor = mask(texCoord0, texCoord1) * perVertexColor;
}
