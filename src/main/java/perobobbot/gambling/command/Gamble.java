package perobobbot.gambling.command;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perobobbot.chat.core.IO;
import perobobbot.command.CommandAction;
import perobobbot.command.CommandParsing;
import perobobbot.gambling.GambledAll;
import perobobbot.gambling.Gambling;
import perobobbot.gambling.GamblingExtension;
import perobobbot.gambling.GamblingWithAmount;
import perobobbot.lang.ExecutionContext;
import perobobbot.lang.fp.Function1;

import java.util.Optional;

@RequiredArgsConstructor
public class Gamble implements CommandAction {

    public static final String GAMBLED_AMOUNT = "amount";

    private final GamblingExtension extension;
    private final IO io;

    @Override
    public void execute(@NonNull CommandParsing parsing, @NonNull ExecutionContext context) {
        createGamblingData(parsing)
                .map(f -> f.apply(context))
                .ifPresentOrElse(extension::gamble, () -> handleInvalidInput(context));
    }

    private void handleInvalidInput(ExecutionContext context) {
        io.send(context.getChatConnectionInfo(), context.getChannelName(), "Soit un entier positif soit 'all' en paramètre %s !".formatted(context.getMessageOwner().getHighlightedUserName()));
    }

    private @NonNull Optional<Function1<ExecutionContext, Gambling>> createGamblingData(@NonNull CommandParsing parsing) {
        final var parameter = parsing.getParameter(GAMBLED_AMOUNT);

        if (parameter.equalsIgnoreCase("all")) {
            return Optional.of(GambledAll::new);
        }
        return parsing.findLongParameter(GAMBLED_AMOUNT)
                      .filter(i -> i > 0)
                      .map(amount -> context -> new GamblingWithAmount(context, amount));
    }
}
