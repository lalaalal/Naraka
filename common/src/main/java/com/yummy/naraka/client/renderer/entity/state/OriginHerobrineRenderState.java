package com.yummy.naraka.client.renderer.entity.state;

import com.yummy.naraka.world.item.SoulType;

import java.util.List;
import java.util.Map;

public class OriginHerobrineRenderState extends AbstractHerobrineRenderState {
    public List<SoulType> absorbedSoulTypes = List.of();
    public Map<SoulType, Float> soulTypeAlpha = Map.of();
}
