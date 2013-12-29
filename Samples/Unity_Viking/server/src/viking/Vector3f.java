/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package viking;

/**
 *
 * @author shephertz
 */

/*
 * Vector 3f
 * x, y and z
 */
public class Vector3f {
    public float x,y,z;
    public Vector3f()
    {
        x = y = z = 0.0f;
    }
    public Vector3f(float _x, float _y, float _z)
    {
        x = _x;
        y = _y;
        z = _z;
    }
}
