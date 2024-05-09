package io.github.thatsmusic99.athena.commands;

import io.github.thatsmusic99.athena.AthenaCore;
import io.github.thatsmusic99.athena.util.EventCache;
import io.github.thatsmusic99.athena.util.RemappingUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@io.github.thatsmusic99.athena.commands.Command(
        name = "listen",
        permission = "athena.command.listen",
        description = "Remaps all event listeners to debug them.",
        usage = "/athena listen <Event Name|Class>"
)
public class ListenCommand implements IAthenaCommand {

    private static final boolean DEFAULT_DIFFERENCES_ONLY = true;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        if (args.length == 1) {
            AthenaCore.sendFailMessage(sender, "You need to specify an event to listen to!");
            return true;
        }
        String eventName = args[1];
        Class<? extends Event> eventClass;
        if (EventCache.get().getEventClass(eventName) != null) {
            eventClass = EventCache.get().getEventClass(eventName);
        } else {
            try {
                Class<?> tempClass = Class.forName(args[1]);
                if (!Event.class.isAssignableFrom(tempClass)) {
                    AthenaCore.sendFailMessage(sender, "You need to specify an event to listen to!");
                    return true;
                }
                eventClass = (Class<? extends Event>) tempClass;
                EventCache.get().addEvent(eventClass);
            } catch (ClassNotFoundException e) {
                AthenaCore.sendFailMessage(sender, "There is no such event with this name! Please include the package" +
                        " name as well.");
                return true;
            }
        }

        boolean differencesOnly = args.length > 2 ? args[2].equalsIgnoreCase("true") : DEFAULT_DIFFERENCES_ONLY;
        RemappingUtil.get().remapEvent(eventClass, sender, differencesOnly);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] args) {
        if (args.length == 3) {
            return List.of("true", "false");
        }
        final List<String> results = new ArrayList<>();
        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], EventCache.get().getEventNames(), results);
        }
        return results;
    }
}
