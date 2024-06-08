package com.yummy.naraka.client.animation;

import com.mojang.logging.LogUtils;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.Mth;
import org.slf4j.Logger;

import java.util.*;

public class NarakaAnimations implements ResourceManagerReloadListener {
    private static final Logger log = LogUtils.getLogger();
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

    public static AnimationInstance instance(String exactId, boolean repeat) {
        Animation animation = get(exactId);
        return new AnimationInstance(animation, repeat);
    }

    public static AnimationInstance instance(String id) {
        if (!id.contains(".chain."))
            return singleInstance(id);
        String[] ids = id.split(".chain.");
        List<String> idList = new ArrayList<>(List.of(ids));
        String last = idList.removeLast();
        AnimationInstance lastInstance = singleInstance(last);
        while (!idList.isEmpty()) {
            last = idList.removeLast();
            lastInstance = chainedInstance(last, lastInstance);
        }
        return lastInstance;
    }

    private static AnimationInstance chainedInstance(String id, AnimationInstance chain) {
        boolean repeat = id.endsWith(".repeat");
        if (repeat)
            log.warn("repeating will be ignored because it's chained (%s)".formatted(id));
        Animation animation = animations.get(id.replace(".repeat", ""));
        return new AnimationInstance(animation, chain);
    }

    private static AnimationInstance singleInstance(String id) {
        boolean repeat = id.endsWith(".repeat");
        return instance(id.replace(".repeat", ""), repeat);
    }

    private static Stack<AnimationInstance> instances(String[] ids) {
        Stack<AnimationInstance> instances = new Stack<>();
        for (String id : ids)
            instances.add(singleInstance(id));
        return instances;
    }

    public static void load() {
        animations.clear();
        animations.put("empty", Animation.EMPTY);

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

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        load();
    }
}
