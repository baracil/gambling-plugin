package perobobbot.gambling;


import com.google.common.collect.ImmutableList;
import jplugman.api.ServiceProvider;
import lombok.NonNull;
import perobobbot.access.AccessRule;
import perobobbot.command.CommandDeclaration;
import perobobbot.extension.ExtensionFactory;
import perobobbot.gambling.command.Gamble;
import perobobbot.lang.Role;

import java.time.Duration;

public class GamblingExtensionFactory implements ExtensionFactory<GamblingExtension> {

    @NonNull
    @Override
    public GamblingExtension createExtension(@NonNull ModuleLayer pluginLayer, @NonNull ServiceProvider serviceProvider) {
        return new GamblingExtension(serviceProvider);
    }

    @Override
    public @NonNull ImmutableList<CommandDeclaration> createCommandDefinitions(@NonNull GamblingExtension extension, @NonNull ServiceProvider serviceProvider, CommandDeclaration.@NonNull Factory factory) {
        final var gambleAccessRule = AccessRule.create(Role.ANY_USER, Duration.ofSeconds(10), Role.ADMINISTRATOR.cooldown(Duration.ofSeconds(0)));
        return ImmutableList.of(
                factory.create("gamble {%s}".formatted(Gamble.GAMBLED_AMOUNT), gambleAccessRule, new Gamble(extension,serviceProvider.getAnyService(Requirements.IO)))
        );
    }
}
