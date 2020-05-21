package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

public abstract class AbstractEntityAISpellAnimatedLLibrary<T extends AbstractEntityCQR & IAnimatedEntity> extends AbstractEntityAISpell<T> {

	public AbstractEntityAISpellAnimatedLLibrary(T entity, boolean needsTargetToStart, boolean needsTargetToContinue, int cooldown, int chargingTicks, int castingTicks) {
		super(entity, needsTargetToStart, needsTargetToContinue, cooldown, chargingTicks, castingTicks);
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!super.shouldExecute()) {
			return false;
		}
		return this.entity.getAnimationTick() < this.getAnimation().getDuration();
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, this.getAnimation());
		this.entity.setAnimationTick(0);
	}

	@Override
	public void resetTask() {
		super.resetTask();
		AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, IAnimatedEntity.NO_ANIMATION);
	}

	public abstract Animation getAnimation();

}
