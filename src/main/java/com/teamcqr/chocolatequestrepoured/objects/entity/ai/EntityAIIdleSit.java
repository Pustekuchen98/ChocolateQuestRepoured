package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.List;

import com.google.common.base.Predicate;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class EntityAIIdleSit extends AbstractCQREntityAI<AbstractEntityCQR> {

	protected static final int COOLDOWN_BORDER = 50;
	protected static final int COOLDOWN_FOR_PARTNER_CYCLE_BORDER = 100;

	private Entity talkingPartner = null;
	private int cooldown = 0;
	private int cooldwonForPartnerCycle = 0;
	protected final Predicate<AbstractEntityCQR> predicate;

	public EntityAIIdleSit(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(2);
		this.predicate = new Predicate<AbstractEntityCQR>() {
			@Override
			public boolean apply(AbstractEntityCQR input) {
				if (input == null) {
					return false;
				}
				if (!EntitySelectors.IS_ALIVE.apply(input)) {
					return false;
				}
				return EntityAIIdleSit.this.isEntityAlly(input);
			}
		};
	}

	@Override
	public boolean shouldExecute() {
		if (!this.entity.onGround) {
			return false;
		}
		if (this.entity.isBurning()) {
			return false;
		}
		if (this.entity.isRiding()) {
			return false;
		}
		if (this.isEntityMoving(this.entity)) {
			return false;
		}
		return this.entity.getAttackTarget() == null;
	}

	@Override
	public void resetTask() {
		this.cooldown = 0;
		this.cooldwonForPartnerCycle = 0;
		this.talkingPartner = null;
		this.entity.setSitting(false);
		this.entity.setChatting(false);
	}

	@Override
	public void updateTask() {
		if (this.cooldown < COOLDOWN_BORDER) {
			this.cooldown++;
		}

		if (this.cooldown >= COOLDOWN_BORDER) {
			if (this.cooldwonForPartnerCycle < COOLDOWN_FOR_PARTNER_CYCLE_BORDER) {
				this.cooldwonForPartnerCycle++;
			}

			// Make entity sit
			if (!this.entity.isSitting()) {
				this.entity.setSitting(true);
			}

			// search for new talking partner
			if (this.talkingPartner == null || this.cooldwonForPartnerCycle >= COOLDOWN_FOR_PARTNER_CYCLE_BORDER) {
				if (this.entity.ticksExisted % 4 == 0) {
					Vec3d vec1 = this.entity.getPositionVector().subtract(6.0D, 3.0D, 6.0D);
					Vec3d vec2 = this.entity.getPositionVector().addVector(6.0D, 3.0D, 6.0D);
					AxisAlignedBB aabb = new AxisAlignedBB(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z);
					List<AbstractEntityCQR> friends = this.entity.world.getEntitiesWithinAABB(AbstractEntityCQR.class, aabb, this.predicate);
					if (!friends.isEmpty()) {
						this.talkingPartner = friends.get(this.random.nextInt(friends.size()));
						this.cooldwonForPartnerCycle = 0;
					}
				}
			}

			// check if talking partner is valid and either talk to him or stop talking
			if (this.talkingPartner != null) {
				if (this.talkingPartner.isEntityAlive() && this.entity.getDistance(this.talkingPartner) < 8.0D) {
					this.entity.setChatting(true);
					this.entity.getLookController().setLookPositionWithEntity(this.talkingPartner, 15.0F, 15.0F);
				} else {
					this.talkingPartner = null;
					this.entity.setChatting(false);
				}
			} else {
				this.entity.setChatting(false);
			}
		}
	}

	private boolean isEntityAlly(AbstractEntityCQR possibleAlly) {
		if (possibleAlly == this.entity) {
			return false;
		}
		if(this.entity.getFaction() == null) {
			return false;
		}
		return this.entity.getFaction().isAlly(possibleAlly);
	}

	private boolean isEntityMoving(Entity entity) {
		// motion <= 0.03 is set to 0 but gravity always pulls entities downwards
		return entity.motionX != 0.0D || Math.abs(entity.motionY) > 0.1D || entity.motionZ != 0.0D;
	}

}
