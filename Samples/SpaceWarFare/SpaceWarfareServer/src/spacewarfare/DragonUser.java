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
    
    public DragonUser() {
        x = y = 0;
        health = 20;
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
    
    public void MoveRandomStep(int minx,int miny, int maxx, int maxy){
        switch(rnd.nextInt(4)){
                case 0:
                    x += 25;
                    break;
                case 1: 
                    x -= 25;
                    break;
                case 2: 
                    y += 25;
                    break;
                case 3: 
                    y -= 25;
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
