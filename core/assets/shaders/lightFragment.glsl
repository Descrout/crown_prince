#ifdef GL_ES
precision mediump float;
#endif

#define NUM_LIGHTS 32

const float constant = 1.0;
const float linear = 0.09;
const float quadratic = 0.032;

struct Light{
    vec2 position;
    vec3 diffuse;
    float power;
};


varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;

uniform Light lights[NUM_LIGHTS];
uniform int num_lights;
uniform vec2 screen;


void main() {
    vec4 color = texture2D(u_sampler2D, v_texCoord0);

    vec2 norm_screen = gl_FragCoord.xy / screen;

    vec3 diffuse = vec3(0.1f,0.1f,0.1f);

    for(int i = 0; i < num_lights; i++){
        Light light = lights[i];
        vec2 norm_pos = light.position / screen;

        float distance = length(norm_pos - norm_screen) * light.power;
        float attenuation = 1.0 / (constant + linear * distance + quadratic * (distance*distance));

        diffuse += light.diffuse;
    }

    diffuse = clamp(diffuse, 0f, 1f);

    gl_FragColor = color * vec4(diffuse,1.0f);
}
