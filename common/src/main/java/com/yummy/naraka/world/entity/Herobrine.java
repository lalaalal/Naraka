package com.yummy.naraka.world.entity;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.core.particles.NarakaFlameParticleOption;
import com.yummy.naraka.network.NarakaClientboundEventPacket;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SyncAfterimagePacket;
import com.yummy.naraka.sounds.NarakaMusics;
import com.yummy.naraka.tags.NarakaEntityTypeTags;
import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.NarakaDimensions;
import com.yummy.naraka.world.NarakaPickaxe;
import com.yummy.naraka.world.effect.NarakaMobEffects;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import com.yummy.naraka.world.entity.ai.control.HerobrineFlyMoveControl;
import com.yummy.naraka.world.entity.ai.goal.MoveToTargetGoal;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.ai.skill.herobrine.*;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationLocations;
import com.yummy.naraka.world.entity.data.LockedHealthHelper;
import com.yummy.naraka.world.entity.data.Stigma;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Herobrine extends AbstractHerobrine {
    private static final float[] HEALTH_BY_PHASE = {106, 210, 350};

    public static final BossEvent.BossBarColor[] PROGRESS_COLOR_BY_PHASE = {BossEvent.BossBarColor.BLUE, BossEvent.BossBarColor.YELLOW, BossEvent.BossBarColor.RED};
    public static final int MAX_HURT_DAMAGE_LIMIT = 20;
    public static final int MAX_ACCUMULATED_DAMAGE_TICK_COUNT = 40;

    protected final DashSkill<AbstractHerobrine> dashSkill = registerSkill(10, this, DashSkill::new);
    protected final DashAroundSkill<AbstractHerobrine> dashAroundSkill = registerSkill(10, this, DashAroundSkill::new);
    protected final StigmatizeEntitiesSkill stigmatizeEntitiesSkill = registerSkill(10, this, StigmatizeEntitiesSkill::new, HerobrineAnimationLocations.STIGMATIZE_ENTITIES_START);
    protected final ThrowFireballSkill throwFireballSkill = registerSkill(9, new ThrowFireballSkill(this, this::createFireball), HerobrineAnimationLocations.THROW_NARAKA_FIREBALL);
    protected final BlockingSkill blockingSkill = registerSkill(10, this, BlockingSkill::new, HerobrineAnimationLocations.BLOCKING);
    protected final SummonShadowSkill summonShadowSkill = registerSkill(0, this, SummonShadowSkill::new);
    protected final RushSkill<AbstractHerobrine> rushSkill = registerSkill(8, new RushSkill<>(this), HerobrineAnimationLocations.RUSH);
    protected final DestroyStructureSkill destroyStructureSkill = registerSkill(this, DestroyStructureSkill::new);

    protected final LandingSkill landingSkill = registerSkill(this, LandingSkill::new, HerobrineAnimationLocations.COMBO_ATTACK_5);
    protected final SuperHitSkill superHitSkill = registerSkill(new SuperHitSkill(this, landingSkill), HerobrineAnimationLocations.COMBO_ATTACK_4);
    protected final SpinningSkill spinningSkill = registerSkill(new SpinningSkill(this, superHitSkill), HerobrineAnimationLocations.COMBO_ATTACK_3);
    protected final UppercutSkill uppercutSkill = registerSkill(new UppercutSkill(this, spinningSkill), HerobrineAnimationLocations.COMBO_ATTACK_2);
    protected final PunchSkill<AbstractHerobrine> punchSkill = registerSkill(2, new PunchSkill<>(this, 140, true, uppercutSkill), HerobrineAnimationLocations.COMBO_ATTACK_1);

    protected final FlickerSkill<Herobrine> flickerSkill = registerSkill(new FlickerSkill<>(this, dashSkill, punchSkill));
    protected final WalkAroundTargetSkill walkAroundTargetSkill = registerSkill(new WalkAroundTargetSkill(this, punchSkill, flickerSkill));

    protected final CarpetBombingSkill carpetBombingSkill = registerSkill(7, this, CarpetBombingSkill::new, HerobrineAnimationLocations.CARPET_BOMBING);
    protected final ExplosionSkill explosionSkill = registerSkill(7, this, ExplosionSkill::new, HerobrineAnimationLocations.EXPLOSION);
    protected final EarthShockSkill earthShockSkill = registerSkill(6, this, EarthShockSkill::new, HerobrineAnimationLocations.EARTH_SHOCK);
    protected final ParryingSkill parryingSkill = registerSkill(7, this, ParryingSkill::new, HerobrineAnimationLocations.PARRYING);
    protected final StormSkill stormSkill = registerSkill(6, new StormSkill(this, parryingSkill), HerobrineAnimationLocations.STORM);

    protected final StrikeDownSkill strikeDownSkill = registerSkill(new StrikeDownSkill(this, parryingSkill), HerobrineAnimationLocations.FINAL_COMBO_ATTACK_3);
    protected final SpinUpSkill spinUpSkill = registerSkill(new SpinUpSkill(this, strikeDownSkill), HerobrineAnimationLocations.FINAL_COMBO_ATTACK_2);
    protected final SplitAttackSkill splitAttackSkill = registerSkill(7, new SplitAttackSkill(this, spinUpSkill), HerobrineAnimationLocations.FINAL_COMBO_ATTACK_1);
    protected final PickaxeSlashSkill<AbstractHerobrine> singlePickaxeSlashSkill = registerSkill(7, this, PickaxeSlashSkill::single, HerobrineAnimationLocations.PICKAXE_SLASH_SINGLE);
    protected final PickaxeSlashSkill<Herobrine> triplePickaxeSlashSkill = registerSkill(6, this, PickaxeSlashSkill::triple, HerobrineAnimationLocations.PICKAXE_SLASH_TRIPLE);

    public final AnimationState chzzkAnimationState = new AnimationState();

    @Nullable
    private LivingEntity firstTarget;

    private final List<Skill<?>> HIBERNATED_MODE_PHASE_1_SKILLS = List.of(throwFireballSkill, blockingSkill);
    private final List<Skill<?>> HIBERNATED_MODE_PHASE_2_SKILLS = List.of(stigmatizeEntitiesSkill, blockingSkill, summonShadowSkill);
    private final List<Skill<?>> PHASE_1_SKILLS = List.of(punchSkill, dashAroundSkill, rushSkill, throwFireballSkill, walkAroundTargetSkill);
    private final List<Skill<?>> PHASE_2_SKILLS = List.of(punchSkill, dashAroundSkill, rushSkill, throwFireballSkill, summonShadowSkill, walkAroundTargetSkill);
    private final List<Skill<?>> PHASE_3_SKILLS = List.of(explosionSkill, splitAttackSkill, stormSkill, carpetBombingSkill, singlePickaxeSlashSkill, triplePickaxeSlashSkill, earthShockSkill);

    private final List<Skill<?>> INVULNERABLE_SKILLS = List.of(dashAroundSkill, walkAroundTargetSkill);
    private final List<ResourceLocation> INVULNERABLE_ANIMATIONS = List.of(HerobrineAnimationLocations.ENTER_PHASE_2, HerobrineAnimationLocations.STAGGERING_PHASE_2, HerobrineAnimationLocations.ENTER_PHASE_3);

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
    private final Set<UUID> stigmatizedEntities = new HashSet<>();
    private final Set<UUID> watchingEntities = new HashSet<>();
    protected final List<Afterimage> afterimages = new ArrayList<>();

    private final ServerBossEvent bossEvent = new ServerBossEvent(getName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    private final PhaseManager phaseManager = new PhaseManager(HEALTH_BY_PHASE, PROGRESS_COLOR_BY_PHASE, this, bossEvent);
    private final ShadowController shadowController = new ShadowController(this);

    @Nullable
    private UUID narakaPickaxeUUID;
    @Nullable
    private NarakaPickaxe cachedNarakaPickaxe;

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
        skillManager.enableOnly(PHASE_1_SKILLS);
        skillManager.shareCooldown(List.of(singlePickaxeSlashSkill, triplePickaxeSlashSkill));

        registerAnimation(HerobrineAnimationLocations.ENTER_PHASE_2);
        registerAnimation(HerobrineAnimationLocations.ENTER_PHASE_3);
        registerAnimation(HerobrineAnimationLocations.PREPARE_PHASE_3);
        registerAnimation(HerobrineAnimationLocations.STAGGERING_PHASE_2);
        registerAnimation(HerobrineAnimationLocations.RUSH_SUCCEED);
        registerAnimation(HerobrineAnimationLocations.RUSH_FAILED);
        registerAnimation(HerobrineAnimationLocations.FINAL_COMBO_ATTACK_1_RETURN);
        registerAnimation(HerobrineAnimationLocations.FINAL_COMBO_ATTACK_2_RETURN);
        registerAnimation(HerobrineAnimationLocations.PARRYING_SUCCEED);
        registerAnimation(HerobrineAnimationLocations.PARRYING_FAILED);

        registerAnimation(HerobrineAnimationLocations.STIGMATIZE_ENTITIES);
        registerAnimation(HerobrineAnimationLocations.STIGMATIZE_ENTITIES_END);

        registerAnimation(HerobrineAnimationLocations.DYING);
        registerAnimation(HerobrineAnimationLocations.CHZZK);
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
        setAnimation(HerobrineAnimationLocations.PHASE_3_IDLE);
        if (isFinalModel())
            return;
        teleportToSpawnedPosition();
        skillManager.setCurrentSkill(destroyStructureSkill);
        setNoGravity(true);
        setDisplayEye(false);
        setDisplayPickaxe(true);

        int armor = bossEvent.getPlayers().size() * 2;
        NarakaAttributeModifiers.addAttributeModifier(this, Attributes.ARMOR, NarakaAttributeModifiers.finalHerobrineArmor(armor));
        NarakaAttributeModifiers.addAttributeModifier(this, Attributes.ARMOR_TOUGHNESS, NarakaAttributeModifiers.FINAL_HEROBRINE_ARMOR_TOUGHNESS);
    }

    public void spawnNarakaPickaxe() {
        if (narakaPickaxeUUID == null) {
            NarakaPickaxe narakaPickaxe = new NarakaPickaxe(level(), this);
            narakaPickaxe.setPos(position());
            narakaPickaxe.setDeltaMovement(0, 0.2, 0);
            narakaPickaxe.setYRot(Mth.wrapDegrees(getYRot() + 180));
            level().addFreshEntity(narakaPickaxe);
            this.narakaPickaxeUUID = narakaPickaxe.getUUID();
            this.cachedNarakaPickaxe = narakaPickaxe;
        }
    }

    public Optional<NarakaPickaxe> getNarakaPickaxe(ServerLevel level) {
        if (narakaPickaxeUUID == null)
            return Optional.empty();
        if (cachedNarakaPickaxe == null)
            return Optional.ofNullable(NarakaEntityUtils.findEntityByUUID(level, narakaPickaxeUUID, NarakaPickaxe.class));
        return Optional.of(cachedNarakaPickaxe);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        if (firstTarget == null && target != null)
            firstTarget = target;
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
            startStaggering(HerobrineAnimationLocations.ENTER_PHASE_2, 55, 40);
        if (currentPhase == 3 && getCurrentAnimation() != HerobrineAnimationLocations.STIGMATIZE_ENTITIES_END && !isFinalModel())
            startStaggering();
    }

    private void updateMusic(int prevPhase, int currentPhase) {
        if (currentPhase == 3)
            NetworkManager.sendToClient(bossEvent.getPlayers(), new NarakaClientboundEventPacket(NarakaClientboundEventPacket.Event.STOP_MUSIC));
        else
            sendMusic(currentPhase);
    }

    public void sendMusic(int phase) {
        NarakaClientboundEventPacket.Event event = NarakaMusics.musicEventByPhase(phase);
        CustomPacketPayload packet = new NarakaClientboundEventPacket(event);
        NetworkManager.sendToClient(bossEvent.getPlayers(), packet);
    }

    private void updateUsingSkills(int prevPhase, int currentPhase) {
        skillManager.enableOnly(SKILLS_BY_PHASE.get(currentPhase));
    }

    public int getPhase() {
        return phaseManager.getCurrentPhase();
    }

    private float calculateLockedHealth(ServerLevel level) {
        double sum = 0;
        for (LivingEntity livingEntity : NarakaEntityUtils.findEntitiesByUUID(level, watchingEntities, LivingEntity.class))
            sum += LockedHealthHelper.get(livingEntity);
        return (float) sum;
    }

    private float calculateStigma(ServerLevel level) {
        float sum = 0;
        for (LivingEntity livingEntity : NarakaEntityUtils.findEntitiesByUUID(level, stigmatizedEntities, LivingEntity.class))
            sum += StigmaHelper.get(livingEntity).value();
        return sum;
    }

    @Override
    public float getAttackDamage() {
        if (level() instanceof ServerLevel serverLevel && getPhase() == 3)
            return super.getAttackDamage() + calculateLockedHealth(serverLevel) * 3;
        return super.getAttackDamage();
    }

    @Override
    public void stigmatizeEntity(ServerLevel level, LivingEntity target) {
        if (!target.getType().is(NarakaEntityTypeTags.HEROBRINE) && getPhase() > 1) {
            StigmaHelper.increaseStigma(level, target, this, true);
            stigmatizedEntities.add(target.getUUID());
            watchingEntities.add(target.getUUID());
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
        if (!level().isClientSide) {
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

    public void showPhaseChangeParticle(ServerLevel level, ParticleOptions particle) {
        for (int count = 0; count < 1500; count++) {
            double xRot = random.nextDouble() * Math.PI * 2;
            double yRot = random.nextDouble() * Math.PI * 2;
            double ySpeed = Math.sin(xRot);
            double base = Math.cos(xRot);

            double xSpeed = Math.cos(yRot) * base;
            double zSpeed = Math.sin(yRot) * base;

            double speed = 0.6;

            level.sendParticles(particle, getX(), getEyeY(), getZ(), 0, xSpeed, ySpeed, zSpeed, speed);
        }
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        updateAccumulatedDamage();

        if (!isFinalModel())
            tryAvoidProjectile();
        collectStigma(serverLevel);
        phaseManager.updatePhase(bossEvent);

        if (NarakaConfig.COMMON.despawnHerobrineWhenTargetIsDead.getValue()
                && firstTarget != null && firstTarget.isDeadOrDying()) {
            shadowController.killShadows(serverLevel);
            discard();
        }
        if (getPhase() == 2 && tickCount % 100 == 0) {
            shadowController.increaseFlickerStack(3);
        }

        if (getPhase() == 3 && tickCount % 200 == 0) {
            float lockedHealth = calculateLockedHealth(serverLevel);
            float stigma = calculateStigma(serverLevel);
            heal(stigma * lockedHealth * 6);
        }

        super.customServerAiStep(serverLevel);
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
        watchingEntities.removeIf(uuid -> {
            LivingEntity entity = NarakaEntityUtils.findEntityByUUID(serverLevel, uuid, LivingEntity.class);
            if (entity == null)
                return false;
            if (entity.isDeadOrDying()) {
                stigmatizedEntities.remove(uuid);
                return true;
            }
            return StigmaHelper.collectStigmaAfter(serverLevel, entity, this, waitingTick);
        });
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
        if (isAlive()) {
            bossEvent.addPlayer(serverPlayer);
            CustomPacketPayload packet = new NarakaClientboundEventPacket(NarakaMusics.musicEventByPhase(getPhase()));
            NetworkManager.sendToClient(serverPlayer, packet);
        }
    }

    public void startHerobrineSky(ServerLevel level) {
        NarakaClientboundEventPacket packet = new NarakaClientboundEventPacket(
                NarakaClientboundEventPacket.Event.START_HEROBRINE_SKY
        );
        NetworkManager.clientbound().send(bossEvent.getPlayers(), packet);
        level.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(false, level.getServer());
        level.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(false, level.getServer());
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
        bossEvent.removePlayer(serverPlayer);
        sendStopPacket(serverPlayer);
    }

    private void sendStopPacket(ServerPlayer serverPlayer) {
        CustomPacketPayload packet = new NarakaClientboundEventPacket(
                NarakaClientboundEventPacket.Event.STOP_MUSIC,
                NarakaClientboundEventPacket.Event.STOP_HEROBRINE_SKY,
                NarakaClientboundEventPacket.Event.STOP_WHITE_FOG
        );
        NetworkManager.clientbound().send(serverPlayer, packet);
    }

    private boolean isUsingInvulnerableSkill() {
        Skill<?> currentSkill = skillManager.getCurrentSkill();
        return currentSkill != null && INVULNERABLE_SKILLS.contains(currentSkill) || INVULNERABLE_ANIMATIONS.contains(getCurrentAnimation());
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return super.hurtServer(level, source, damage);
        if (isDeadOrDying() && source.getEntity() instanceof LivingEntity sourceEntity
                && sourceEntity.getMainHandItem().is(NarakaItems.PURIFIED_SOUL_SWORD.get())) {
            teleportTargetToNarakaDimension(level, sourceEntity);
            return false;
        }
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
                startStaggering(HerobrineAnimationLocations.STAGGERING_PHASE_2, 125, 100);
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
            hurtDamageLimit = MAX_HURT_DAMAGE_LIMIT - calculateLockedHealth(level);
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

    protected void startStaggering(ResourceLocation animation, int duration, int showParticleTick) {
        playStaticAnimation(animation, duration);
        resetDamageLimit();
        List<Integer> particleTicks = List.of(showParticleTick, showParticleTick - 5, showParticleTick - 10);
        animationTickListener = () -> {
            if (particleTicks.contains(animationTickLeft) && level() instanceof ServerLevel serverLevel) {
                showPhaseChangeParticle(serverLevel, NarakaFlameParticleOption.GOLD);
                if (phaseManager.getCurrentPhase() == 2)
                    entityData.set(DISPLAY_SCARF, true);
            }
        };
    }

    protected void startStaggering() {
        startStaggering(HerobrineAnimationLocations.STAGGERING, 70, -1);
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
                target.teleport(new TeleportTransition(narakaDimension, new Vec3(0, 10, 0), Vec3.ZERO, 0, 0, TeleportTransition.PLAY_PORTAL_SOUND));
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
        if (level() instanceof ServerLevel serverLevel) {
            bossEvent.getPlayers().forEach(this::sendStopPacket);
            bossEvent.removeAllPlayers();
            releaseStigma(serverLevel);
            spawnAbsoluteHerobrine(serverLevel.getServer());
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
            updateAnimation(HerobrineAnimationLocations.DYING);
        if (deathTime == 60) {
            updateAnimation(HerobrineAnimationLocations.CHZZK);
            chzzkAnimationState.start(tickCount);
        }
        if (deathTime > 1200)
            super.tickDeath();
        this.deathTime++;
    }

    private void releaseStigma(ServerLevel level) {
        for (LivingEntity livingEntity : NarakaEntityUtils.findEntitiesByUUID(level, stigmatizedEntities, LivingEntity.class)) {
            LockedHealthHelper.release(livingEntity);
            StigmaHelper.removeStigma(livingEntity);
        }
        stigmatizedEntities.clear();
    }

    @Override
    public void remove(RemovalReason reason) {
        if (reason.shouldDestroy() && level() instanceof ServerLevel serverLevel) {
            releaseStigma(serverLevel);
            shadowController.killShadows(serverLevel);
            serverLevel.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(true, serverLevel.getServer());
            serverLevel.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(true, serverLevel.getServer());
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
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("HurtDamageLimit", hurtDamageLimit);
        compound.putBoolean("HibernateMode", hibernateMode);
        compound.storeNullable("SpawnPosition", BlockPos.CODEC, spawnPosition);
        compound.storeNullable("NarakaPickaxe", UUIDUtil.CODEC, narakaPickaxeUUID);
        compound.store("StigmatizedEntities", UUIDUtil.CODEC_SET, stigmatizedEntities);
        compound.store("WatchingEntities", UUIDUtil.CODEC_SET, watchingEntities);
        shadowController.save(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (!(level() instanceof ServerLevel level))
            return;
        super.readAdditionalSaveData(compound);
        hurtDamageLimit = compound.getFloatOr("HurtDamageLimit", MAX_HURT_DAMAGE_LIMIT);
        hibernateMode = compound.getBooleanOr("HibernatedMode", false);
        if (hibernateMode)
            startHibernateMode(level);
        compound.read("NarakaPickaxe", UUIDUtil.CODEC).ifPresent(uuid -> narakaPickaxeUUID = uuid);
        compound.read("SpawnPosition", BlockPos.CODEC).ifPresent(pos -> spawnPosition = pos);
        compound.read("StigmatizedEntities", UUIDUtil.CODEC_SET).ifPresent(stigmatizedEntities::addAll);
        compound.read("WatchingEntities", UUIDUtil.CODEC_SET).ifPresent(watchingEntities::addAll);
        shadowController.load(compound);
    }
}
