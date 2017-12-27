/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quizcommon;

/**
 *
 * @author rahulwindows
 */
public class QuizResponse {
    private byte type;
    private int payLoadSize;
    private byte[] payLoad;
    
    public QuizResponse(byte type, int payLoadSize, byte[] payLoad) {
        this.type = type;
        this.payLoadSize = payLoadSize;
        this.payLoad = payLoad;
    }

    public byte[] getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(byte[] payLoad) {
        this.payLoad = payLoad;
    }

    public int getPayLoadSize() {
        return payLoadSize;
    }

    public void setPayLoadSize(int payLoadSize) {
        this.payLoadSize = payLoadSize;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}
