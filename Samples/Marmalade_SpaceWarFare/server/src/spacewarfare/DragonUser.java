/**
 *
 * @author Suyash Mohan
 */

package spacewarfare;

import java.util.Random;

public class DragonUser{
    private int x,y;
    private int health;
    private Random rnd;
    private int step;
    
    public DragonUser() {
        x = y = 0;
        health = 20;
        step = 100;
        rnd = new Random();
    }
    
    public void SetPosition(int px, int py){
        x = px;
        y = py;
    }
    
    public int GetX(){
        return x;
    }
    
    public int GetY(){
        return y;
    }
    
    public int GetHealth(){
        return health;
    }
    
    public int ReduceHealth(){
        if(health > 0)
            health = health - 1;
        return health;
    }
    
    /*
     * perform a random move
     * minx, miny, maxx and maxy defines the bounding box, 
     * within which move is generated
     */
    public void MoveRandomStep(int minx,int miny, int maxx, int maxy){
        switch(rnd.nextInt(4)){
                case 0:
                    x += step;
                    break;
                case 1: 
                    x -= step;
                    break;
                case 2: 
                    y += step;
                    break;
                case 3: 
                    y -= step;
                    break;    
            }
            
            if(x < minx)
                x = minx;
            if(y < miny)
                y = miny;
            if(x > maxx)
                x = maxx;
            if(y > maxy)
                y = maxy;
    }
    
    public void Spawn( int _health){
        health = _health;
    }
}
