package com.yummy.naraka.forge.client;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.init.BuiltinResourcePackRegistry;
import com.yummy.naraka.forge.NarakaEventBus;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.resource.PathPackResources;

import java.nio.file.Path;
import java.util.function.Consumer;

public final class ForgeBuiltinResourcePackRegistry implements BuiltinResourcePackRegistry.Registrar, NarakaEventBus {
    @Override
    public void register(ResourceLocation resourcePackId, Component displayName) {
        String packDirectory = "resourcepacks/" + resourcePackId.getPath();
        NARAKA_BUS.addListener((Consumer<AddPackFindersEvent>) event -> {
            event.addRepositorySource(
                    packConsumer -> {
                        Path packPath = ModList.get().getModFileById(NarakaMod.MOD_ID).getFile()
                                .findResource(packDirectory);
                        Pack pack = Pack.readMetaAndCreate(
                                resourcePackId.getPath(),
                                displayName,
                                false,
                                id -> new PathPackResources(id, false, packPath),
                                PackType.CLIENT_RESOURCES,
                                Pack.Position.TOP,
                                PackSource.BUILT_IN
                        );
                        if (pack != null)
                            packConsumer.accept(pack);
                    }
            );
        });
    }
}
