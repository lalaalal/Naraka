package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.renderer.VertexConsumerExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VertexConsumer.class)
public interface VertexConsumerMixin extends VertexConsumerExtension {

}
