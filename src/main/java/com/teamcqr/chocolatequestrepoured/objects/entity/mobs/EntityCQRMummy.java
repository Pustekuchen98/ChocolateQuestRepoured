package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.init.ModLoottables;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityCQRMummy extends AbstractEntityCQR {

	public EntityCQRMummy(World worldIn) {
		super(worldIn);
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		boolean flag = super.attackEntityAsMob(entityIn);

		if (flag && this.getHeldItemMainhand().isEmpty() && entityIn instanceof EntityLivingBase) {
			int i = this.world.getDifficulty().getDifficultyId();
			((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.HUNGER, 140 * i));
		}

		return flag;
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.MUMMY.getValue();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.UNDEAD;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_HUSK_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_HUSK_DEATH;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_HUSK_AMBIENT;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return ModLoottables.ENTITIES_MUMMY;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

}
