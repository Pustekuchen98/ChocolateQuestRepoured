package com.teamcqr.chocolatequestrepoured.client.models.armor;

import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.GlStateManager;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQREntityArmor;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemCrown;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

/**
 * ModelCrown - DerToaster
 * Created using Tabula 7.0.1
 */
public class ModelCrown extends ModelCustomArmorBase {
	
    public ModelRenderer crownBorderFront;
    public ModelRenderer crownBorderBack;
    public ModelRenderer crownBorderRight;
    public ModelRenderer crownBorderLeft;
    public ModelRenderer crownUpper;
    public ModelRenderer crownJewelFrontBase;
    public ModelRenderer crownJewelFront;

    public ModelCrown(float scale) {
    	super(scale, 32, 32);
    	
    	this.crownBorderLeft = new ModelRenderer(this, 18, 16);
        this.crownBorderLeft.setRotationPoint(3.0F, 1.3F, 0.0F);
        this.crownBorderLeft.addBox(0.0F, -9.0F, -3.0F, 1, 2, 6, 0.0F);
        this.crownJewelFrontBase = new ModelRenderer(this, 0, 0);
        this.crownJewelFrontBase.setRotationPoint(0.0F, -9.2F, -0.5F);
        this.crownJewelFrontBase.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 1, 0.0F);
        this.crownBorderRight = new ModelRenderer(this, 18, 24);
        this.crownBorderRight.setRotationPoint(-3.0F, 1.3F, 0.0F);
        this.crownBorderRight.addBox(-1.0F, -9.0F, -3.0F, 1, 2, 6, 0.0F);
        this.crownUpper = new ModelRenderer(this, 0, 0);
        this.crownUpper.setRotationPoint(0.0F, -2.7F, 0.0F);
        this.crownUpper.addBox(-4.0F, -7.0F, -4.0F, 8, 2, 8, 0.0F);
        this.crownJewelFront = new ModelRenderer(this, 0, 3);
        this.crownJewelFront.setRotationPoint(0.0F, 0.0F, -0.7F);
        this.crownJewelFront.addBox(-0.5F, 0.5F, -0.5F, 1, 1, 1, 0.0F);
        this.crownBorderBack = new ModelRenderer(this, 0, 24);
        this.crownBorderBack.setRotationPoint(0.0F, 1.3F, 2.0F);
        this.crownBorderBack.addBox(-4.0F, -9.0F, 1.0F, 8, 2, 1, 0.0F);
        this.bipedHead = new ModelRenderer(this, 0, 10);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHead.addBox(-3.0F, -7.0F, -3.0F, 6, 1, 6, 0.0F);
        this.crownBorderFront = new ModelRenderer(this, 0, 29);
        this.crownBorderFront.setRotationPoint(0.0F, 1.3F, -3.0F);
        this.crownBorderFront.addBox(-4.0F, -9.0F, -1.0F, 8, 2, 1, 0.0F);
        
        this.bipedHead.addChild(this.crownBorderLeft);
        this.crownBorderFront.addChild(this.crownJewelFrontBase);
        this.bipedHead.addChild(this.crownBorderRight);
        this.bipedHead.addChild(this.crownUpper);
        this.crownJewelFrontBase.addChild(this.crownJewelFront);
        this.bipedHead.addChild(this.crownBorderBack);
        this.bipedHead.addChild(this.crownBorderFront);
        this.bipedHeadwear.showModel = false;
        this.bipedHeadwear.isHidden = true;
    }
    
    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    	if(getAttachedItemModel(entityIn) != null) {
    		getAttachedItemModel(entityIn).render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    	}
    	GlStateManager.pushMatrix();
    	GlStateManager.scale(1.2, 1.2, 1.2);
    	super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    	GlStateManager.popMatrix();
    }
    
    @Override
    public void render(Entity entityIn, float scale, LayerCQREntityArmor layer, ModelBiped model, EquipmentSlotType slot) {
    	if(getAttachedItemModel(entityIn) != null && getAttachedItemModel(entityIn) instanceof ModelCustomArmorBase) {
    		((ModelCustomArmorBase)getAttachedItemModel(entityIn)).render(entityIn, scale, layer, model, slot);
    	}
    	GlStateManager.pushMatrix();
    	GlStateManager.scale(1.2, 1.2, 1.2);
    	super.render(entityIn, scale, layer, model, slot);
    	GlStateManager.popMatrix();
    }
    
    @Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
    	if(getAttachedItemModel(entityIn) != null) {
    		getAttachedItemModel(entityIn).setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
    	}
    	super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
	}

    @Nullable
    protected ModelBiped getAttachedItemModel(Entity entityIn) {
    	if(entityIn instanceof LivingEntity) {
    		ItemStack helmet = ((LivingEntity) entityIn).getItemStackFromSlot(EquipmentSlotType.HEAD);
    		if(helmet != null && !helmet.isEmpty() && helmet.getItem() instanceof ItemCrown) {
    			ItemCrown crown = (ItemCrown) ModItems.KING_CROWN;
    			if(crown.getAttachedItem(helmet) != null) {
    				Item attachedHelmet = crown.getAttachedItem(helmet);
    				if(attachedHelmet instanceof ItemArmor) {
    					ModelBiped attachedModel = attachedHelmet.getArmorModel((LivingEntity) entityIn, new ItemStack(attachedHelmet, 1), EquipmentSlotType.HEAD, null);
    					if(attachedModel != null) {
    						return attachedModel;
    					}
    				}
    			}
    		}
    	}
    	return null;
    }
    
}
