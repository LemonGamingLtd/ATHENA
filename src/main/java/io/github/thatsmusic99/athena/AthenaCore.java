package io.github.thatsmusic99.athena;

import io.github.thatsmusic99.athena.commands.AthenaCommand;
import io.github.thatsmusic99.athena.util.EventCache;
import io.github.thatsmusic99.athena.util.RemappingUtil;
import me.nahu.scheduler.wrapper.FoliaWrappedJavaPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.CommandSender;

public class AthenaCore extends FoliaWrappedJavaPlugin {

    private static AthenaCore instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Successfully initialized scheduler wrapper of type: " + getScheduler().getImplementationType());

        getCommand("athena").setExecutor(new AthenaCommand());
        try {
            new Metrics(this, 12408);
        } catch (NoClassDefFoundError ignored) {
        }

        new RemappingUtil();

        getScheduler().runTaskAsynchronously(EventCache::new);
    }

    public static AthenaCore get() {
        return instance;
    }

    public static Component getPrefix() {
        return Component.text("ATHENA ", TextColor.color(0x9ED0FF))
            .append(Component.text("» ", NamedTextColor.DARK_GRAY));
    }

    public static TextColor getInfoColour() {
        return TextColor.color(0x9ED0FF);
    }

    public static TextColor getSuccessColour() {
        return TextColor.color(0xD1E9FF);
    }

    public static TextColor getFailColour() {
        return TextColor.color(0xFCC0B3);
    }

    public static TextColor getNoticeColour() {
        return TextColor.color(0xC2FFBB);
    }

    public static TextColor getNoticeColourDark() {
        return TextColor.color(0x7AF47A);
    }

    public static void sendSuccessMessage(CommandSender sender, String message) {
        sendSuccessMessage(sender, Component.text(message, AthenaCore.getSuccessColour()));
    }

    public static void sendSuccessMessage(CommandSender sender, Component component) {
        sender.sendMessage(AthenaCore.getPrefix().append(component));
    }

    public static void sendFailMessage(CommandSender sender, String message) {
        sender.sendMessage(AthenaCore.getPrefix().append(Component.text(message, AthenaCore.getFailColour())));
    }

    public static void sendNoticeMessage(CommandSender sender, String message) {
        sendNoticeMessage(sender, Component.text(message, AthenaCore.getNoticeColour()));
    }

    public static void sendNoticeMessage(CommandSender sender, Component component) {
        sender.sendMessage(AthenaCore.getPrefix().append(component));
    }
}
