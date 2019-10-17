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
float lensq_m_25 = dot(tco, tco) - 0.25;
float ocov = clamp(0.5 * (odim.x + 1.0 - lensq_m_25 / odim.x), 0.0, odim.y);
float icov = clamp(0.5 * (idim.x + 1.0 - lensq_m_25 / idim.x), 0.0, idim.y);
return ocov - icov;
}
void main() {
gl_FragColor = mask(texCoord0, texCoord1) * perVertexColor;
}
