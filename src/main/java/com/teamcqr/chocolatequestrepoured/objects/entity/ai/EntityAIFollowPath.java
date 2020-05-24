package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.EnumSet;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.util.math.BlockPos;

public class EntityAIFollowPath extends AbstractCQREntityAI<AbstractEntityCQR> {

	private boolean isReversingPath = false;

	public EntityAIFollowPath(AbstractEntityCQR entity) {
		super(entity);
		//this.setMutexBits(3);
		
		setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.getGuardPathPoints().length == 0) {
			return false;
		}
		return this.entity.hasHomePositionCQR();
	}

	@Override
	public void resetTask() {
		this.entity.getNavigator().clearPath();
	}

	@Override
	public void tick() {
		if (!this.entity.hasPath() && entity.hasHomePositionCQR() && entity.getGuardPathPoints() != null) {
			int index = this.getNextPathIndex();
			this.entity.setCurrentGuardPathTargetPoint(index);
			BlockPos pos = this.entity.getHomePositionCQR().add(this.entity.getGuardPathPoints()[index]);
			this.entity.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 0.75D);
			this.entity.getLookController().setLookPosition(pos.getX(), pos.getY() + entity.getEyeHeight(), pos.getZ(), 30, 30);
		}
	}

	private int getNextPathIndex() {
		BlockPos[] pathPoints = this.entity.getGuardPathPoints();
		int index = this.entity.getCurrentGuardPathTargetPoint() + (this.isReversingPath ? -1 : 1);
		if (this.entity.isGuardPathLoop()) {
			if (index == pathPoints.length) {
				index = 0;
			}
		} else if (index >= pathPoints.length - 1) {
			this.isReversingPath = true;
		} else if (index <= 0) {
			this.isReversingPath = false;
		}
		if(index >= pathPoints.length) {
			index -= 2;
		}
		if(index < 0) {
			index += 2;
		}
		return index;
	}

}
