package com.teamcqr.chocolatequestrepoured.client.models.armor;

import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * @author Sir Squidly
 */
public class ModelArmorTurtle extends ModelCustomArmorBase {

	public ModelRenderer shell1;
	public ModelRenderer shell2;
	public ModelRenderer tassetLeft;
	public ModelRenderer tassetRight;

	public ModelArmorTurtle(float scale) {
		super(scale, 64, 64);

		this.shell1 = new ModelRenderer(this, 0, 32);
		this.shell1.setRotationPoint(0.0F, -2.0F, 3.5F);
		this.shell1.addBox(-6.0F, 0.0F, 0.0F, 12, 16, 4, 0.0F);
		this.shell2 = new ModelRenderer(this, 32, 32);
		this.shell2.setRotationPoint(0.0F, 0.0F, 7.5F);
		this.shell2.addBox(-4.0F, 0.0F, 0.0F, 8, 12, 2, 0.0F);
		this.tassetLeft = new ModelRenderer(this, 0, 52);
		this.tassetLeft.mirror = true;
		this.tassetLeft.setRotationPoint(2.0F, -1.2F, -0.2F);
		this.tassetLeft.addBox(-1.5F, 0.0F, -2.5F, 3, 7, 5, 0.0F);
		this.setRotateAngle(this.tassetLeft, 0.0F, 0.0F, -0.4363323129985824F);
		this.tassetRight = new ModelRenderer(this, 0, 52);
		this.tassetRight.setRotationPoint(-2.0F, -1.2F, -0.2F);
		this.tassetRight.addBox(-1.5F, 0.0F, -2.5F, 3, 7, 5, 0.0F);
		this.setRotateAngle(this.tassetRight, 0.0F, 0.0F, 0.4363323129985824F);

		this.bipedHead = new ModelRenderer(this, 30, 46);
		this.bipedHead.setRotationPoint(0F, 0F, 0F);
		this.bipedHead.addBox(-4.5F, -8.0F, -4.0F, 9, 8, 8, 0.75F);

		this.bipedBody.addChild(this.shell2);
		this.bipedBody.addChild(this.shell1);
		this.bipedLeftLeg.addChild(this.tassetLeft);
		this.bipedRightLeg.addChild(this.tassetRight);
	}

}
