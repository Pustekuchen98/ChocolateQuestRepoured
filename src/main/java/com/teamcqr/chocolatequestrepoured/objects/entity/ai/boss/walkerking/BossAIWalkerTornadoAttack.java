package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.walkerking;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRWalkerKing;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityWalkerTornado;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.util.math.Vec3d;

public class BossAIWalkerTornadoAttack extends AbstractCQREntityAI<EntityCQRWalkerKing> {
	
	protected static final int MIN_TORNADOES = 3;
	protected static final int MAX_TORNADOES = 6;
	protected static final int MIN_COOLDOWN = 120;
	protected static final int MAX_COOLDOWN = 240;
	
	protected int cooldown = MIN_COOLDOWN + (MAX_COOLDOWN - MIN_COOLDOWN) /2; 

	public BossAIWalkerTornadoAttack(EntityCQRWalkerKing entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if(entity != null && entity.getAttackTarget() != null && !entity.getAttackTarget().isDead) {
			cooldown--;
			return cooldown <= 0;
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && shouldExecute();
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		spawnTornadoes(DungeonGenUtils.getIntBetweenBorders(MIN_TORNADOES, MAX_TORNADOES +1, entity.getRNG()));
		cooldown = DungeonGenUtils.getIntBetweenBorders(MIN_COOLDOWN, MAX_COOLDOWN, entity.getRNG());
	}
	
	private void spawnTornadoes(int count) {
		//System.out.println("Executing");
		double angle = 90 / (count -1);
		Vec3d velocity = this.entity.getAttackTarget().getPositionVector().subtract(this.entity.getPositionVector());
		velocity = VectorUtil.rotateVectorAroundY(velocity, -45);
		for(int i = 0; i < count; i++) {
			Vec3d v = VectorUtil.rotateVectorAroundY(velocity, angle * i);
			Vec3d p = entity.getPositionVector().add(v.normalize().scale(0.5));
			v = v.normalize().scale(0.25);
			//System.out.println("V=" + v.toString());
			EntityWalkerTornado tornado = new EntityWalkerTornado(entity.world);
			tornado.setOwner(entity.getUniqueID());
			tornado.setPosition(p.x, p.y, p.z);
			tornado.setVelocity(v);
			entity.world.spawnEntity(tornado);
		}
	}
	

}

