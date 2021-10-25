package perobobbot.gambling;


import com.google.common.collect.ImmutableSet;
import jplugman.api.Requirement;
import perobobbot.extension.ExtensionPlugin;

/**
 * This is the entry point of the plugin (at the jplugman level).
 * <p>
 * For jplugman, the plugin provides a service which in the case
 * of the bot, is an ExtensionPlugin (which might be confusing, sorry).
 * <p>
 * An ExtensionPlugin is a plugin for the Bot to add an extension to itself.
 */
public class JPlugin extends ExtensionPlugin {

    public static final ImmutableSet<Requirement<?>> REQUIREMENTS = ImmutableSet.of(
            Requirements.IO,
            Requirements.BANK_SERVICE,
            Requirements.VIEWER_IDENTITY_SERVICE
    );


    public JPlugin() {
        super(GamblingExtensionPlugin::new,REQUIREMENTS);
    }
}
