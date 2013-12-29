/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package viking;

/**
 *
 * @author shephertz
 */
public class Robot {
    /*
     * start and end are the extreme points on the linear path, that robot will follow 
     */
    private Vector3f current, start, end;
    // by how much, we incerement the current position
    private float step;
    //direction in the linear path
    int direction;
    
    public Robot()
    {
        current = new Vector3f();
        start = new Vector3f();
        end = new Vector3f();
    }
    
    public void setPath(Vector3f _start, Vector3f _end, float _step)
    {
        start = _start;
        end = _end;
        current = new Vector3f(_start.x, _start.y, _start.z);
        
        step = _step;
        direction = 0;
    }
    
    public Vector3f getPosition()
    {
        return current;
    }
    
    /*
     * increment the x component of current position by step
     * depending on the direction and start and end points
     */
    public void moveStepX()
    {
        if(direction == 0)
        {
            if(current.x < end.x)
            {
                current.x += step;
            }
            else
            {
                direction = 1;
            }
        }
        else if(direction == 1)
        {
            if(current.x > start.x)
            {
                current.x -= step;
            }
            else
            {
                direction = 0;
            }
        }
    }
}
