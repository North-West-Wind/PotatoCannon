package ml.northwestwind.potatocannon.entities;

import ml.northwestwind.potatocannon.PotatoCannon;
import ml.northwestwind.potatocannon.others.PotatoExplosion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class PotatoEntity extends ProjectileItemEntity {
    private Entity shooter = null;

    public PotatoEntity(EntityType<PotatoEntity> type, World world) {
        super(type, world);
    }

    public PotatoEntity(LivingEntity entity, World world) {
        super(PotatoCannon.POTATO_ENTITY_TYPE, entity, world);
        shooter = entity;
    }

    @Override
    public void setShooter(@Nullable Entity entityIn) {
        super.setShooter(entityIn);
        this.shooter = entityIn;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.getType().equals(RayTraceResult.Type.MISS)) return;
        createExplosion(this, result.getHitVec());
        if (!world.isRemote) remove();
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.POTATO;
    }

    private Explosion createExplosion(@Nullable Entity exploder, Vector3d pos) {
        PotatoExplosion explosion = new PotatoExplosion(world, exploder, pos, shooter);
        if (!world.isRemote) explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }
}
