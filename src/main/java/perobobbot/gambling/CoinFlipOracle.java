package perobobbot.gambling;

import lombok.NonNull;

import java.util.Random;

public class CoinFlipOracle implements Oracle
{
    private final @NonNull Random random = new Random();

    @Override
    public @NonNull OraclePredication predict() {
        final var winning= random.nextBoolean();
        return new OraclePredication(winning,0.5);
    }
}
