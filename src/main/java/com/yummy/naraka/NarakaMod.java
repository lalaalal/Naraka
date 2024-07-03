package com.yummy.naraka;

import com.yummy.naraka.attachment.NarakaAttachments;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.inventory.NarakaMenuTypes;
import com.yummy.naraka.world.item.NarakaCreativeModTabs;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.crafting.NarakaRecipeSerializers;
import com.yummy.naraka.world.item.crafting.NarakaRecipeTypes;
import com.yummy.naraka.world.structure.NarakaStructureTypes;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(NarakaMod.MOD_ID)
public class NarakaMod {
    public static final String MOD_ID = "naraka";

    public NarakaMod(IEventBus modEventBus, ModContainer modContainer) {
        NarakaItems.register(modEventBus);
        NarakaCreativeModTabs.register(modEventBus);
        NarakaBlocks.register(modEventBus);
        NarakaBlockEntityTypes.register(modEventBus);
        NarakaEntityTypes.register(modEventBus);
        NarakaAttachments.register(modEventBus);
        NarakaMenuTypes.register(modEventBus);
        NarakaRecipeTypes.register(modEventBus);
        NarakaRecipeSerializers.register(modEventBus);
        NarakaStructureTypes.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, NarakaConfig.SPEC);
    }

    public static ResourceLocation mcLocation(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    public static ResourceLocation neoLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath("neoforge", path);
    }

    /**
     * Returns mod's resource location
     *
     * @param path Resource path
     * @return {@linkplain ResourceLocation} with namespace {@linkplain NarakaMod#MOD_ID}
     */
    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static ResourceLocation location(String prefix, String path) {
        return location("%s/%s".formatted(prefix, path));
    }

    public static NarakaConfig config() {
        return NarakaConfig.getInstance();
    }

    public static NarakaContext context() {
        return NarakaContext.INSTANCE;
    }
}
