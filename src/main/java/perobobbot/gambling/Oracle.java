package perobobbot.gambling;

import lombok.NonNull;

public interface Oracle {

    @NonNull OraclePredication predict();

    static @NonNull Oracle createCoinFlipOracle() {
        return new CoinFlipOracle();
    }

}
