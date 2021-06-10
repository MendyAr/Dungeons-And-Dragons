package BusinessLayer.util;

public class Cooldown {
    //fields
    private final int cdLength;
    private int cdState;

    public Cooldown(int cdLength){
        this.cdLength = cdLength;
        init();
    }

    public int getCdLength() {
        return cdLength;
    }

    public int getCdState() {
        return cdState;
    }

    public void init(){
        cdState = 0;
    }

    public void tick(){
        cdState = Math.max(cdState -1, 0);
    }

    public boolean isReady(){
        return cdState == 0;
    }

    public void use(){
        if (!isReady())
            throw new IllegalStateException(String.valueOf(cdState));
        cdState = cdLength;
    }

    @Override
    public String toString() {
        return String.format("%d%c%d", cdState, '/' , cdLength);
    }
}
