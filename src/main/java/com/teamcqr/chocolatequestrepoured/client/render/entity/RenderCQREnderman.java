package com.teamcqr.chocolatequestrepoured.client.render.entity;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQREntityArmor;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQRHeldItem;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQREnderman;

import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Side.CLIENT)
public class RenderCQREnderman extends RenderCQREntity<EntityCQREnderman> {

	public RenderCQREnderman(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelEnderman(0.0F), 0.5F, "entity_mob_cqrenderman", 1.0D, 1.0D);
		List<LayerRenderer<?>> toRemove = new ArrayList<LayerRenderer<?>>();
		for (LayerRenderer<?> layer : this.layerRenderers) {
			if (layer instanceof LayerBipedArmor || layer instanceof LayerHeldItem) {
				toRemove.add(layer);
			}
		}
		for (LayerRenderer<?> layer : toRemove) {
			this.layerRenderers.remove(layer);
		}

		this.addLayer(new LayerCQRHeldItem(this));
		/*
		this.addLayer(new LayerBipedArmor(this) {
			@Override
			protected void initArmor() {
				this.modelLeggings = new ModelCQREndermanArmor(0.5F, true);
				this.modelArmor = new ModelCQREndermanArmor(1.0F, false);
			}
		});
		*/
		this.addLayer(new LayerCQREntityArmor(this) {
			@Override
			public void setupRightLegOffsets(ModelRenderer modelRenderer, EquipmentSlotType slot) {
				if (slot == EquipmentSlotType.LEGS) {
					this.rotate(modelRenderer, false);
					GlStateManager.translate(0.0D, 0.525D, 0.0D);
					GlStateManager.scale(1.0D, 2.6D, 1.0D);
					this.rotate(modelRenderer, true);
				} else if (slot == EquipmentSlotType.FEET) {
					this.rotate(modelRenderer, false);
					GlStateManager.translate(0.0D, 1.1D, 0.0D);
					this.rotate(modelRenderer, true);
				}
			}

			@Override
			public void setupLeftLegOffsets(ModelRenderer modelRenderer, EquipmentSlotType slot) {
				if (slot == EquipmentSlotType.LEGS) {
					this.rotate(modelRenderer, false);
					GlStateManager.translate(0.0D, 0.525D, 0.0D);
					GlStateManager.scale(1.0D, 2.6D, 1.0D);
					this.rotate(modelRenderer, true);
				} else if (slot == EquipmentSlotType.FEET) {
					this.rotate(modelRenderer, false);
					GlStateManager.translate(0.0D, 1.1D, 0.0D);
					this.rotate(modelRenderer, true);
				}
			}
		});
	}

}
