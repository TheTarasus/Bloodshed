package ru.thetarasus.bloodshed.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.thetarasus.bloodshed.BloodEntity;
import ru.thetarasus.bloodshed.Bloodshed;

import java.util.Random;

@Mixin(DamageTracker.class)
public abstract class PlayerMixin{

    @Shadow @Final private LivingEntity entity;

    @Inject(at = @At("HEAD"), method = "onDamage")
    public void onDamage(DamageSource source, float originalHealth, float damage, CallbackInfo ci) {

        if(!(entity instanceof PlayerEntity)) return;

        boolean isMoreOrLess = damage > 4;
        if (!isMoreOrLess) return;


        if (source.getSource() != null) {
            spreadBloodDirectly(damage, source.getSource().getEyePos());
            return;
        }

        if (source.getAttacker() != null) {
            spreadBloodDirectly(damage, source.getAttacker().getEyePos());
            return;
        }
        spreadBlood(damage);

    }

    public void spreadBloodDirectly(float amount, Vec3d pos) {
        Vec3d dirPos = this.entity.getEyePos().relativize(pos).normalize();
        dirPos = dirPos.multiply(-0.5);
        int count = Math.min((int) amount >> 1, 32);
        Random r = new Random();
        for (int i = 0; i <= count; i++) {
            Vec3d localPos = dirPos;
            localPos = localPos.add((r.nextFloat() -0.5)*0.25, (r.nextFloat() -0.5)*0.25, (r.nextFloat() -0.5)*0.25);
            localPos = localPos.multiply((r.nextFloat() * count * 0.5));

            BloodEntity bloodEntity = Bloodshed.BLOOD_ENTITY.create(this.entity.world);
            bloodEntity.setPosition(this.entity.getPos().add(0, 1.5, 0));
            bloodEntity.setVelocity(localPos);

            this.entity.getWorld().spawnEntity(bloodEntity);
        }
    }

    public void spreadBlood(float amount) {
        int count = Math.min((int) amount >> 1, 32);
        Random r = new Random();
        for (int i = 0; i <= count; i++) {
            Vec3d localPos = new Vec3d(0, 0, 0);
            localPos = localPos.add(r.nextFloat() -0.5, r.nextFloat() -0.5, r.nextFloat() -0.5);
            localPos = localPos.multiply((r.nextFloat() * count * 0.5));

            BloodEntity bloodEntity = Bloodshed.BLOOD_ENTITY.create(this.entity.world);
            bloodEntity.setPosition(this.entity.getPos().add(0, 1.5, 0));
            bloodEntity.setVelocity(localPos);

            this.entity.getWorld().spawnEntity(bloodEntity);
        }
    }
}
