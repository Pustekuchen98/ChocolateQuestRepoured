package com.teamcqr.chocolatequestrepoured.client.init;

import com.teamcqr.chocolatequestrepoured.client.models.armor.ModelArmorBull;
import com.teamcqr.chocolatequestrepoured.client.models.armor.ModelArmorHeavy;
import com.teamcqr.chocolatequestrepoured.client.models.armor.ModelArmorInquisition;
import com.teamcqr.chocolatequestrepoured.client.models.armor.ModelArmorSpider;
import com.teamcqr.chocolatequestrepoured.client.models.armor.ModelArmorTransparent;
import com.teamcqr.chocolatequestrepoured.client.models.armor.ModelArmorTurtle;
import com.teamcqr.chocolatequestrepoured.client.models.armor.ModelBackpack;
import com.teamcqr.chocolatequestrepoured.client.models.armor.ModelCrown;

public class ModArmorModels {

	public static ModelBackpack backpack = new ModelBackpack(0.0F);

	public static ModelArmorTransparent slimeArmor = new ModelArmorTransparent(0.75F);
	public static ModelArmorTransparent slimeArmorLegs = new ModelArmorTransparent(0.375F);

	public static ModelArmorTurtle turtleArmor = new ModelArmorTurtle(1.0F);
	public static ModelArmorTurtle turtleArmorLegs = new ModelArmorTurtle(0.5F);

	public static ModelArmorBull bullArmor = new ModelArmorBull(1.0F);
	public static ModelArmorBull bullArmorLegs = new ModelArmorBull(0.5F);

	// This is supposed! Reason: If future version have a separate model for the heavy diamond armor, adaption isnt really needed
	public static ModelArmorHeavy heavyIronArmor = new ModelArmorHeavy(1.0F);
	public static ModelArmorHeavy heavyIronArmorLegs = new ModelArmorHeavy(0.5F);

	public static ModelArmorHeavy heavyDiamondArmor = new ModelArmorHeavy(1.0F);
	public static ModelArmorHeavy heavyDiamondArmorLegs = new ModelArmorHeavy(0.5F);

	public static ModelArmorInquisition inquisitionArmor = new ModelArmorInquisition(1.0F);
	public static ModelArmorInquisition inquisitionArmorLegs = new ModelArmorInquisition(0.5F);

	public static ModelArmorSpider spiderArmor = new ModelArmorSpider(1.0F);
	public static ModelArmorSpider spiderArmorLegs = new ModelArmorSpider(0.5F);

	public static ModelCrown crown = new ModelCrown(1.0F);

}
