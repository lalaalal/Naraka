package com.yummy.naraka.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.world.entity.data.LockedHealthHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class LockHealthCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("lockhealth")
                .then(Commands.literal("lock")
                        .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                .executes(context -> lockHealth(
                                        context.getSource(),
                                        context.getSource().getEntityOrException(),
                                        IntegerArgumentType.getInteger(context, "value")
                                ))
                        )
                )
                .then(Commands.literal("remove")
                        .executes(context -> removeLockHealth(
                                context.getSource(),
                                context.getSource().getEntityOrException()
                        ))
                )
                .then(Commands.argument("target", EntityArgument.entity())
                        .then(Commands.literal("lock")
                                .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                        .executes(context -> lockHealth(
                                                context.getSource(),
                                                EntityArgument.getEntity(context, "target"),
                                                IntegerArgumentType.getInteger(context, "value")
                                        ))
                                )
                        )
                        .then(Commands.literal("remove")
                                .executes(context -> removeLockHealth(
                                        context.getSource(),
                                        EntityArgument.getEntity(context, "target")
                                ))
                        )
                )
        );
    }

    private static int lockHealth(CommandSourceStack source, Entity target, int value) throws CommandSyntaxException {
        LivingEntity livingEntity = NarakaCommands.getLivingEntity(target);
        LockedHealthHelper.lock(livingEntity, value);
        source.sendSuccess(() -> Component.translatable(LanguageKey.LOCK_HEALTH_COMMAND_LOCK_KEY, livingEntity.getName(), value), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int removeLockHealth(CommandSourceStack source, Entity target) throws CommandSyntaxException {
        LivingEntity livingEntity = NarakaCommands.getLivingEntity(target);
        LockedHealthHelper.release(livingEntity);
        source.sendSuccess(() -> Component.translatable(LanguageKey.LOCK_HEALTH_COMMAND_REMOVE_KEY, livingEntity.getName()), false);
        return Command.SINGLE_SUCCESS;
    }
}
