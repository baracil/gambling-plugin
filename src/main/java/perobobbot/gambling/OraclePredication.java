package perobobbot.gambling;

public record OraclePredication(boolean winner, double probabilityOfWinning) {

    public long getGain(long gambledAmount) {

        // 0 = p*Gain - (1-p)Mise
        // Gain = (1-p)/p Mise
        final var proba = clampProbability(probabilityOfWinning);
        if (winner) {
            return Math.round(gambledAmount*(1.0-proba)/proba);
        }
        return -gambledAmount;
    }

    private double clampProbability(double probability) {
        return Math.max(0.,Math.min(1., probability));
    }
}
