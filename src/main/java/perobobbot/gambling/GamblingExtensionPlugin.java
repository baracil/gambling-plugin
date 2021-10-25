package perobobbot.gambling;


import jplugman.annotation.Extension;
import jplugman.api.Disposable;
import jplugman.api.ServiceProvider;
import lombok.Getter;
import lombok.NonNull;
import perobobbot.extension.PerobobbotExtensionPluginBase;
import perobobbot.plugin.PerobobbotPlugin;

@Getter
@Extension(point = PerobobbotPlugin.class, version = "1.0.0")
public class GamblingExtensionPlugin extends PerobobbotExtensionPluginBase {


    public GamblingExtensionPlugin(@NonNull ModuleLayer pluginLayer, @NonNull ServiceProvider serviceProvider) {
        super(new GamblingExtensionFactory(),pluginLayer, serviceProvider);
    }

}
