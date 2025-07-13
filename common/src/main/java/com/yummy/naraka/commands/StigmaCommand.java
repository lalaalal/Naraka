package com.yummy.naraka.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import com.yummy.naraka.world.entity.data.Stigma;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.Set;

public class StigmaCommand {


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("stigma")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("get")
                        .executes(context -> getStigma(
                                context.getSource(),
                                context.getSource().getEntityOrException()
                        ))
                        .then(Commands.argument("target", EntityArgument.entity())
                                .executes(context -> getStigma(
                                        context.getSource(),
                                        EntityArgument.getEntity(context, "target")
                                ))
                        )
                )
                .then(Commands.literal("set")
                        .then(Commands.argument("value", IntegerArgumentType.integer(0, Stigma.MAX_STIGMA))
                                .executes(context -> setStigma(
                                        context.getSource(),
                                        Set.of(context.getSource().getEntityOrException()),
                                        IntegerArgumentType.getInteger(context, "value"),
                                        false
                                ))
                        )
                        .then(Commands.argument("target", EntityArgument.entities())
                                .then(Commands.argument("value", IntegerArgumentType.integer(0, Stigma.MAX_STIGMA))
                                        .executes(context -> setStigma(
                                                context.getSource(),
                                                EntityArgument.getEntities(context, "target"),
                                                IntegerArgumentType.getInteger(context, "value"),
                                                false
                                        ))
                                        .then(Commands.argument("markTime", BoolArgumentType.bool())
                                                .executes(context -> setStigma(
                                                        context.getSource(),
                                                        EntityArgument.getEntities(context, "target"),
                                                        IntegerArgumentType.getInteger(context, "value"),
                                                        BoolArgumentType.getBool(context, "markTime")
                                                ))
                                        )
                                )
                        )
                )
                .then(Commands.literal("remove")
                        .executes(context -> removeStigma(
                                context.getSource(),
                                Set.of(context.getSource().getEntityOrException())
                        ))
                        .then(Commands.argument("target", EntityArgument.entities())
                                .executes(context -> removeStigma(
                                        context.getSource(),
                                        EntityArgument.getEntities(context, "target")
                                ))
                        )
                )
                .then(Commands.literal("consume")
                        .executes(context -> consumeStigma(
                                context.getSource(),
                                Set.of(context.getSource().getEntityOrException()),
                                context.getSource().getEntityOrException()
                        ))
                        .then(Commands.argument("target", EntityArgument.entities())
                                .executes(context -> consumeStigma(
                                        context.getSource(),
                                        EntityArgument.getEntities(context, "target"),
                                        context.getSource().getEntityOrException()
                                ))
                                .then(Commands.argument("cause", EntityArgument.entity())
                                        .executes(context -> consumeStigma(
                                                context.getSource(),
                                                EntityArgument.getEntities(context, "target"),
                                                EntityArgument.getEntity(context, "cause")
                                        ))
                                )
                        )
                )
                .then(Commands.literal("increase")
                        .executes(context -> increaseStigma(
                                context.getSource(),
                                Set.of(context.getSource().getEntityOrException()),
                                context.getSource().getEntityOrException()
                        ))
                        .then(Commands.argument("target", EntityArgument.entities())
                                .executes(context -> increaseStigma(
                                        context.getSource(),
                                        EntityArgument.getEntities(context, "target"),
                                        context.getSource().getEntityOrException()
                                ))
                                .then(Commands.argument("cause", EntityArgument.entities())
                                        .executes(context -> increaseStigma(
                                                context.getSource(),
                                                EntityArgument.getEntities(context, "target"),
                                                EntityArgument.getEntity(context, "cause")
                                        ))
                                )
                        )
                )

        );
    }

    private static int getStigma(CommandSourceStack source, Entity entity) throws CommandSyntaxException {
        LivingEntity livingEntity = NarakaCommands.getLivingEntity(entity);
        source.sendSuccess(() -> Component.translatable(LanguageKey.STIGMA_COMMAND_GET_KEY, livingEntity.getName(), StigmaHelper.get(livingEntity).value()), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int setStigma(CommandSourceStack source, Collection<? extends Entity> targets, int value, boolean markTime) throws CommandSyntaxException {
        int count = 0;
        for (Entity target : targets) {
            LivingEntity livingEntity = NarakaCommands.getLivingEntity(target);
            long lastMarkedTime = markTime ? livingEntity.level().getGameTime() : 0;
            EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.STIGMA.get(), new Stigma(value, lastMarkedTime));
            count += 1;
        }
        Component component = Component.translatable(LanguageKey.STIGMA_COMMAND_SET_KEY, count, value);
        source.sendSuccess(() -> component, false);
        return Command.SINGLE_SUCCESS;
    }

    private static int increaseStigma(CommandSourceStack source, Collection<? extends Entity> targets, Entity cause) throws CommandSyntaxException {
        int count = 0;
        for (Entity target : targets) {
            LivingEntity livingEntity = NarakaCommands.getLivingEntity(target);
            StigmaHelper.increaseStigma(source.getLevel(), livingEntity, cause);
            count += 1;
        }
        Component component = Component.translatable(LanguageKey.STIGMA_COMMAND_INCREASE_KEY, count, cause.getName());
        source.sendSuccess(() -> component, false);
        return Command.SINGLE_SUCCESS;
    }

    private static int consumeStigma(CommandSourceStack source, Collection<? extends Entity> targets, Entity cause) throws CommandSyntaxException {
        int succeed = 0;
        for (Entity target : targets) {
            LivingEntity livingEntity = NarakaCommands.getLivingEntity(target);
            Stigma stigma = StigmaHelper.get(livingEntity);
            if (stigma.value() > 0) {
                Stigma result = stigma.consume(source.getLevel(), livingEntity, cause);
                EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.STIGMA.get(), result);
                succeed += 1;
            }
        }
        Component component = Component.translatable(LanguageKey.STIGMA_COMMAND_CONSUME_KEY, succeed, cause.getName(), cause.getName());
        source.sendSuccess(() -> component, false);
        return Command.SINGLE_SUCCESS;
    }

    private static int removeStigma(CommandSourceStack source, Collection<? extends Entity> targets) throws CommandSyntaxException {
        int count = 0;
        for (Entity target : targets) {
            LivingEntity livingEntity = NarakaCommands.getLivingEntity(target);
            EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.STIGMA.get(), Stigma.ZERO);
            count += 1;
        }
        Component component = Component.translatable(LanguageKey.STIGMA_COMMAND_REMOVE_KEY, count);
        source.sendSuccess(() -> component, false);
        return Command.SINGLE_SUCCESS;
    }
}
