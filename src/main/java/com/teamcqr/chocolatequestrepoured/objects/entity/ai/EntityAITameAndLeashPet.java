package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.EnumSet;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.TargetUtil;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.LeadItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class EntityAITameAndLeashPet extends AbstractCQREntityAI<AbstractEntityCQR> {

	// TODO: Save pet information on entity!!!

	protected static final double PET_SEARCH_RADIUS = 16.0D;
	protected static final double DISTANCE_TO_PET = 2.0D;
	protected static final double WALK_SPEED_TO_PET = 1.0D;

	protected TameableEntity entityToTame = null;

	public EntityAITameAndLeashPet(AbstractEntityCQR entity) {
		super(entity);
		//this.setMutexBits(3);
		setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		if (!this.entity.canTameEntity()) {
			return false;
		}
		if (!(this.entity.getHeldItemMainhand().getItem() instanceof LeadItem || this.entity.getHeldItemOffhand().getItem() instanceof LeadItem)) {
			return false;
		}
		if (this.entity.ticksExisted % 4 == 0) {
			Vec3d vec1 = this.entity.getPositionVector().add(PET_SEARCH_RADIUS, PET_SEARCH_RADIUS * 0.5D, PET_SEARCH_RADIUS);
			Vec3d vec2 = this.entity.getPositionVector().subtract(PET_SEARCH_RADIUS, PET_SEARCH_RADIUS * 0.5D, PET_SEARCH_RADIUS);
			AxisAlignedBB aabb = new AxisAlignedBB(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z);
			List<TameableEntity> possiblePets = this.entity.world.getLoadedEntitiesWithinAABB(TameableEntity.class, aabb, TargetUtil.PREDICATE_PETS);
			if (!possiblePets.isEmpty()) {
				this.entityToTame = TargetUtil.getNearestEntity(this.entity, possiblePets);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!(this.entity.getHeldItemMainhand().getItem() instanceof LeadItem || this.entity.getHeldItemOffhand().getItem() instanceof LeadItem)) {
			return false;
		}
		if (this.entityToTame == null) {
			return false;
		}
		if (!this.entityToTame.isAlive()) {
			return false;
		}
		if (this.entityToTame.getOwnerId() != null) {
			return false;
		}
		if (this.entity.getDistance(this.entityToTame) > 16.0D) {
			return false;
		}
		return this.entity.hasPath();
	}

	@Override
	public void startExecuting() {
		if (this.entity.getDistance(this.entityToTame) > DISTANCE_TO_PET) {
			this.entity.getNavigator().tryMoveToEntityLiving(this.entityToTame, WALK_SPEED_TO_PET);
		}
	}

	@Override
	public void tick() {
		if (this.entity.getDistance(this.entityToTame) > DISTANCE_TO_PET) {
			this.entity.getNavigator().tryMoveToEntityLiving(this.entityToTame, WALK_SPEED_TO_PET);
		} else {
			this.entityToTame.setOwnerId(this.entity.getUniqueID());
			this.entityToTame.setTamed(true);
			this.entityToTame.setLeashHolder(this.entity, true);
		}
	}

	@Override
	public void resetTask() {
		this.entityToTame = null;
		this.entity.getNavigator().clearPath();
	}

}
