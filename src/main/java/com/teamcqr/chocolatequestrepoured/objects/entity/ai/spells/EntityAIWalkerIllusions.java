package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import java.util.function.Consumer;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.TargetUtil;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRWalkerKing;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityWalkerKingIllusion;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class EntityAIWalkerIllusions extends AbstractEntityAISpell<AbstractEntityCQR> implements IEntityAISpellAnimatedVanilla {

	public EntityAIWalkerIllusions(AbstractEntityCQR entity, int cooldown, int chargeUpTicks) {
		super(entity, true, false, cooldown, chargeUpTicks, 1);
	}

	@Override
	public void startCastingSpell() {
		// entity.getAttackTarget().addPotionEffect(new PotionEffect(Potion.getPotionById(15), 40));
		this.entity.world.getEntitiesInAABBexcluding(this.entity, new AxisAlignedBB(this.entity.getPosition().add(-20, -10, -20), this.entity.getPosition().add(20, 10, 20)), TargetUtil.createPredicateNonAlly(this.entity.getFaction()))
				.forEach(new Consumer<Entity>() {

					@Override
					public void accept(Entity t) {
						if (t instanceof LivingEntity) {
							((LivingEntity) t).addPotionEffect(new EffectInstance(Effects.BLINDNESS, 40));
						}
					}
				});
		Vec3d v = new Vec3d(2.5, 0, 0);
		for (int i = 0; i < 3; i++) {
			Vec3d pos = this.entity.getPositionVector().add(VectorUtil.rotateVectorAroundY(v, 120 * i));
			EntityWalkerKingIllusion illusion = new EntityWalkerKingIllusion(1200, (EntityCQRWalkerKing) this.entity, this.entity.getEntityWorld());
			illusion.setPosition(pos.x, pos.y, pos.z);
			this.entity.world.addEntity(illusion);
		}
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED;
	}

	@Override
	protected SoundEvent getStartCastingSound() {
		return SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL;
	}

	@Override
	public int getWeight() {
		return 10;
	}

	@Override
	public boolean ignoreWeight() {
		return false;
	}

	@Override
	public float getRed() {
		return 0.55F;
	}

	@Override
	public float getGreen() {
		return 0.0F;
	}

	@Override
	public float getBlue() {
		return 0.8F;
	}

}
