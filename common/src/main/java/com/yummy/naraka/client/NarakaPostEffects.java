package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class NarakaPostEffects {
    public static final Identifier MONOCHROME = NarakaMod.identifier("monochrome");
    public static final Identifier RYOIKI_GRAY = NarakaMod.identifier("ryoiki_gray");
    public static final Identifier RYOIKI_GREEN = NarakaMod.identifier("ryoiki_green");
}
