package com.megatrex4;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MovementSpeedHandler {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
                adjustPlayerSpeed(player);
            }
        });
    }

    private static Identifier lastBlockId = null;
    private static double lastSpeedAdjustment = 0.0;

    private static void adjustPlayerSpeed(PlayerEntity player) {
        World world = player.getEntityWorld();
        BlockPos pos = player.getBlockPos();

        BlockPos blockPosBelow = pos.down();

        if (!world.getBlockState(pos).getCollisionShape(world, pos).isEmpty()) {
            blockPosBelow = pos;
        }

        Identifier blockId = Registries.BLOCK.getId(world.getBlockState(blockPosBelow).getBlock());

        if (!blockId.equals(lastBlockId)) {
            if (lastBlockId != null) {
                resetPlayerSpeed(player);
            }

            double blockSpeedMultiplier = PathSpeed.getSpeedMultiplier(blockId);

            double speed = Math.max(0.1 * blockSpeedMultiplier, 1.0 / 20.0);

            EntityAttributeInstance speedAttribute = player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            if (speedAttribute != null) {
                double newSpeedAdjustment = speed - 0.1;
                speedAttribute.setBaseValue(speedAttribute.getBaseValue() + newSpeedAdjustment);
                lastSpeedAdjustment = newSpeedAdjustment;
            }

            lastBlockId = blockId;
        }
    }


    private static void resetPlayerSpeed(PlayerEntity player) {
        EntityAttributeInstance speedAttribute = player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (speedAttribute != null && lastSpeedAdjustment != 0.0) {
            speedAttribute.setBaseValue(speedAttribute.getBaseValue() - lastSpeedAdjustment);
        }
        lastSpeedAdjustment = 0.0;
    }

}