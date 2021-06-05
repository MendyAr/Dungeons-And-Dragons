package util;

public interface RandomNumberGenerator {

    public Integer generate(int lowBound, int highBound);

    public static RandomNumberGenerator getInstance() {
        return null;
    }
}
