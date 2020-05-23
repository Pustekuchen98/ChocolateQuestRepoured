package com.teamcqr.chocolatequestrepoured.objects.entity;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.world.World;

public class EntitySlimePart extends SlimeEntity {

	private UUID ownerUuid;

	public EntitySlimePart(World worldIn) {
		super(worldIn);
	}

	public EntitySlimePart(World worldIn, LivingEntity owner) {
		this(worldIn);
		this.ownerUuid = owner.getUniqueID();
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.targetTasks.taskEntries.clear();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		iattributeinstance.setBaseValue(iattributeinstance.getBaseValue() * 0.5D);
	}

	@Override
	public void onUpdate() {
		if (this.ticksExisted > 400) {
			this.setDead();
		}

		super.onUpdate();
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		if (entityIn instanceof LivingEntity && entityIn.getUniqueID().equals(this.ownerUuid)) {
			((LivingEntity) entityIn).heal(2.0F);
			this.setDead();
		}
	}

	@Override
	protected boolean canDropLoot() {
		return false;
	}

	@Override
	public void writeEntityToNBT(CompoundNBT compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("ticksExisted", this.ticksExisted);
		compound.setTag("ownerUuid", NBTUtil.createUUIDTag(this.ownerUuid));
	}

	@Override
	public void readEntityFromNBT(CompoundNBT compound) {
		super.readEntityFromNBT(compound);
		this.ticksExisted = compound.getInteger("ticksExisted");
		this.ownerUuid = NBTUtil.getUUIDFromTag(compound.getCompoundTag("ownerUuid"));
	}

}
