package com.teamcqr.chocolatequestrepoured.client.init;

import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelGiantTortoise;
import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelLich;
import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelNecromancer;
import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelPigMage;
import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelWalkerKing;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderBubble;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRBoarman;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREnderman;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRGolem;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRGremlin;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRIllager;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRMandril;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRMinotaur;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQROgre;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRSkeleton;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRSpectre;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRTriton;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQRWasp;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderColoredLightningBolt;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderFlyingSkull;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderSummoningCircle;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderWalkerTornado;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRGiantTortoise;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRGiantTortoisePart;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRMage;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRNetherDragonSegment;
import com.teamcqr.chocolatequestrepoured.client.render.entity.boss.RenderCQRWalkerKing;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mounts.RenderGiantEndermite;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mounts.RenderGiantSilverfish;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mounts.RenderGiantSilverfishGreen;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mounts.RenderGiantSilverfishRed;
import com.teamcqr.chocolatequestrepoured.client.render.entity.mounts.RenderPollo;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileBubble;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileBullet;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileCannonBall;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileEarthQuake;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileFirewallPart;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileHookShotHook;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileHotFireball;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectilePoisonSpell;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileSpiderBall;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileVampiricSpell;
import com.teamcqr.chocolatequestrepoured.client.render.tileentity.TileEntityExporterChestRenderer;
import com.teamcqr.chocolatequestrepoured.client.render.tileentity.TileEntityExporterRenderer;
import com.teamcqr.chocolatequestrepoured.client.render.tileentity.TileEntityForceFieldNexusRenderer;
import com.teamcqr.chocolatequestrepoured.client.render.tileentity.TileEntityTableRenderer;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRBoarmage;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRLich;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNecromancer;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRWalkerKing;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.subparts.EntityCQRGiantTortoisePart;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.subparts.EntityCQRNetherDragonSegment;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityBubble;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityCQRWasp;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityColoredLightningBolt;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityFlyingSkullMinion;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntitySummoningCircle;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityWalkerKingIllusion;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityWalkerTornado;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRBoarman;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRDummy;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRDwarf;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQREnderman;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRGolem;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRGremlin;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRIllager;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRMandril;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRMinotaur;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRMummy;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRNPC;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQROgre;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQROrc;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRPirate;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRSkeleton;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRSpectre;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRTriton;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRWalker;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRZombie;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBubble;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBullet;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileCannonBall;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileEarthQuake;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileFireWallPart;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileHookShotHook;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileHotFireball;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectilePoisonSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileSpiderBall;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileVampiricSpell;
import com.teamcqr.chocolatequestrepoured.objects.mounts.EntityGiantEndermite;
import com.teamcqr.chocolatequestrepoured.objects.mounts.EntityGiantSilverfishGreen;
import com.teamcqr.chocolatequestrepoured.objects.mounts.EntityGiantSilverfishNormal;
import com.teamcqr.chocolatequestrepoured.objects.mounts.EntityGiantSilverfishRed;
import com.teamcqr.chocolatequestrepoured.objects.mounts.EntityPollo;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporterChest;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityForceFieldNexus;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityTable;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ModEntityRenderers {

	public static void registerRenderers() {
		registerTileRenderers();
		registerProjectileAndMiscRenderers();
		registerEntityRenderers();
		registerBossRenderers();
	}

	protected static void registerTileRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTable.class, new TileEntityTableRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExporter.class, new TileEntityExporterRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityForceFieldNexus.class, new TileEntityForceFieldNexusRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExporterChest.class, new TileEntityExporterChestRenderer());
	}

	protected static void registerProjectileAndMiscRenderers() {
		// Projectiles

		RenderingRegistry.registerEntityRenderingHandler(ProjectileBullet.class, renderManager -> new RenderProjectileBullet(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(ProjectileCannonBall.class, renderManager -> new RenderProjectileCannonBall(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(ProjectileEarthQuake.class, renderManager -> new RenderProjectileEarthQuake(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(ProjectilePoisonSpell.class, renderManager -> new RenderProjectilePoisonSpell(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(ProjectileSpiderBall.class, renderManager -> new RenderProjectileSpiderBall(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(ProjectileVampiricSpell.class, renderManager -> new RenderProjectileVampiricSpell(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(ProjectileFireWallPart.class, renderManager -> new RenderProjectileFirewallPart(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(ProjectileHookShotHook.class, renderManager -> new RenderProjectileHookShotHook(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(ProjectileBubble.class, renderManager -> new RenderProjectileBubble(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(ProjectileHotFireball.class, renderManager -> new RenderProjectileHotFireball(renderManager));

		// Miscs
		RenderingRegistry.registerEntityRenderingHandler(EntitySummoningCircle.class, renderManager -> new RenderSummoningCircle(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityFlyingSkullMinion.class, renderManager -> new RenderFlyingSkull(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBubble.class, renderManager -> new RenderBubble(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityWalkerKingIllusion.class, renderManager -> new RenderCQREntity<EntityWalkerKingIllusion>(renderManager, new ModelWalkerKing(0F), 0F, "boss/walker_king", 1, 1));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRWasp.class, renderManager -> new RenderCQRWasp(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityWalkerTornado.class, renderManager -> new RenderWalkerTornado(renderManager));
		
		RenderingRegistry.registerEntityRenderingHandler(EntityColoredLightningBolt.class, RenderColoredLightningBolt::new);
	}

	protected static void registerEntityRenderers() {
		// Dummy
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRDummy.class, renderManager -> new RenderCQREntity<EntityCQRDummy>(renderManager, "entity_mob_cqrdummy"));
		// Dwarf
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRDwarf.class, renderManager -> new RenderCQREntity<EntityCQRDwarf>(renderManager, "entity_mob_cqrdwarf", 0.9D, 0.65D));
		// Enderman
		RenderingRegistry.registerEntityRenderingHandler(EntityCQREnderman.class, renderManager -> new RenderCQREnderman(renderManager));
		// Goblin
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRGremlin.class, renderManager -> new RenderCQRGremlin(renderManager));
		// Golem
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRGolem.class, renderManager -> new RenderCQRGolem(renderManager));
		// Illager
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRIllager.class, renderManager -> new RenderCQRIllager(renderManager));
		// Inquisiton
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRNPC.class, renderManager -> new RenderCQREntity<EntityCQRNPC>(renderManager, "entity_mob_cqrinquisitor"));
		// Minotaur
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRMinotaur.class, renderManager -> new RenderCQRMinotaur(renderManager));
		// Mandril
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRMandril.class, renderManager -> new RenderCQRMandril(renderManager));
		// Mummy
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRMummy.class, renderManager -> new RenderCQREntity<EntityCQRMummy>(renderManager, "entity_mob_cqrmummy"));
		// Ogre
		RenderingRegistry.registerEntityRenderingHandler(EntityCQROgre.class, renderManager -> new RenderCQROgre(renderManager));
		// Orc
		RenderingRegistry.registerEntityRenderingHandler(EntityCQROrc.class, renderManager -> new RenderCQREntity<EntityCQROrc>(renderManager, "entity_mob_cqrorc", false));
		// Boarman
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRBoarman.class, renderManager -> new RenderCQRBoarman(renderManager, "entity_mob_cqrboarman"));
		// Pirate
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRPirate.class, renderManager -> new RenderCQREntity<EntityCQRPirate>(renderManager, "entity_mob_cqrpirate"));
		// Skeleton
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRSkeleton.class, renderManager -> new RenderCQRSkeleton(renderManager));
		// Spectre
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRSpectre.class, renderManager -> new RenderCQRSpectre(renderManager));
		// Triton
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRTriton.class, renderManager -> new RenderCQRTriton(renderManager));
		// Walker
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRWalker.class, renderManager -> new RenderCQREntity<EntityCQRWalker>(renderManager, "entity_mob_cqrwalker"));
		// Zombie
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRZombie.class, renderManager -> new RenderCQREntity<EntityCQRZombie>(renderManager, "entity_mob_cqrzombie"));

		// Mounts
		RenderingRegistry.registerEntityRenderingHandler(EntityGiantEndermite.class, renderManager -> new RenderGiantEndermite(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityGiantSilverfishNormal.class, renderManager -> new RenderGiantSilverfish(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityGiantSilverfishGreen.class, renderManager -> new RenderGiantSilverfishGreen(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityGiantSilverfishRed.class, renderManager -> new RenderGiantSilverfishRed(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityPollo.class, renderManager -> new RenderPollo(renderManager));
	}

	protected static void registerBossRenderers() {
		// Nether Dragon
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRNetherDragon.class, renderManager -> new RenderCQRNetherDragon(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRNetherDragonSegment.class, renderManager -> new RenderCQRNetherDragonSegment(renderManager));

		// Giant Tortoise
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRGiantTortoise.class, renderManager -> new RenderCQRGiantTortoise(renderManager, new ModelGiantTortoise(), 1.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRGiantTortoisePart.class, renderManager -> new RenderCQRGiantTortoisePart(renderManager));

		// Lich
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRLich.class, renderManager -> new RenderCQRMage(renderManager, new ModelLich(0F), "entity_cqr_lich"));

		// Boar Mage
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRBoarmage.class, renderManager -> new RenderCQRMage(renderManager, new ModelPigMage(0F), "entity_cqr_boar_mage"));

		// Necromancer
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRNecromancer.class, renderManager -> new RenderCQRMage(renderManager, new ModelNecromancer(0F), "entity_cqr_necromancer"));
		
		// Walker King
		RenderingRegistry.registerEntityRenderingHandler(EntityCQRWalkerKing.class, renderManager -> new RenderCQRWalkerKing(renderManager, new ModelWalkerKing(0F), "boss/walker_king"));
	}

}
