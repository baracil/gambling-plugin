package perobobbot.gambling;

import perobobbot.lang.ExecutionContext;

public final record GamblingWithAmount(ExecutionContext executionContext, long amount) implements Gambling {

}
