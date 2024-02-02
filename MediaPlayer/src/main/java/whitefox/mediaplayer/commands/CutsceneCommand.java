package whitefox.mediaplayer.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import whitefox.mediaplayer.network.PacketHandler;
import whitefox.mediaplayer.network.S2CCutscenePacket;

import java.util.Collection;

import static com.mojang.text2speech.Narrator.LOGGER;

public class CutsceneCommand {

    private final static SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.literal("The cutscene launch error"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("cutscene").requires((o) -> {
            return o.hasPermission(2);
        }).then(Commands.argument("id", StringArgumentType.string()).executes((o) -> {
            return cutscene(o.getSource(), StringArgumentType.getString(o, "id"), ImmutableList.of(o.getSource().getPlayerOrException()));
        }).then(Commands.argument("players", EntityArgument.players()).executes((o) -> {
            return cutscene(o.getSource(), StringArgumentType.getString(o, "id"), EntityArgument.getPlayers(o, "players"));
        }))));
    }

    public static int cutscene(CommandSourceStack stack, String id, Collection<? extends ServerPlayer> serverPlayers) throws CommandSyntaxException {
        try {
            for (ServerPlayer player : serverPlayers) {
                PacketHandler.sendTo(new S2CCutscenePacket(id), player);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
            throw ERROR_FAILED.create();
        }
        stack.sendSuccess(Component.literal("Started cutscene " + id + " to " + serverPlayers.size() + " players"), true);
        return serverPlayers.size();
    }
}
