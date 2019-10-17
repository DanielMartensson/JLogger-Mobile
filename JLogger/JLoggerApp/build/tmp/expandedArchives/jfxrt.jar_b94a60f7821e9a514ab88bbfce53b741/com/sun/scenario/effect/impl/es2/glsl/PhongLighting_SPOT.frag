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
uniform vec4 jsl_pixCoordOffset;
uniform sampler2D bumpImg;
uniform sampler2D origImg;
uniform float diffuseConstant;
uniform float specularConstant;
uniform float specularExponent;
uniform vec3 lightColor;
uniform vec4 kvals[8];
uniform float surfaceScale;
uniform vec3 lightPosition;
uniform vec3 normalizedLightDirection;
uniform float lightSpecularExponent;
void main() {vec2 pixcoord = vec2(
    gl_FragCoord.x-jsl_pixCoordOffset.x,
    ((jsl_pixCoordOffset.z-gl_FragCoord.y)*jsl_pixCoordOffset.w)-jsl_pixCoordOffset.y);

vec4 orig = texture2D(origImg, texCoord1);
int i;
vec3 sum = vec3(0.0, 0.0, 1.0);
{
sum.xy += kvals[0].zw * texture2D(bumpImg, texCoord0 + kvals[0].xy).a;
}
{
sum.xy += kvals[1].zw * texture2D(bumpImg, texCoord0 + kvals[1].xy).a;
}
{
sum.xy += kvals[2].zw * texture2D(bumpImg, texCoord0 + kvals[2].xy).a;
}
{
sum.xy += kvals[3].zw * texture2D(bumpImg, texCoord0 + kvals[3].xy).a;
}
{
sum.xy += kvals[4].zw * texture2D(bumpImg, texCoord0 + kvals[4].xy).a;
}
{
sum.xy += kvals[5].zw * texture2D(bumpImg, texCoord0 + kvals[5].xy).a;
}
{
sum.xy += kvals[6].zw * texture2D(bumpImg, texCoord0 + kvals[6].xy).a;
}
{
sum.xy += kvals[7].zw * texture2D(bumpImg, texCoord0 + kvals[7].xy).a;
}
vec3 N = normalize(sum);
float bumpA = texture2D(bumpImg, texCoord0).a;
vec3 tmp = vec3(pixcoord.x, pixcoord.y, surfaceScale * bumpA);
vec3 Lxyz = normalize(lightPosition - tmp);
float LdotS = dot(Lxyz, normalizedLightDirection);
LdotS = min(LdotS, 0.0);
vec3 Lrgb = lightColor * pow(-LdotS, lightSpecularExponent);
vec3 E = vec3(0.0, 0.0, 1.0);
vec3 H = normalize(Lxyz + E);
vec4 D;
D.rgb = diffuseConstant * dot(N, Lxyz) * Lrgb;
D.rgb = clamp(D.rgb, 0.0, 1.0);
D.a = 1.0;
vec4 S;
float NdotH = dot(N, H);
S.rgb = specularConstant * pow(NdotH, specularExponent) * Lrgb;
S.a = max(S.r, S.g);
S.a = max(S.a, S.b);
orig *= D;
S *= orig.a;
gl_FragColor = S + (orig * (1.0 - S.a));
}
