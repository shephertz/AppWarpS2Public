package rummydemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utils {
    /*
     * This function accept three card (id) and check if they are of same type
     */
    public static boolean checkForSameType(List<Integer> list){
        Collections.sort(list); 
        int c1 = list.get(0);
        int c2 = list.get(1);
        int c3 = list.get(2);
        if((c2==(c1+13) || c2==(c1+26) || c2==(c1+39)) && (c3==(c1+13) || c3==(c1+26) || c3==(c1+39))){
            return true;
        }
        return false;
    }
    
    /*
     * This function accept three card (id) and check if they are in sequence
     */
    public static boolean checkForSuquence(List<Integer> list){
        int c1 = list.get(0);
        int c2 = list.get(1);
        int c3 = list.get(2);
        if((c1!=12 || c1!=25 || c1!=38 || c1!=51) && c1%13!=0){
            if(c2==(c1+1) && c3==(c1+2)){
                return true;
            }
        }else if(c1==12 || c1==25 || c1==38 || c1==51){
            int r = 0;
            if(c1>12){
                r = c1/12;
                r--;
            }
            if(c2==(c1+1) && c3==1+(r*13)){
                return true;
            }
            
        }
        else if(c1%13==0){// correct
            int r = c1/13;
            r--;
            if(c2==1+(r*13) && c3==2+(r*13)){
                return true;
            }
        }
        return false;
    }
    
    public static boolean checkForWin(ArrayList<Integer> list){
        int winCounter = 0;
        int startIndex = 0;
        for(int i=0;i<3;i++){
            List<Integer> subList = list.subList(startIndex, startIndex+3);
            if((checkForSameType(subList))||(checkForSuquence(subList))){
                winCounter++;
            }
            startIndex +=3;
        }
        if(list.size()==6 && winCounter==2){// three user case
            return true;
        }
        if(list.size()==9 && winCounter==3){// two user case
            return true;
        }
        return false;
    }
}