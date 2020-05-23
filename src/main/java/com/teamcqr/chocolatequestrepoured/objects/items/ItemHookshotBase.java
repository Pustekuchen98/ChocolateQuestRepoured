package com.teamcqr.chocolatequestrepoured.objects.items;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.ModSounds;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBullet;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileHookShotHook;
import com.teamcqr.chocolatequestrepoured.util.CQRBlockUtil;
import com.teamcqr.chocolatequestrepoured.util.IRangedWeapon;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.java.games.input.Keyboard;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Copyright (c) 15 Feb 2019
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 */
public abstract class ItemHookshotBase extends Item implements IRangedWeapon {

    private enum BlockGroup {
        BASE_SOLID("BASE_SOLID"),
        BASE_WOOD("BASE_WOOD"),
        BASE_STONE("BASE_STONE"),
        BASE_DIRT("BASE_DIRT");

        private final String configName;
        

        BlockGroup(String configName) {
            this.configName = configName;
        }

        public static Optional<BlockGroup> fromConfigString(String string) {
            for (BlockGroup bg : BlockGroup.values()) {
                if (bg.configName.equalsIgnoreCase(string)) {
                    return Optional.of(bg);
                }
            }
            return Optional.empty();
        }

        public boolean containsBlock(Block block) {
            switch (this) {
                case BASE_SOLID:
                    return true;
                case BASE_WOOD:
                    return CQRBlockUtil.VANILLA_WOOD_SET.contains(block);
                case BASE_STONE:
                    return CQRBlockUtil.VANILLA_STONE_SET.contains(block);
                case BASE_DIRT:
                    return CQRBlockUtil.VANILLA_DIRT_SET.contains(block);
                default:
                    return false;
            }
        }
    }

    protected ArrayList<Block> validLatchBlocks = new ArrayList<>();
    protected ArrayList<BlockGroup> latchGroups = new ArrayList<>();

    public ItemHookshotBase(String hookshotName) {
        this.setMaxDamage(300);
        this.setMaxStackSize(1);

        this.loadPropertiesFromFile(hookshotName);

        this.addPropertyOverride(new ResourceLocation("hook_out"), new IItemPropertyGetter() {
            @Override
            @OnlyIn(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable LivingEntity entityIn) {
                if (entityIn != null && stack.getItem() instanceof ItemHookshotBase) {
                    CompoundNBT stackTag = stack.getTagCompound();
                    if ((stackTag != null) && (stackTag.getBoolean("isShooting"))) {
                        return 1.0f;
                    }
                }

                return 0.0f;
            }
        });
    }

    private void loadPropertiesFromFile(String hookshotName) {
        Collection<File> files = FileUtils.listFiles(CQRMain.CQ_ITEM_FOLDER, new String[]{"properties", "prop", "cfg"}, true);
        //Find the property file that matches this hookshot name
        Optional<File> configFile = files.stream().filter(f -> FilenameUtils.getBaseName(f.getName()).equalsIgnoreCase(hookshotName)).findFirst();

        if (configFile.isPresent()) {
            Properties hookshotConfig = new Properties();
            FileInputStream stream = null;
            try {
                stream = new FileInputStream(configFile.get());
                hookshotConfig.load(stream);

                String[] latchBlocks = PropertyFileHelper.getStringArrayProperty(hookshotConfig, "latchblocks", new String[]{});
                for (String blockType : latchBlocks) {
                    Optional<BlockGroup> groupMatch = BlockGroup.fromConfigString(blockType);
                    if (groupMatch.isPresent()) {
                        this.latchGroups.add(groupMatch.get());
                        continue;
                    }

                    Block blockMatch;
                    //Try the vanilla blocks first
                    blockMatch = Block.getBlockFromName(blockType);
                    if (blockMatch != null) {
                        validLatchBlocks.add(blockMatch);
                        continue;
                    }

                    //Then try CQR blocks
                    //TODO: Create modblocks lookup by name function and check against that

                    //Then try other Mod blocks
                    //TODO: Search other mod block registries

                    CQRMain.logger.error(configFile.get().getName() + ": Invalid latch block: " + blockType);
                }

            } catch (IOException e) {
                CQRMain.logger.error(configFile.get().getName() + ": Failed to load file!");
            }
        }
    }

    public boolean canLatchToBlock(Block block) {
        for (BlockGroup bg : this.latchGroups) {
            if (bg.containsBlock(block)) {
                return true;
            }
        }
        return validLatchBlocks.contains(block);
    }

    @Override
    @OnlyIn(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            tooltip.add(TextFormatting.BLUE + I18n.format(getTranslationKey()));
        } else {
            tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
        }
    }

    public abstract String getTranslationKey();

    public abstract double getHookRange();

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        // System.out.println("Firing hookshot");

        ItemStack stack = playerIn.getHeldItem(handIn);

        this.shoot(stack, worldIn, playerIn);

        return new ActionResult(EnumActionResult.SUCCESS, stack);
    }

    public void shoot(ItemStack stack, World worldIn, PlayerEntity player) {

        if (!worldIn.isRemote) {
            ProjectileHookShotHook hookEntity = new ProjectileHookShotHook(worldIn, player, this, stack);
            hookEntity.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, (float) hookEntity.getTravelSpeed(), 0F);
            player.getCooldownTracker().setCooldown(stack.getItem(), getCooldown());
            worldIn.spawnEntity(hookEntity);
            stack.damageItem(1, player);
        }
        worldIn.playSound(player.posX, player.posY, player.posZ, ModSounds.GUN_SHOOT, SoundCategory.MASTER, 1.0F, 1.0F, false);
    }

    @Override
    public void shoot(World worldIn, LivingEntity shooter, Entity target, Hand handIn) {
        if (!worldIn.isRemote) {
            ProjectileBullet bulletE = new ProjectileBullet(worldIn, shooter, 1/* 1 is Iron bullet */);
            Vec3d v = target.getPositionVector().subtract(shooter.getPositionVector());
            v = v.normalize();
            v = v.scale(3.5D);
            //bulletE.setVelocity(v.x, v.y, v.z);
            bulletE.motionX = v.x;
            bulletE.motionY = v.y;
            bulletE.motionZ = v.z;
            bulletE.velocityChanged = true;
            worldIn.spawnEntity(bulletE);
        }
    }

    @Override
    public SoundEvent getShootSound() {
        return ModSounds.GUN_SHOOT;
    }

}
