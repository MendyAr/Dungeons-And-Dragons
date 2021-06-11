package BusinessLayer.util;

public class DeterministicRNG implements RandomNumberGenerator{

    private int result;

    public static RandomNumberGenerator getInstance(int result) {
        return new DeterministicRNG(result);
    }

    private DeterministicRNG(int result){
        this.result = result;
    }

    @Override
    public Integer generate(int lowBound, int highBound) {
        return result;
    }

    public void setResult(int result){
        this.result = result;
    }
}
