package BusinessLayer.util;

public class DeterministicRNG implements RandomNumberGenerator{
    private static int result;
    private static RandomNumberGenerator rng;

    private DeterministicRNG(int result){
        DeterministicRNG.result = result;
    }

    @Override
    public Integer generate(int lowBound, int highBound) {
        return result;
    }

    public static RandomNumberGenerator getInstance(int result) {
        if (rng == null)
            rng = new DeterministicRNG(result);
        return rng;
    }
}
