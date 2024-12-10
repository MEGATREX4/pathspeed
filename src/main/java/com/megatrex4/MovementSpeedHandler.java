package com.megatrex4;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;

import java.util.UUID;

public class MovementSpeedHandler {
    private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("f17e3b4b-56d9-4c45-91b5-b1e758db028b");
    private static final Identifier SPEED_MODIFIER_ID = Identifier.of("pathspeed", "block_speed_modifier");
    private static Identifier lastBlockId = null;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
                adjustPlayerSpeed(player);
            }
        });
    }

    private static void adjustPlayerSpeed(PlayerEntity player) {
        World world = player.getEntityWorld();
        BlockPos pos = player.getBlockPos();

        BlockPos blockPosBelow = pos.down();
        if (!world.getBlockState(pos).getCollisionShape(world, pos).isEmpty()) {
            blockPosBelow = pos;
        }

        Identifier blockId = Registries.BLOCK.getId(world.getBlockState(blockPosBelow).getBlock());
        if (!blockId.equals(lastBlockId)) {
            resetPlayerSpeed(player);

            double blockSpeedMultiplier = PathSpeed.getSpeedMultiplier(blockId);
            double speed = Math.max(0.1 * blockSpeedMultiplier, 1.0 / 20.0);

            EntityAttributeInstance speedAttribute = player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            if (speedAttribute != null) {
                double speedAdjustment = speed - 0.1;

                EntityAttributeModifier modifier = new EntityAttributeModifier(SPEED_MODIFIER_ID, speedAdjustment, Operation.ADD_MULTIPLIED_TOTAL);
                speedAttribute.addPersistentModifier(modifier);
            }

            lastBlockId = blockId;
        }
    }

    private static void resetPlayerSpeed(PlayerEntity player) {
        EntityAttributeInstance speedAttribute = player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (speedAttribute != null) {
            EntityAttributeModifier modifier = speedAttribute.getModifier(SPEED_MODIFIER_ID);
            if (modifier != null) {
                speedAttribute.removeModifier(modifier);
            }
        }
    }

}
