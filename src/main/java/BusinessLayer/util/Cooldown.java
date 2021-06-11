package BusinessLayer.util;

public class Cooldown extends Resource{
    //fields

    public Cooldown(String name, int cdLength){
        super(name, cdLength);
        init();
    }

    @Override
    public void init(){
        setCurrent(0);
    }
}
