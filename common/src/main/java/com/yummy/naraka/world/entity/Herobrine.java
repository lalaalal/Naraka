package com.yummy.naraka.world.entity;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.core.component.NarakaDataComponentTypes;
import com.yummy.naraka.core.particles.NarakaFlameParticleOption;
import com.yummy.naraka.network.NarakaClientboundEntityEventPacket;
import com.yummy.naraka.network.NarakaClientboundEventPacket;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SyncAfterimagePacket;
import com.yummy.naraka.sounds.NarakaMusics;
import com.yummy.naraka.tags.NarakaEntityTypeTags;
import com.yummy.naraka.tags.NarakaItemTags;
import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.NarakaDimensions;
import com.yummy.naraka.world.block.NarakaPortalBlock;
import com.yummy.naraka.world.effect.NarakaMobEffects;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import com.yummy.naraka.world.entity.ai.control.HerobrineFlyMoveControl;
import com.yummy.naraka.world.entity.ai.goal.MoveToTargetGoal;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.ai.skill.herobrine.*;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationIdentifiers;
import com.yummy.naraka.world.entity.data.LockedHealthHelper;
import com.yummy.naraka.world.entity.data.Stigma;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class Herobrine extends AbstractHerobrine {
    private static final float[] HEALTH_BY_PHASE = {106, 210, 350};

    public static final BossEvent.BossBarColor[] PROGRESS_COLOR_BY_PHASE = {BossEvent.BossBarColor.BLUE, BossEvent.BossBarColor.YELLOW, BossEvent.BossBarColor.RED};
    public static final int MAX_HURT_DAMAGE_LIMIT = 20;
    public static final int MAX_ACCUMULATED_DAMAGE_TICK_COUNT = 40;

    protected final DashSkill<AbstractHerobrine> dashSkill = registerSkill(10, this, DashSkill::new);
    protected final DashAroundSkill<AbstractHerobrine> dashAroundSkill = registerSkill(10, this, DashAroundSkill::new);
    protected final StigmatizeEntitiesSkill stigmatizeEntitiesSkill = registerSkill(10, this, StigmatizeEntitiesSkill::new, HerobrineAnimationIdentifiers.STIGMATIZE_ENTITIES_START);
    protected final ThrowFireballSkill throwFireballSkill = registerSkill(9, new ThrowFireballSkill(this, this::createFireball), HerobrineAnimationIdentifiers.THROW_NARAKA_FIREBALL);
    protected final BlockingSkill blockingSkill = registerSkill(10, this, BlockingSkill::new, HerobrineAnimationIdentifiers.BLOCKING);
    protected final SummonShadowSkill summonShadowSkill = registerSkill(0, this, SummonShadowSkill::new);
    protected final RushSkill<AbstractHerobrine> rushSkill = registerSkill(8, new RushSkill<>(this), HerobrineAnimationIdentifiers.RUSH);
    protected final DestroyStructureSkill destroyStructureSkill = registerSkill(this, DestroyStructureSkill::new);

    protected final LandingSkill landingSkill = registerSkill(this, LandingSkill::new, HerobrineAnimationIdentifiers.COMBO_ATTACK_5);
    protected final SuperHitSkill superHitSkill = registerSkill(new SuperHitSkill(this, landingSkill), HerobrineAnimationIdentifiers.COMBO_ATTACK_4);
    protected final SpinningSkill spinningSkill = registerSkill(new SpinningSkill(this, superHitSkill), HerobrineAnimationIdentifiers.COMBO_ATTACK_3);
    protected final UppercutSkill uppercutSkill = registerSkill(new UppercutSkill(this, spinningSkill), HerobrineAnimationIdentifiers.COMBO_ATTACK_2);
    protected final PunchSkill<AbstractHerobrine> punchSkill = registerSkill(2, new PunchSkill<>(this, 140, true, uppercutSkill), HerobrineAnimationIdentifiers.COMBO_ATTACK_1);

    protected final FlickerSkill<Herobrine> flickerSkill = registerSkill(new FlickerSkill<>(this, dashSkill, punchSkill));
    protected final WalkAroundTargetSkill walkAroundTargetSkill = registerSkill(new WalkAroundTargetSkill(this, punchSkill, flickerSkill));

    protected final StarShootingSkill starShootingSkill = registerSkill(6, this, StarShootingSkill::new, HerobrineAnimationIdentifiers.PHASE_3_IDLE);
    protected final CarpetBombingSkill carpetBombingSkill = registerSkill(6, this, CarpetBombingSkill::new, HerobrineAnimationIdentifiers.CARPET_BOMBING);
    protected final ExplosionSkill explosionSkill = registerSkill(7, this, ExplosionSkill::new, HerobrineAnimationIdentifiers.EXPLOSION);
    protected final EarthShockSkill earthShockSkill = registerSkill(6, this, EarthShockSkill::new, HerobrineAnimationIdentifiers.EARTH_SHOCK);
    protected final ParryingSkill parryingSkill = registerSkill(7, this, ParryingSkill::new, HerobrineAnimationIdentifiers.PARRYING);
    protected final StormSkill stormSkill = registerSkill(6, new StormSkill(this, parryingSkill), HerobrineAnimationIdentifiers.STORM);

    protected final StrikeDownSkill strikeDownSkill = registerSkill(new StrikeDownSkill(this, parryingSkill), HerobrineAnimationIdentifiers.FINAL_COMBO_ATTACK_3);
    protected final SpinUpSkill spinUpSkill = registerSkill(new SpinUpSkill(this, strikeDownSkill), HerobrineAnimationIdentifiers.FINAL_COMBO_ATTACK_2);
    protected final SplitAttackSkill splitAttackSkill = registerSkill(7, new SplitAttackSkill(this, spinUpSkill), HerobrineAnimationIdentifiers.FINAL_COMBO_ATTACK_1);
    protected final PickaxeSlashSkill<AbstractHerobrine> singlePickaxeSlashSkill = registerSkill(7, this, PickaxeSlashSkill::single, HerobrineAnimationIdentifiers.PICKAXE_SLASH_SINGLE);
    protected final PickaxeSlashSkill<Herobrine> triplePickaxeSlashSkill = registerSkill(6, this, PickaxeSlashSkill::triple, HerobrineAnimationIdentifiers.PICKAXE_SLASH_TRIPLE);
    protected final SpawnPickaxeSkill spawnPickaxeSkill = registerSkill(7, this, SpawnPickaxeSkill::new, HerobrineAnimationIdentifiers.PICKAXE_STRIKE);

    public final AnimationState chzzkAnimationState = new AnimationState();

    private final List<Skill<?>> HIBERNATED_MODE_PHASE_1_SKILLS = List.of(throwFireballSkill, blockingSkill);
    private final List<Skill<?>> HIBERNATED_MODE_PHASE_2_SKILLS = List.of(stigmatizeEntitiesSkill, blockingSkill, summonShadowSkill);
    private final List<Skill<?>> PHASE_1_SKILLS = List.of(punchSkill, dashAroundSkill, rushSkill, throwFireballSkill, walkAroundTargetSkill);
    private final List<Skill<?>> PHASE_2_SKILLS = List.of(punchSkill, dashAroundSkill, rushSkill, throwFireballSkill, summonShadowSkill, walkAroundTargetSkill);
    private final List<Skill<?>> PHASE_3_SKILLS = List.of(explosionSkill, splitAttackSkill, stormSkill, starShootingSkill, singlePickaxeSlashSkill, triplePickaxeSlashSkill, earthShockSkill, spawnPickaxeSkill);

    private final List<Skill<?>> INVULNERABLE_SKILLS = List.of(dashAroundSkill, walkAroundTargetSkill, destroyStructureSkill);
    private final List<Identifier> INVULNERABLE_ANIMATIONS = List.of(HerobrineAnimationIdentifiers.ENTER_PHASE_2, HerobrineAnimationIdentifiers.STAGGERING_PHASE_2, HerobrineAnimationIdentifiers.PREPARE_PHASE_3, HerobrineAnimationIdentifiers.ENTER_PHASE_3);

    private final List<List<Skill<?>>> HIBERNATED_MODE_SKILL_BY_PHASE = List.of(
            List.of(), HIBERNATED_MODE_PHASE_1_SKILLS, HIBERNATED_MODE_PHASE_2_SKILLS, List.of()
    );
    private final List<List<Skill<?>>> SKILLS_BY_PHASE = List.of(
            List.of(), PHASE_1_SKILLS, PHASE_2_SKILLS, PHASE_3_SKILLS
    );

    private final Map<Skill<?>, Float> STEPPABLE_SKILLS = Map.of(
            walkAroundTargetSkill, 0.5f
    );

    private final List<Projectile> ignoredProjectiles = new ArrayList<>();
    private int maxWatchedEntities = 0;
    @Nullable
    private LivingEntity selectedTarget;
    private final Set<UUID> watchingEntities = new HashSet<>();
    private final Map<UUID, LivingEntity> cachedWatchingEntities = new HashMap<>();
    protected final List<Afterimage> afterimages = new ArrayList<>();

    private final ServerBossEvent bossEvent = new ServerBossEvent(getName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    private final PhaseManager phaseManager = new PhaseManager(HEALTH_BY_PHASE, PROGRESS_COLOR_BY_PHASE, this, bossEvent);
    private final ShadowController shadowController = new ShadowController(this);

    private float hurtDamageLimit = MAX_HURT_DAMAGE_LIMIT;

    private boolean hibernateMode = false;

    protected int accumulatedDamageTickCount;
    protected float accumulatedHurtDamage;

    private @Nullable BlockPos spawnPosition;

    public Herobrine(EntityType<? extends Herobrine> entityType, Level level) {
        super(entityType, level, false);

        bossEvent.setPlayBossMusic(true);
        phaseManager.addPhaseChangeListener(this::updateMusic);
        phaseManager.addPhaseChangeListener(this::updateUsingSkills);
        phaseManager.addPhaseChangeListener((prev, current) -> resetDamageLimit());
        phaseManager.addPhaseChangeListener(this::startStaggering);
        phaseManager.addPhaseChangeListener(this::onPhase3, 3);

        skillManager.runOnSkillStart(this::enableEyeOnPhase3);
        skillManager.runOnSkillEnd(this::disableEyeOnPhase3);
        skillManager.runOnSkillEnd(this::useShadowFlicker);
        skillManager.runOnSkillSelect(this::changeTarget);
        skillManager.enableOnly(PHASE_1_SKILLS);
        skillManager.shareCooldown(List.of(singlePickaxeSlashSkill, triplePickaxeSlashSkill));

        registerAnimation(HerobrineAnimationIdentifiers.ENTER_PHASE_2);
        registerAnimation(HerobrineAnimationIdentifiers.ENTER_PHASE_3);
        registerAnimation(HerobrineAnimationIdentifiers.PREPARE_PHASE_3);
        registerAnimation(HerobrineAnimationIdentifiers.STAGGERING_PHASE_2);
        registerAnimation(HerobrineAnimationIdentifiers.RUSH_SUCCEED);
        registerAnimation(HerobrineAnimationIdentifiers.RUSH_FAILED);
        registerAnimation(HerobrineAnimationIdentifiers.FINAL_COMBO_ATTACK_1_RETURN);
        registerAnimation(HerobrineAnimationIdentifiers.FINAL_COMBO_ATTACK_2_RETURN);
        registerAnimation(HerobrineAnimationIdentifiers.PARRYING_SUCCEED);
        registerAnimation(HerobrineAnimationIdentifiers.PARRYING_FAILED);

        registerAnimation(HerobrineAnimationIdentifiers.STIGMATIZE_ENTITIES);
        registerAnimation(HerobrineAnimationIdentifiers.STIGMATIZE_ENTITIES_END);

        registerAnimation(HerobrineAnimationIdentifiers.DYING);
        registerAnimation(HerobrineAnimationIdentifiers.CHZZK);
        registerAnimation(HerobrineAnimationIdentifiers.HIDDEN_CHZZK);
    }

    private void useShadowFlicker(Skill<?> skill) {
        if (getPhase() == 2 && level() instanceof ServerLevel serverLevel) {
            shadowController.consumeFlickerStack(serverLevel);
        }
    }

    private void enableEyeOnPhase3(Skill<?> skill) {
        if (getPhase() == 3)
            setDisplayEye(true);
    }

    private void disableEyeOnPhase3(Skill<?> skill) {
        if (getPhase() == 3)
            setDisplayEye(false);
    }

    private void changeTarget(Skill<?> skill) {
        if (!cachedWatchingEntities.isEmpty()) {
            int randomSkip = random.nextInt(cachedWatchingEntities.size());
            cachedWatchingEntities.values().stream()
                    .skip(randomSkip)
                    .findFirst()
                    .ifPresent(livingEntity -> selectedTarget = livingEntity);
        }
    }

    public Herobrine(Level level, Vec3 pos) {
        this(NarakaEntityTypes.HEROBRINE.get(), level);
        setPos(pos);
    }

    public Optional<ShadowController> getShadowController() {
        return Optional.of(shadowController);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(3, new MoveToTargetGoal(this, 1, 32, 5, 0));
    }

    public void setSpawnPosition(BlockPos pos) {
        this.spawnPosition = pos;
    }

    public boolean hasSpawnPosition() {
        return spawnPosition != null;
    }

    private void resetDamageLimit() {
        this.hurtDamageLimit = MAX_HURT_DAMAGE_LIMIT;
    }

    private void onPhase3(int phase) {
        entityData.set(DISPLAY_SCARF, true);
        navigation = new FlyingPathNavigation(this, level());
        moveControl = new HerobrineFlyMoveControl(this, 0.75);
        setAnimation(HerobrineAnimationIdentifiers.PHASE_3_IDLE);
        if (isFinalModel()) {
            setNoGravity(true);
            return;
        }
        teleportToSpawnedPosition();
        skillManager.setCurrentSkill(destroyStructureSkill);
        setDisplayEye(false);
        setDisplayPickaxe(true);

        int armor = 0;
        for (ServerPlayer player : bossEvent.getPlayers()) {
            if (NarakaEntityUtils.isDamageablePlayer(player))
                armor += 6;
        }
        NarakaAttributeModifiers.addAttributeModifier(this, Attributes.ARMOR, NarakaAttributeModifiers.finalHerobrineArmor(armor));
        NarakaAttributeModifiers.addAttributeModifier(this, Attributes.ARMOR_TOUGHNESS, NarakaAttributeModifiers.FINAL_HEROBRINE_ARMOR_TOUGHNESS);
    }

    @Override
    public void push(Vec3 vector) {
        if (getPhase() < 3)
            super.push(vector);
    }

    @Override
    public void push(double x, double y, double z) {
        if (getPhase() < 3)
            super.push(x, y, z);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        if (target != null && !watchingEntities.contains(target.getUUID())) {
            watchingEntities.add(target.getUUID());
            cachedWatchingEntities.put(target.getUUID(), target);
            maxWatchedEntities = Math.max(watchingEntities.size(), maxWatchedEntities);
        }
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        if (isFinalModel()) {
            playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1, 0.8f);
            playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1, 0.95f);
            playSound(SoundEvents.ANVIL_PLACE, 0.2f, 0.95f);
        } else {
            super.playHurtSound(source);
        }
    }

    @Override
    @Nullable
    public LivingEntity getTarget() {
        if (selectedTarget != null && selectedTarget.isAlive())
            return selectedTarget;
        return super.getTarget();
    }

    @Override
    protected AABB makeBoundingBox(Vec3 position) {
        if (!firstTick && isFinalModel())
            return super.makeBoundingBox(position).expandTowards(0, 0.5, 0);
        return super.makeBoundingBox(position);
    }

    private void teleportToSpawnedPosition() {
        if (spawnPosition != null) {
            int floor = NarakaUtils.findFloor(level(), spawnPosition).getY() + 1;
            setDeltaMovement(Vec3.ZERO);
            setPos(spawnPosition.getX() + 0.5, floor, spawnPosition.getZ() + 0.5);
        }
    }

    private void startStaggering(int prevPhase, int currentPhase) {
        if (currentPhase == 2)
            startStaggering(HerobrineAnimationIdentifiers.ENTER_PHASE_2, 55, 40);
        if (currentPhase == 3 && getCurrentAnimation() != HerobrineAnimationIdentifiers.STIGMATIZE_ENTITIES_END && !isFinalModel())
            startStaggering();
    }

    private void updateMusic(int prevPhase, int currentPhase) {
        if (currentPhase == 3)
            NetworkManager.sendToClient(bossEvent.getPlayers(), new NarakaClientboundEntityEventPacket(
                    NarakaClientboundEntityEventPacket.Event.STOP_MUSIC, this
            ));
        else
            sendMusic(currentPhase);
    }

    public void sendMusic(int phase) {
        NarakaClientboundEntityEventPacket.Event event = NarakaMusics.musicEventByPhase(phase);
        CustomPacketPayload packet = new NarakaClientboundEntityEventPacket(event, this);
        NetworkManager.sendToClient(bossEvent.getPlayers(), packet);
    }

    private void updateUsingSkills(int prevPhase, int currentPhase) {
        skillManager.enableOnly(SKILLS_BY_PHASE.get(currentPhase));
    }

    public int getPhase() {
        return phaseManager.getCurrentPhase();
    }

    private float calculateLockedHealth() {
        double sum = 0;
        for (LivingEntity livingEntity : cachedWatchingEntities.values())
            sum += LockedHealthHelper.get(livingEntity);
        return (float) sum;
    }

    private float calculateStigma() {
        float sum = 0;
        for (LivingEntity livingEntity : cachedWatchingEntities.values())
            sum += StigmaHelper.get(livingEntity).value();
        return sum;
    }

    @Override
    public float getAttackDamage() {
        if (getPhase() == 3)
            return super.getAttackDamage() + calculateLockedHealth() * 3;
        return super.getAttackDamage();
    }

    @Override
    public void stigmatizeEntity(ServerLevel level, LivingEntity target) {
        if (!target.getType().is(NarakaEntityTypeTags.HEROBRINE) && getPhase() > 1) {
            StigmaHelper.increaseStigma(level, target, this, true);
            watchingEntities.add(target.getUUID());
            cachedWatchingEntities.put(target.getUUID(), target);
            maxWatchedEntities = Math.max(watchingEntities.size(), maxWatchedEntities);
        }
    }

    @Override
    public void collectStigma(ServerLevel level, LivingEntity target, Stigma original) {
        if (isAlive()) {
            this.heal(21);
            if (getPhase() == 2 && target.isDeadOrDying())
                stopHibernateMode(level);
        }
    }

    @Override
    public void heal(float healAmount) {
        float maxHealth = phaseManager.getActualCurrentPhaseMaxHealth();
        float healthAfterHeal = Mth.clamp(getHealth() + healAmount, getHealth(), maxHealth);
        setHealth(healthAfterHeal);
    }

    public int getShadowCount() {
        return shadowController.getShadowCount();
    }

    @Override
    public void addAfterimage(Afterimage afterimage) {
        if (!level().isClientSide()) {
            SyncAfterimagePacket payload = new SyncAfterimagePacket(this, afterimage);
            NetworkManager.sendToClient(bossEvent.getPlayers(), payload);
        }
        this.afterimages.add(afterimage);
    }

    @Override
    public List<Afterimage> getAfterimages() {
        return afterimages;
    }

    @Override
    public void tick() {
        super.tick();
        afterimages.removeIf(Afterimage::tick);
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        updateAccumulatedDamage();
        updateWatchingEntities(serverLevel);

        if (!isFinalModel())
            tryAvoidProjectile();
        collectStigma(serverLevel);
        phaseManager.updatePhase(bossEvent);

        if (NarakaConfig.COMMON.despawnHerobrineWhenTargetIsDead.getValue()
                && watchingEntities.isEmpty() && maxWatchedEntities > 0) {
            discard();
        }
        if (getPhase() == 2 && tickCount % 100 == 0) {
            shadowController.increaseFlickerStack(3);
        }

        if (getPhase() == 3 && tickCount % 66 == 0) {
            float lockedHealth = calculateLockedHealth();
            float stigma = calculateStigma();
            heal(stigma * lockedHealth * 1.5f);
        }

        if (isInWall())
            teleportToSpawnedPosition();

        super.customServerAiStep(serverLevel);
    }

    private void updateWatchingEntities(ServerLevel level) {
        watchingEntities.removeIf(uuid -> {
            LivingEntity target = NarakaEntityUtils.findEntityByUUID(level, uuid, LivingEntity.class);
            if (target == null || target.isDeadOrDying() || !NarakaEntityUtils.isDamageable(target)) {
                cachedWatchingEntities.remove(uuid);
                return true;
            }
            cachedWatchingEntities.put(uuid, target);
            return false;
        });
    }

    @Override
    public float maxUpStep() {
        Optional<Skill<?>> current = getCurrentSkill();
        if (current.isPresent() && STEPPABLE_SKILLS.containsKey(current.get()))
            return STEPPABLE_SKILLS.get(current.get());
        if (isUsingSkill())
            return 0;
        return super.maxUpStep();
    }

    @Override
    public boolean shouldPlayIdleAnimation() {
        return getPhase() < 3;
    }

    private void updateAccumulatedDamage() {
        if (accumulatedDamageTickCount > MAX_ACCUMULATED_DAMAGE_TICK_COUNT)
            resetAccumulatedDamage();
        accumulatedDamageTickCount += 1;
    }

    public void resetAccumulatedDamage() {
        accumulatedDamageTickCount = 0;
        accumulatedHurtDamage = 0;
    }

    private void collectStigma(ServerLevel serverLevel) {
        final int waitingTick = NarakaConfig.COMMON.herobrineTakingStigmaTick.getValue();

        for (LivingEntity entity : cachedWatchingEntities.values())
            StigmaHelper.collectStigmaAfter(serverLevel, entity, this, waitingTick);
    }

    private boolean shouldCheckProjectile(Projectile projectile) {
        return !ignoredProjectiles.contains(projectile);
    }

    private void tryAvoidProjectile() {
        List<Projectile> projectiles = level().getEntitiesOfClass(Projectile.class, getBoundingBox().inflate(5), this::shouldCheckProjectile);
        for (Projectile projectile : projectiles) {
            if (projectile.getOwner() == this)
                continue;
            ignoredProjectiles.add(projectile);
            if (!projectile.position().equals(new Vec3(projectile.xo, projectile.yo, projectile.zo)) && random.nextBoolean()) {
                lookAt(projectile, 360, 0);
                skillManager.setCurrentSkillIfAbsence(dashAroundSkill);
                return;
            }
        }
        ignoredProjectiles.removeIf(Projectile::isRemoved);
    }

    @Override
    public boolean canBeHitByProjectile() {
        return getCurrentSkill()
                .filter(skill -> skill != dashAroundSkill)
                .isPresent() && super.canBeHitByProjectile();
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        if (isAlive()) {
            bossEvent.addPlayer(serverPlayer);
            CustomPacketPayload packet = new NarakaClientboundEntityEventPacket(
                    NarakaMusics.musicEventByPhase(getPhase()),
                    this
            );
            NetworkManager.sendToClient(serverPlayer, packet);
            if (isFinalModel()) {
                this.startHerobrineSky();
            }
        }
    }

    public void startHerobrineSky() {
        NarakaClientboundEventPacket packet = new NarakaClientboundEventPacket(
                NarakaClientboundEventPacket.Event.START_HEROBRINE_SKY
        );
        NetworkManager.clientbound().send(bossEvent.getPlayers(), packet);
    }

    public void fixTimeAndWeather(ServerLevel level) {
        level.getGameRules().set(GameRules.ADVANCE_TIME, false, level.getServer());
        level.getGameRules().set(GameRules.ADVANCE_WEATHER, false, level.getServer());
        level.setDayTime(18000);
        level.resetWeatherCycle();
    }

    public void startWhiteScreen() {
        NarakaClientboundEventPacket packet = new NarakaClientboundEventPacket(
                NarakaClientboundEventPacket.Event.START_WHITE_SCREEN
        );
        NetworkManager.clientbound().send(bossEvent.getPlayers(), packet);
    }

    public void stopWhiteScreen() {
        NarakaClientboundEventPacket packet = new NarakaClientboundEventPacket(
                NarakaClientboundEventPacket.Event.STOP_WHITE_FOG
        );
        NetworkManager.clientbound().send(bossEvent.getPlayers(), packet);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        bossEvent.removePlayer(serverPlayer);
        if (isAlive() || serverPlayer.isDeadOrDying())
            sendStopPacket(serverPlayer);
    }

    private void sendStopPacket(ServerPlayer serverPlayer) {
        CustomPacketPayload packet = new NarakaClientboundEventPacket(
                NarakaClientboundEventPacket.Event.STOP_HEROBRINE_SKY,
                NarakaClientboundEventPacket.Event.STOP_WHITE_FOG
        );
        CustomPacketPayload entityEventPacket = new NarakaClientboundEntityEventPacket(
                NarakaClientboundEntityEventPacket.Event.STOP_MUSIC,
                this
        );
        NetworkManager.clientbound().send(serverPlayer, packet);
        NetworkManager.clientbound().send(serverPlayer, entityEventPacket);
    }

    private boolean isUsingInvulnerableSkill() {
        Skill<?> currentSkill = skillManager.getCurrentSkill();
        return currentSkill != null && INVULNERABLE_SKILLS.contains(currentSkill) || INVULNERABLE_ANIMATIONS.contains(getCurrentAnimation());
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (isDeadOrDying() && source.getEntity() instanceof LivingEntity sourceEntity) {
            if (sourceEntity.getMainHandItem().is(NarakaItemTags.ENTER_NARAKA_DIMENSION)) {
                teleportTargetToNarakaDimension(level, sourceEntity);
            } else {
                NarakaEntityTypes.SHINY_EFFECT.get().spawn(level, blockPosition().above(), EntitySpawnReason.EVENT);
                remove(RemovalReason.KILLED);
            }
            return false;
        }
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return super.hurtServer(level, source, damage);
        if (source.getEntity() == this || isUsingInvulnerableSkill()) {
            level.playSound(null, getX(), getY(), getZ(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.HOSTILE, 4, 0.3f);
            return false;
        }

        float limitedDamage = Math.min(damage, hurtDamageLimit);
        if (updateHibernateMode(level, source, getActualDamage(source, limitedDamage)))
            return true;
        if (hibernateMode)
            return true;

        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            if (source.getDirectEntity() != null)
                lookAt(source.getDirectEntity(), 360, 0);
            if (!isFinalModel())
                skillManager.setCurrentSkillIfAbsence(blockingSkill);
            return false;
        }
        return super.hurtServer(level, source, limitedDamage);
    }

    @Override
    protected void actuallyHurt(ServerLevel level, DamageSource damageSource, float damageAmount) {
        if (!damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)
                && phaseManager.getCurrentPhaseHealth() - damageAmount < 0 && getPhase() < 3) {
            setHealth(phaseManager.getActualPhaseMaxHealth(getPhase() + 1) + 1);
            return;
        }
        super.actuallyHurt(level, damageSource, damageAmount);
        updateHurtDamageLimit(level);
        accumulatedHurtDamage += damageAmount;
        if (getPhase() == 2 && (accumulatedHurtDamage > 15 || random.nextDouble() < 0.25f))
            shadowController.increaseFlickerStack();
    }

    private float getPhaseMinimumHealth() {
        return phaseManager.getActualPhaseMaxHealth(getPhase() + 1) + 1;
    }

    private boolean updateHibernateMode(ServerLevel level, DamageSource source, float actualDamage) {
        if (source.getDirectEntity() instanceof NarakaFireball fireball && !fireball.hasTarget()) {
            if (getHealth() > getPhaseMinimumHealth())
                startStaggering();
            if (getHealth() == getPhaseMinimumHealth() && getPhase() == 1)
                startStaggering(HerobrineAnimationIdentifiers.STAGGERING_PHASE_2, 125, 100);
            resetDamageLimit();
            if (hibernateMode)
                stopHibernateMode(level);
            return true;
        }

        return preserveCurrentPhaseMinimumHealth(level, actualDamage);
    }

    private boolean preserveCurrentPhaseMinimumHealth(ServerLevel level, float actualDamage) {
        if (getPhase() == phaseManager.getMaxPhase())
            return false;
        float healthAfterHurt = phaseManager.getCurrentPhaseHealth() + getAbsorptionAmount() - actualDamage;
        if (healthAfterHurt < 2) {
            setHealth(getPhaseMinimumHealth());
            startHibernateMode(level);
            return true;
        }

        return false;
    }

    private float getActualDamage(DamageSource source, float damage) {
        damage = getDamageAfterArmorAbsorb(source, damage);
        return getDamageAfterMagicAbsorb(source, damage);
    }

    private float calculateHurtDamageLimitByLockedHealth() {
        double sum = 0;
        for (LivingEntity livingEntity : cachedWatchingEntities.values()) {
            double lockedHealth = LockedHealthHelper.get(livingEntity);
            sum += (lockedHealth / (livingEntity.getMaxHealth() + lockedHealth)) * 20;
        }
        return MAX_HURT_DAMAGE_LIMIT - (float) sum;
    }

    private void updateHurtDamageLimit(ServerLevel level) {
        if (phaseManager.getCurrentPhase() < 3 && hurtDamageLimit > 1) {
            float damageLimitReduce = NarakaConfig.COMMON.herobrineHurtLimitReduce.getValue();
            hurtDamageLimit = hurtDamageLimit - damageLimitReduce;
            if (hurtDamageLimit <= 1) {
                hurtDamageLimit = 0;
                startHibernateMode(level);
            }
        }
        if (getPhase() == 3) {
            hurtDamageLimit = calculateHurtDamageLimitByLockedHealth();
        }
    }

    public boolean isHibernateMode() {
        return hibernateMode;
    }

    protected void startHibernateMode(ServerLevel level) {
        if (hibernateMode)
            return;
        hibernateMode = true;
        skillManager.enableOnly(HIBERNATED_MODE_SKILL_BY_PHASE.get(getPhase()));
        skillManager.interrupt();
        shadowController.activateFlickerSkill(level);
        NarakaAttributeModifiers.addAttributeModifier(this, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.HIBERNATE_PREVENT_MOVING);
        teleportToSpawnedPosition();
    }

    protected void stopHibernateMode(ServerLevel level) {
        hibernateMode = false;
        resetDamageLimit();
        skillManager.enableOnly(SKILLS_BY_PHASE.get(getPhase()));
        shadowController.deactivateFlickerSkill(level);
        NarakaAttributeModifiers.removeAttributeModifier(this, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.HIBERNATE_PREVENT_MOVING);

        setHealth(getHealth() - 1);
    }

    protected void startStaggering(Identifier animation, int duration, int showParticleTick) {
        playStaticAnimation(animation, duration);
        resetDamageLimit();
        List<Integer> particleTicks = List.of(showParticleTick, showParticleTick - 5, showParticleTick - 10);
        animationTickListener = () -> {
            if (particleTicks.contains(animationTickLeft) && level() instanceof ServerLevel serverLevel) {
                NarakaSkillUtils.sendSphereParticles(serverLevel, this, NarakaFlameParticleOption.GOLD, 0.6);
                if (phaseManager.getCurrentPhase() == 2)
                    entityData.set(DISPLAY_SCARF, true);
            }
        };
    }

    protected void startStaggering() {
        startStaggering(HerobrineAnimationIdentifiers.STAGGERING, 70, -1);
    }

    @Override
    public Fireball createFireball(ServerLevel level) {
        NarakaFireball fireball = new NarakaFireball(this, Vec3.ZERO, level(), false);
        fireball.setDamageCalculator(this::calculateFireballDamage);
        fireball.setCanDeflect(false);
        fireball.setTimeToLive(50);
        fireball.addHurtTargetListener((target, damage) -> stigmatizeEntity(level, target));
        fireball.addHurtTargetListener((target, damage) -> updateHibernateModeOnTargetSurvivedFromFireball(level, target, damage));

        return fireball;
    }

    private float calculateFireballDamage(NarakaFireball fireball) {
        if (getTarget() == null)
            return getAttackDamage();
        if (!hibernateMode)
            return getAttackDamage() + getTarget().getMaxHealth() * 0.05f;
        return getTarget().getMaxHealth() * 0.5f;
    }

    private void updateHibernateModeOnTargetSurvivedFromFireball(ServerLevel level, LivingEntity target, float damage) {
        if (target != this && getPhase() == 1 && hibernateMode && damage >= 66 && target.isAlive()) {
            stopHibernateMode(level);
        }
    }

    protected void teleportTargetToNarakaDimension(ServerLevel level, Entity target) {
        if (isDeadOrDying() && isFinalModel()) {
            ServerLevel narakaDimension = level.getServer().getLevel(NarakaDimensions.NARAKA);
            if (narakaDimension != null) {
                BlockPos blockPos = NarakaPortalBlock.createRandomNarakaSpawnPosition(random);
                Vec3 pos = blockPos.getBottomCenter();
                target.teleport(new TeleportTransition(narakaDimension, pos, Vec3.ZERO, 0, 0, TeleportTransition.PLAY_PORTAL_SOUND));
            }
        }
    }

    @Override
    @Nullable
    public Entity teleport(TeleportTransition teleportTransition) {
        if (!level().dimension().equals(teleportTransition.newLevel().dimension()) && level() instanceof ServerLevel level) {
            for (ShadowHerobrine shadowHerobrine : shadowController.getShadows(level))
                shadowHerobrine.teleport(teleportTransition);
        }
        return super.teleport(teleportTransition);
    }

    public void shakeCamera() {
        NetworkManager.clientbound().send(bossEvent.getPlayers(), new NarakaClientboundEventPacket(
                NarakaClientboundEventPacket.Event.SHAKE_CAMERA
        ));
    }

    @Override
    public void die(DamageSource damageSource) {
        if (damageSource.getEntity() instanceof LivingEntity livingEntity)
            rewardChallenger(livingEntity);
        super.die(damageSource);
        if (!level().isClientSide()) {
            bossEvent.getPlayers().forEach(this::sendStopPacket);
            bossEvent.removeAllPlayers();
            releaseStigma();
        }
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            deathTime = 1180;
    }

    private void spawnAbsoluteHerobrine(MinecraftServer server) {
        ServerLevel narakaLevel = server.getLevel(NarakaDimensions.NARAKA);
        if (narakaLevel != null)
            NarakaEntityTypes.ABSOLUTE_HEROBRINE.get().spawn(narakaLevel, BlockPos.ZERO.above(10), EntitySpawnReason.TRIGGERED);
    }

    @Override
    protected void tickDeath() {
        setXRot(0);
        setDeltaMovement(0, -1, 0);
        if (deathTime == 0)
            updateAnimation(HerobrineAnimationIdentifiers.DYING);
        if (deathTime == 60) {
            updateAnimation(HerobrineAnimationIdentifiers.CHZZK);
            chzzkAnimationState.start(tickCount);
        }
        if (deathTime > 1200)
            super.tickDeath();
        this.deathTime++;
    }

    private void releaseStigma() {
        for (LivingEntity livingEntity : cachedWatchingEntities.values()) {
            LockedHealthHelper.release(livingEntity);
            StigmaHelper.removeStigma(livingEntity);
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        if (reason.shouldDestroy() && level() instanceof ServerLevel serverLevel) {
            releaseStigma();
            shadowController.killShadows(serverLevel);
            if (reason == RemovalReason.DISCARDED)
                players().forEach(this::sendStopPacket);
            serverLevel.getGameRules().set(GameRules.ADVANCE_TIME, true, serverLevel.getServer());
            serverLevel.getGameRules().set(GameRules.ADVANCE_WEATHER, true, serverLevel.getServer());
        }
        super.remove(reason);
    }

    private void rewardChallenger(LivingEntity livingEntity) {
        NarakaMobEffects.getChallengersBlessing(livingEntity).ifPresent(instance -> {
            livingEntity.removeEffect(instance.getEffect());
            for (EquipmentSlot slot : EquipmentSlotGroup.ARMOR.slots()) {
                ItemStack stack = livingEntity.getItemBySlot(slot);
                stack.consume(1, livingEntity);
            }
            ItemStack weaponStack = livingEntity.getMainHandItem();
            weaponStack.set(NarakaDataComponentTypes.BLESSED.get(), true);
        });
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putFloat("HurtDamageLimit", hurtDamageLimit);
        output.putBoolean("HibernateMode", hibernateMode);
        output.storeNullable("SpawnPosition", BlockPos.CODEC, spawnPosition);
        output.store("WatchingEntities", UUIDUtil.CODEC_SET, watchingEntities);
        shadowController.save(output);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        if (!(level() instanceof ServerLevel level))
            return;
        super.readAdditionalSaveData(input);
        hurtDamageLimit = input.getFloatOr("HurtDamageLimit", MAX_HURT_DAMAGE_LIMIT);
        hibernateMode = input.getBooleanOr("HibernatedMode", false);
        if (hibernateMode)
            startHibernateMode(level);
        input.read("SpawnPosition", BlockPos.CODEC).ifPresent(pos -> spawnPosition = pos);
        input.read("WatchingEntities", UUIDUtil.CODEC_SET).ifPresent(watchingEntities::addAll);
        shadowController.load(input);
    }
}
