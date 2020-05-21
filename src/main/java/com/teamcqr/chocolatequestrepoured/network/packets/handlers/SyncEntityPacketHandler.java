package com.teamcqr.chocolatequestrepoured.network.packets.handlers;

import javax.xml.ws.handler.MessageContext;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.SyncEntityPacket;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;

public class SyncEntityPacketHandler implements IMessageHandler<SyncEntityPacket, IMessage> {

	@Override
	public IMessage onMessage(SyncEntityPacket message, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
			if (CQRMain.proxy.getPlayer(ctx).isCreative()) {
				World world = CQRMain.proxy.getWorld(ctx);
				Entity entity = world.getEntityByID(message.getEntityId());
				if (entity instanceof AbstractEntityCQR) {
					AbstractEntityCQR cqrentity = (AbstractEntityCQR) entity;
					cqrentity.setHealthScale((double) message.getHealthScaling() / 100.0D);
					cqrentity.setDropChance(EquipmentSlotType.HEAD, (float) message.getDropChanceHelm() / 100.0F);
					cqrentity.setDropChance(EquipmentSlotType.CHEST, (float) message.getDropChanceChest() / 100.0F);
					cqrentity.setDropChance(EquipmentSlotType.LEGS, (float) message.getDropChanceLegs() / 100.0F);
					cqrentity.setDropChance(EquipmentSlotType.FEET, (float) message.getDropChanceFeet() / 100.0F);
					cqrentity.setDropChance(EquipmentSlotType.MAINHAND, (float) message.getDropChanceMainhand() / 100.0F);
					cqrentity.setDropChance(EquipmentSlotType.OFFHAND, (float) message.getDropChanceOffhand() / 100.0F);
					cqrentity.setSizeVariation((float) message.getSizeScaling() / 100.0F); 
				}
			}
		});
		return null;
	}

}
