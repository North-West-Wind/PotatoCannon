package ml.northwestwind.potatocannon.entities;

import ml.northwestwind.potatocannon.PotatoCannon;
import ml.northwestwind.potatocannon.others.PotatoExplosion;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class ThrownPotato extends ThrowableItemProjectile {
    private Entity shooter = null;

    public ThrownPotato(EntityType<ThrownPotato> type, Level world) {
        super(type, world);
    }

    public ThrownPotato(LivingEntity entity, Level world) {
        super(PotatoCannon.POTATO_ENTITY_TYPE.get(), entity, world);
        shooter = entity;
    }

    @Override
    public void setOwner(@Nullable Entity entityIn) {
        super.setOwner(entityIn);
        this.shooter = entityIn;
    }

    @Override
    protected void onHit(HitResult result) {
        if (result.getType().equals(HitResult.Type.MISS)) return;
        createExplosion(this, result.getLocation());
        if (!level.isClientSide) remove(RemovalReason.DISCARDED);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.POTATO;
    }

    private Explosion createExplosion(@Nullable Entity exploder, Vec3 pos) {
        PotatoExplosion explosion = new PotatoExplosion(level, exploder, pos, shooter);
        if (!level.isClientSide) explosion.explode();
        explosion.finalizeExplosion(true);
        return explosion;
    }
}
