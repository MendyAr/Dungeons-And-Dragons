package util;

public class DeterministicRNG implements RandomNumberGenerator{
    public static int result;
    private static RandomNumberGenerator rng;

    private DeterministicRNG(int result){
        this.result = result;
    }

    @Override
    public Integer generate(int lowBound, int highBound) {
        return result;
    }

    public static RandomNumberGenerator getInstance() {
        if (rng == null)
            rng = new DeterministicRNG(result);
        return rng;
    }
}
