package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ProjectileVampiricSpell extends ProjectileBase {
	private LivingEntity shooter;

	public ProjectileVampiricSpell(World worldIn) {
		super(worldIn);
	}

	public ProjectileVampiricSpell(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public ProjectileVampiricSpell(World worldIn, LivingEntity shooter) {
		super(worldIn, shooter);
		this.shooter = shooter;
		this.isImmuneToFire = false;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote) {
			if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
				if (result.entityHit instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) result.entityHit;
					float damage = 4F;

					if (result.entityHit == this.shooter) {
						return;
					}

					entity.attackEntityFrom(DamageSource.MAGIC, damage);

					if (this.shooter != null && this.shooter.getHealth() < this.shooter.getMaxHealth()) {
						this.shooter.heal(1.0F);
					}

					this.setDead();
				}
			}

			super.onImpact(result);
		}
	}

	@Override
	protected void onUpdateInAir() {
		if (this.world.isRemote) {
			if (this.ticksExisted < 30) {
				this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + 0.1D, this.posZ, 0.0D, 0.0D, 0.0D);
			}
		}
	}
}