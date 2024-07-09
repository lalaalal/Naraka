package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.yummy.naraka.attachment.NarakaAttachments;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.Locale;
import java.util.function.Supplier;

public enum IntAttachmentTypeProvider implements AttachmentTypeProvider<Integer>, StringRepresentable {
    STIGMA(NarakaAttachments.STIGMA),
    DEATH_COUNT(NarakaAttachments.DEATH_COUNT);

    public static final Codec<IntAttachmentTypeProvider> CODEC = StringRepresentable.fromEnum(IntAttachmentTypeProvider::values);

    private final Supplier<AttachmentType<Integer>> supplier;

    public static IntAttachmentTypeProvider from(AttachmentType<?> attachmentType) {
        for (IntAttachmentTypeProvider value : values()) {
            if (value.get() == attachmentType)
                return value;
        }
        throw new IllegalArgumentException();
    }

    IntAttachmentTypeProvider(Supplier<AttachmentType<Integer>> supplier) {
        this.supplier = supplier;
    }

    @Override
    public AttachmentType<Integer> get() {
        return supplier.get();
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
