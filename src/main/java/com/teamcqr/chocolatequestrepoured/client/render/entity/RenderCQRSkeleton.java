package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRSkeleton;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRSkeleton;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRSkeleton extends RenderCQREntity<EntityCQRSkeleton> {

	public RenderCQRSkeleton(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRSkeleton(), 0.5F, "entity_mob_cqrskeleton", 1.0D, 1.0D);
	}

}
