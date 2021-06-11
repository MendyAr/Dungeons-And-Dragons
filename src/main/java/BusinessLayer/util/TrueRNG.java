package BusinessLayer.util;

public class TrueRNG implements RandomNumberGenerator{
    private static RandomNumberGenerator rng = null;

    @Override
    public Integer generate(int lowBound, int highBound) {
        return (int)(Math.random()*(highBound - lowBound + 1)) + lowBound;
    }

    public static RandomNumberGenerator getInstance() {
        if (rng == null)
            rng = new TrueRNG();
        return rng;
    }
}
