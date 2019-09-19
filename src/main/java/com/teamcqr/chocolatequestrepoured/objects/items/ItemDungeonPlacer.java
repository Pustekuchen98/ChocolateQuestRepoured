package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.HashMap;
import java.util.Map.Entry;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.DungeonSyncPacket;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class ItemDungeonPlacer extends Item {

	public static HashMap<String, Integer> dungeonMap = new HashMap<String, Integer>();
	public static final int HIGHEST_ICON_NUMBER = 16;
	private int iconID;

	public ItemDungeonPlacer(int iconID) {
		setMaxStackSize(1);
		this.iconID = iconID;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			for (Entry<String, Integer> entry : dungeonMap.entrySet()) {
				int iconID = entry.getValue() <= HIGHEST_ICON_NUMBER ? entry.getValue() : 0;
				if (iconID == this.iconID) {
					ItemStack stack = new ItemStack(this);
					NBTTagCompound compound = new NBTTagCompound();
					compound.setString("dungeonName", entry.getKey());
					compound.setInteger("iconID", iconID);
					stack.setTagCompound(compound);
					items.add(stack);
				}
			}
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if (stack.hasTagCompound()) {
			NBTTagCompound compound = stack.getTagCompound();
			return "Dungeon Placer - " + compound.getString("dungeonName");
		}
		return "Dungeon Placer";
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (!worldIn.isRemote) {
			ItemStack stack = playerIn.getHeldItem(handIn);

			if (stack.hasTagCompound()) {
				String dungeonName = stack.getTagCompound().getString("dungeonName");
				DungeonBase dungeon = CQRMain.dungeonRegistry.getDungeon(dungeonName);

				if (dungeon != null) {
					double eye = playerIn.getEyeHeight();
					Vec3d pos = playerIn.getPositionVector();
					Vec3d look = playerIn.getLookVec();

					RayTraceResult result = worldIn.rayTraceBlocks(pos.addVector(0.0D, eye, 0.0D),
							pos.addVector(64.0D * look.x, eye + 64.0D * look.y, 64.0D * look.z));

					if (result != null) {
						dungeon.generate(result.getBlockPos(), worldIn);

						playerIn.getCooldownTracker().setCooldown(stack.getItem(), 30);
					}
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@EventBusSubscriber(modid = Reference.MODID)
	public static class DungeonPlacerSyncHandler {

		@SubscribeEvent
		public static void syncDungeonPlacers(PlayerEvent.PlayerLoggedInEvent event) {
			if (!event.player.world.isRemote) {
				CQRMain.NETWORK.sendTo(new DungeonSyncPacket(CQRMain.dungeonRegistry.dungeonList),
						(EntityPlayerMP) event.player);
			}
		}

	}

}