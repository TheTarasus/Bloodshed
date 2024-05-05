package ru.thetarasus.bloodshed;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class BloodEntity extends ThrownItemEntity {

    public BloodEntity(EntityType<? extends ThrownItemEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if(!world.isClient) this.discard();
        BlockPos origin = blockHitResult.getBlockPos();
        BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());
        if(!world.getBlockState(origin).isFullCube(world, pos)) {discard(); return;}
        if(!world.getBlockState(pos).isAir()) {discard(); return;}
        world.setBlockState(pos, Bloodshed.BLOOD_BLOCK.getDefaultState().with(Properties.AGE_3, 3));
        discard();
    }

    @Override
    public void tick() {
        super.tick();
        if(world.isClient) return;
        if(world.getTime() % 4 != 0) return;
        world.addParticle(new DustParticleEffect(new Vec3f(0.5f, 0f, 0f), 0.25f), getX(), getY(), getZ(), 0, 0, 0);
    }

    @Override
    protected Item getDefaultItem() {
        return Bloodshed.BLOOD_ITEM;
    }
}
