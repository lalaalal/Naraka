package com.yummy.naraka.network;

import net.neoforged.neoforge.attachment.AttachmentType;

public interface AttachmentTypeProvider<T> {
    AttachmentType<T> get();
}
