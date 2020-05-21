package com.teamcqr.chocolatequestrepoured.objects.entity.bases;

import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public abstract class EntityCQRGiantSilverfishBase extends EntityCQRMountBase {

	public EntityCQRGiantSilverfishBase(World worldIn) {
		super(worldIn);
		this.setSize(2.0F, 1.0F);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_SILVERFISH_HURT;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SILVERFISH_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SILVERFISH_DEATH;
	}

}
