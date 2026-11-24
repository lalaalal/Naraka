package com.yummy.naraka.client.renderer.uniform;

import com.mojang.blaze3d.shaders.AbstractUniform;
import com.mojang.blaze3d.shaders.Shader;
import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.renderer.EffectInstance;

import java.util.function.BiConsumer;

public class UniformBuffer<T> {
    private final Type<T> type;
    private final String name;
    private T value;

    public static UniformBuffer<Integer> createInteger(String name) {
        return new UniformBuffer<>(Type.INT, name);
    }

    public static UniformBuffer<Float> createFloat(String name) {
        return new UniformBuffer<>(Type.FLOAT, name);
    }

    private UniformBuffer(Type<T> type, String name) {
        this.type = type;
        this.name = name;
        this.value = type.defaultValue();
    }

    public Uniform createUniform(Shader parent) {
        return type.createUniform(name, parent);
    }

    public void storeValue(T value) {
        this.value = value;
    }

    public void setUniform(EffectInstance effectInstance) {
        AbstractUniform uniform = effectInstance.safeGetUniform(name);
        type.set(uniform, value);
    }

    private interface Type<T> {
        Type<Integer> INT = create(0, 0, AbstractUniform::set);
        Type<Float> FLOAT = create(4, 0f, AbstractUniform::set);

        static <T> Type<T> create(final int id, final T defaultValue, final BiConsumer<AbstractUniform, T> setter) {
            return new Type<>() {
                @Override
                public Uniform createUniform(String name, Shader parent) {
                    return new Uniform(name, id, 1, parent);
                }

                @Override
                public T defaultValue() {
                    return defaultValue;
                }

                @Override
                public void set(AbstractUniform uniform, T value) {
                    setter.accept(uniform, value);
                }
            };
        }

        Uniform createUniform(String name, Shader parent);

        void set(AbstractUniform uniform, T value);

        T defaultValue();
    }
}
