package perobobbot.gambling;

import lombok.NonNull;
import perobobbot.lang.ChatConnectionInfo;
import perobobbot.lang.ExecutionContext;
import perobobbot.lang.Platform;

public sealed interface Gambling permits GamblingWithAmount, GambledAll {

    @NonNull ExecutionContext executionContext();

    default @NonNull String getMessageOwnerId() {
        return executionContext().getMessageOwner().getUserId();
    }

    default @NonNull Platform getPlatform() {
        return executionContext().getPlatform();
    }

    default @NonNull String getChannelName() {
        return executionContext().getChannelName();
    }

    default String getMessageOwnerName() {
        return executionContext().getMessageOwner().getHighlightedUserName();
    }

    default ChatConnectionInfo getChatConnectionInfo() {
        return executionContext().getChatConnectionInfo();
    }
}
