package com.yummy.naraka.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.yummy.naraka.init.CommandRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class NarakaCommands {
    private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(
            object -> Component.translatableEscape("commands.attribute.failed.entity", object)
    );

    public static void initialize() {
        CommandRegistry.register(StigmaCommand::register);
        CommandRegistry.register(LockHealthCommand::register);
    }

    public static LivingEntity getLivingEntity(Entity entity) throws CommandSyntaxException {
        if (entity instanceof LivingEntity livingEntity)
            return livingEntity;
        throw ERROR_NOT_LIVING_ENTITY.create(entity.getName());
    }
}
