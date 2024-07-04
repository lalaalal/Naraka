package com.yummy.naraka.core;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.attachment.AttachmentSynchronizer;
import com.yummy.naraka.world.structure.PiecePlacement;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class NarakaRegistries {
    public static final ResourceKey<Registry<PiecePlacement>> PIECE_PLACEMENT = create("piece_placement");
    public static final ResourceKey<Registry<AttachmentSynchronizer>> ATTACHMENT_SYNCHRONIZER = create("attachment_synchronizer");

    private static <T> ResourceKey<Registry<T>> create(String name) {
        return ResourceKey.createRegistryKey(NarakaMod.location(name));
    }
}
