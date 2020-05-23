package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;

import net.minecraft.entity.ILivingEntityData;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public abstract class AbstractEntityCQRMageBase extends AbstractEntityCQRBoss {

	private static final DataParameter<Boolean> IDENTITY_HIDDEN = EntityDataManager.<Boolean>createKey(AbstractEntityCQRMageBase.class, DataSerializers.BOOLEAN);

	public AbstractEntityCQRMageBase(World worldIn) {
		super(worldIn);

		this.bossInfoServer.setColor(Color.RED);
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(IDENTITY_HIDDEN, true);
	}

	public void revealIdentity() {
		this.dataManager.set(IDENTITY_HIDDEN, false);
	}

	public boolean isIdentityHidden() {
		return this.dataManager.get(IDENTITY_HIDDEN);
	}

	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		super.damageEntity(damageSrc, damageAmount);

		if ((this.getHealth() / this.getMaxHealth()) < 0.83F) {
			this.revealIdentity();
		}
	}

	@Override
	public ILivingEntityData onInitialSpawn(DifficultyInstance difficulty, ILivingEntityData livingdata) {
		this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.STAFF_VAMPIRIC, 1));
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	public void writeEntityToNBT(CompoundNBT compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("identityHidden", this.isIdentityHidden());
	}

	@Override
	public void readEntityFromNBT(CompoundNBT compound) {
		super.readEntityFromNBT(compound);
		if (!compound.getBoolean("identityHidden")) {
			this.revealIdentity();
		}
	}

	@Override
	public boolean canIgniteTorch() {
		return false;
	}

}
