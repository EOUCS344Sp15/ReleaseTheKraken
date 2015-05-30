#ifdef GL_ES
    precision mediump float;
#endif
 
uniform sampler2D u_texture;
uniform sampler2D u_mask;
 
varying vec4 v_color;
varying vec2 v_texCoords; //The pixel coordinates on the texture for this fragment
 
void main()
{    
    vec4 texColor = texture2D(u_texture, v_texCoords);
    vec4 mask = texture2D(u_mask, v_texCoords);
    texColor.r += mask.r;
    texColor.g += mask.g;
    texColor.b += mask.b;
    texColor.a += mask.a;
    gl_FragColor = v_color * texColor;
    
    /*
        BUG: This is applying to the tilemap texture, not the rendered tilemap
    */
}