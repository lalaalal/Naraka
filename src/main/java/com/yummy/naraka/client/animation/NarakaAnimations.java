package com.yummy.naraka.client.animation;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.Mth;

import java.util.HashMap;
import java.util.Map;

public class NarakaAnimations implements ResourceManagerReloadListener {
    private static NarakaAnimations INSTANCE;
    private static final Map<String, Animation> animations = new HashMap<>();

    public static NarakaAnimations classInstance() {
        if (INSTANCE == null)
            return INSTANCE = new NarakaAnimations();
        return INSTANCE;
    }

    public static Animation get(String id) {
        return animations.get(id);
    }

    public static AnimationInstance instance(String id, boolean repeat) {
        Animation animation = get(id);
        return new AnimationInstance(animation, repeat);
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        animations.clear();
        Animation herobrineIdle = Animation.builder("herobrine_idle")
                .define("head", "body", "left_arm", "right_arm")
                .startWithZeroPose()
                .keyframe("left_arm", 40).pose(-0.1f, 0, -Mth.PI / 72).easeInOut().end()
                .keyframe("left_arm", 80).zero().easeInOut().end()
                .keyframe("right_arm", 40).pose(-0.1f, 0, Mth.PI / 72).easeInOut().end()
                .keyframe("right_arm", 80).zero().easeInOut().end()
                .build();
        animations.put("herobrine.idle", herobrineIdle);
    }
}
