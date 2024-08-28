package com.megatrex4;

import com.mojang.brigadier.Command;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class MovementSpeedCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("reloadMovementConfig")
                    .requires(source -> source.hasPermissionLevel(2))
                    .executes(context -> reloadConfig(context.getSource())));
        });
    }

    private static int reloadConfig(ServerCommandSource source) {
        PathSpeed pathSpeed = new PathSpeed();
        pathSpeed.loadOrCreateConfig();
        source.sendFeedback(() -> Text.translatable("SpeedPath.configReloaded"), true);
        return Command.SINGLE_SUCCESS;
    }
}