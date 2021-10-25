package perobobbot.gambling;

import jplugman.api.ServiceProvider;
import lombok.NonNull;
import perobobbot.chat.core.IO;
import perobobbot.data.com.NotEnoughPoints;
import perobobbot.data.service.BankService;
import perobobbot.data.service.BankTransaction;
import perobobbot.extension.ExtensionBase;
import perobobbot.lang.*;

import java.time.Duration;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class GamblingExtension extends ExtensionBase {

    public static final PointType POINT_TYPE = PointType.CREDIT;

    private final Oracle oracle;
    private final IO io;
    private final BankService bankService;

    private final Gamblings gamblings = new Gamblings();

    public GamblingExtension(@NonNull ServiceProvider serviceProvider) {
        super("Gambling");
        this.io = serviceProvider.getAnyService(Requirements.IO);
        this.bankService = serviceProvider.getAnyService(Requirements.BANK_SERVICE);
        this.oracle = Oracle.createCoinFlipOracle();
    }

    public void gamble(@NonNull Gambling gambling) {
        gamblings.addGamblingData(gambling, oracle.predict());
    }


    @Override
    protected void onEnabled() {
        super.onEnabled();
        gamblings.start();
    }

    @Override
    protected void onDisabled() {
        super.onDisabled();
        gamblings.requestStop();
    }


    private class Gamblings extends Looper {

        private static record GamblingData(@NonNull Gambling gambling, @NonNull OraclePredication oraclePredication) {
        }

        private final @NonNull BlockingDeque<GamblingData> gamblingDataQueue = new LinkedBlockingDeque<>();

        public void addGamblingData(@NonNull Gambling gambling, @NonNull OraclePredication oraclePredication) {
            this.gamblingDataQueue.add(new GamblingData(gambling, oraclePredication));
        }

        @Override
        protected @NonNull IterationCommand performOneIteration() throws Exception {
            final var gamblingData = gamblingDataQueue.take();
            final var gambling = gamblingData.gambling();
            final var oraclePredication = gamblingData.oraclePredication();

            final var safe = bankService.findSafe(gambling.getPlatform(), gambling.getMessageOwnerId(), gambling.getChannelName());
            final long amount = getGambleAmount(gambling, safe);

            if (amount > 0) {
                final var gain = oraclePredication.getGain(amount);

                try {
                    performTransaction(safe, amount, gain);
                    final var newCredits = bankService.getSafe(safe.getId()).getCredits().getOrDefault(POINT_TYPE, 0L);
                    warnViewer(gambling, gain, newCredits);
                } catch (NotEnoughPoints notEnoughPoints) {
                    io.send(gambling.getChatConnectionInfo(),gambling.getChannelName(), "Tu n'as pas assez de crédit "+gambling.getMessageOwnerName());
                }
            }

            return IterationCommand.CONTINUE;
        }

        private void performTransaction(@NonNull Safe safe, long gambledAmount, long gain) {
            if (gain<0) {
                createTransaction(safe,gambledAmount).complete();
            } else {
                addCredits(safe, gain);
            }
        }

        private long getGambleAmount(@NonNull Gambling gambling, @NonNull Safe safe) {
            if (gambling instanceof GamblingWithAmount gamblingWithAmount) {
                return gamblingWithAmount.amount();
            } else if (gambling instanceof GambledAll) {
                return safe.getCredits().getOrDefault(POINT_TYPE, 0L);
            }
            return 0L;
        }

        public @NonNull Transaction createTransaction(@NonNull Safe safe, long amount) {
            return new BankTransaction(bankService, safe.getId(), POINT_TYPE, amount, Duration.ofSeconds(2));
        }

        public void addCredits(@NonNull Safe safe, long amount) {
            bankService.addPoints(safe.getId(), POINT_TYPE, amount);
        }

        public void warnViewer(Gambling gambling, long gain, long newCredits) {
            final var message = createResultMessage(gambling.getMessageOwnerName(), gain, newCredits);
            io.send(gambling.getChatConnectionInfo(), gambling.getChannelName(), message);
        }

        private String createResultMessage(@NonNull String viewerName, long gain, long credits) {
            return "%s %s %d ! Tu as %s credits".formatted(viewerName, gain > 0 ? "a gagné" : "a perdu", Math.abs(gain), credits);
        }

    }
}
