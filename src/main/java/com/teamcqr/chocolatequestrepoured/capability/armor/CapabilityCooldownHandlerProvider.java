package com.teamcqr.chocolatequestrepoured.capability.armor;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.capability.SerializableCapabilityProvider;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.ArmorCooldownSyncPacket;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Reference.MODID)
public class CapabilityCooldownHandlerProvider extends SerializableCapabilityProvider<CapabilityCooldownHandler> {

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Reference.MODID, "item_cooldown_handler");

	@CapabilityInject(CapabilityCooldownHandler.class)
	public static final Capability<CapabilityCooldownHandler> CAPABILITY_ITEM_COOLDOWN_CQR = null;

	public CapabilityCooldownHandlerProvider(Capability<CapabilityCooldownHandler> capability, CapabilityCooldownHandler instance) {
		super(capability, instance);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(CapabilityCooldownHandler.class, new CapabilityCooldownHandlerStorage(), CapabilityCooldownHandler::new);
	}

	public static CapabilityCooldownHandlerProvider createProvider() {
		return new CapabilityCooldownHandlerProvider(CAPABILITY_ITEM_COOLDOWN_CQR, new CapabilityCooldownHandler());
	}

	@SubscribeEvent
	public static void onLivingUpdateEvent(LivingUpdateEvent event) {
		LivingEntity entity = event.getEntityLiving();
		CapabilityCooldownHandler icapability = entity.getCapability(CAPABILITY_ITEM_COOLDOWN_CQR, null);
		for (Item item : icapability.getItemCooldownMap().keySet()) {
			icapability.reduceCooldown(item);
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.player.world.isRemote) {
			CapabilityCooldownHandler icapability = event.player.getCapability(CAPABILITY_ITEM_COOLDOWN_CQR, null);
			CQRMain.NETWORK.sendTo(new ArmorCooldownSyncPacket(icapability.getItemCooldownMap()), (ServerPlayerEntity) event.player);
		}
	}

}
