package com.teamcqr.chocolatequestrepoured.client.render.projectile;

import com.mojang.blaze3d.platform.GlStateManager;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileHookShotHook;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.PlayerEntitySP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class RenderProjectileHookShotHook extends Render<ProjectileHookShotHook> {
	public ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/cannon_ball.png");
	
	public RenderProjectileHookShotHook(RenderManager renderManager) {
		super(renderManager);
	}
	
	//TODO: Make this work the same as the snowball renderer so we can actually use item models for the model

	@Override
	public void doRender(ProjectileHookShotHook entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		this.bindEntityTexture(entity);
		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(.5F, .5F, .5F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

		//This seems to render the texture....
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		bufferbuilder.pos(-0.5D, -0.25D, 0.0D).tex(0.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		bufferbuilder.pos(0.5D, -0.25D, 0.0D).tex(1.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		bufferbuilder.pos(0.5D, 0.75D, 0.0D).tex(1.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		bufferbuilder.pos(-0.5D, 0.75D, 0.0D).tex(0.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		tessellator.draw();
		
		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();

		renderChain(entity);
		
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	private void renderChain(ProjectileHookShotHook entity) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
				GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableTexture2D();

		Vec3d v = entity.getPositionVector().subtract(entity.getShooterPosition());//.add(new Vec3d(0,1.7,0));
		int iterations = (int) Math.ceil(v.lengthVector());
		v = v.normalize();
		Vec3d loc = entity.getShooterPosition().add(v);

		//Offsets for camera
		PlayerEntitySP player = Minecraft.getMinecraft().player;
        double xo = player.lastTickPosX + (player.posX - player.lastTickPosX) /* (double)partialTicks*/;
        double yo = player.lastTickPosY + (player.posY - player.lastTickPosY) /* (double)partialTicks*/;
        double zo = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) /* (double)partialTicks*/;
		
		for(int i =  1; i < iterations; i++) {
			//Render code here -> render a small cube
			RenderGlobal.renderFilledBox((new AxisAlignedBB(loc.subtract(0.125, 0.125, 0.125), loc.add(new Vec3d(0.125, 0.125, 0.125))).offset(-xo, -yo, -zo)), 96, 96, 96, 1);

			loc = loc.add(v);
		}

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(ProjectileHookShotHook entity) {
		return this.TEXTURE;
	}
}