package com.teamcqr.chocolatequestrepoured.client.render.entity.boss;

import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelNetherDragonHead;
import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelNetherDragonHeadSkeletal;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRNetherDragon extends RenderLiving<EntityCQRNetherDragon> {

	public static final ResourceLocation TEXTURES_NORMAL = new ResourceLocation((Reference.MODID + ":textures/entity/boss/nether_dragon.png"));
	public static final ResourceLocation TEXTURES_SKELETAL = new ResourceLocation((Reference.MODID + ":textures/entity/boss/nether_dragon_skeletal_head.png"));

	private final static ModelNetherDragonHead modelNormal = new ModelNetherDragonHead();
	private final static ModelNetherDragonHeadSkeletal modelSkeletal = new ModelNetherDragonHeadSkeletal();

	public RenderCQRNetherDragon(RenderManager manager) {
		// super(manager, new ModelNetherDragon(), 0.5F);
		super(manager, modelNormal, 0.5F);
	}

	@Override
	public void doRender(EntityCQRNetherDragon entity, double x, double y, double z, float entityYaw, float partialTicks) {
		
		//Determine model
		if(entity.getSkeleProgress() >= 0) {
			this.mainModel = modelSkeletal;
		} else {
			this.mainModel = modelNormal;
		}
		
		/*if(entity.deathTicks > 0 ) {
			GlStateManager.pushMatrix();
			GlStateManager.color(new Float(0.5F * (0.25 * Math.sin(0.75 * entity.ticksExisted) + 0.5)),0,0, 1F);
		}*/
		if(entity.deathTicks > 0 && entity.deathTicks % 5 == 0) {
			float f = (entity.getRNG().nextFloat() - 0.5F) * 8.0F;
			float f1 = (entity.getRNG().nextFloat() - 0.5F) * 4.0F;
			float f2 = (entity.getRNG().nextFloat() - 0.5F) * 8.0F;
			Minecraft.getMinecraft().world.spawnParticle(entity.getDeathAnimParticles(), entity.posX + (double) f, entity.posY + 2.0D + (double) f1, entity.posZ + (double) f2, 0.0D, 0.0D, 0.0D);
		}
		super.doRender(entity, x, y, z, entity.rotationYawHead, partialTicks);
		/*if(entity.deathTicks > 0 ) {
			GlStateManager.popMatrix();
		}*/
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCQRNetherDragon entity) {
		return entity.getSkeleProgress() < 0 ? TEXTURES_NORMAL : TEXTURES_SKELETAL;
	}

}
