package com.paragon464.gameserver.model.content.combat.npcs;

import com.paragon464.gameserver.model.Projectiles;
import com.paragon464.gameserver.model.World;
import com.paragon464.gameserver.model.entity.mob.CombatType;
import com.paragon464.gameserver.model.entity.mob.Mob;
import com.paragon464.gameserver.model.entity.mob.NPCFollowing;
import com.paragon464.gameserver.model.entity.mob.masks.Animation;
import com.paragon464.gameserver.model.entity.mob.masks.Hits;
import com.paragon464.gameserver.model.entity.mob.npc.NPC;
import com.paragon464.gameserver.model.entity.mob.player.Player;
import com.paragon464.gameserver.model.content.combat.NPCAttackLayout;
import com.paragon464.gameserver.model.content.combat.data.Formulas;
import com.paragon464.gameserver.model.pathfinders.TileControl;
import com.paragon464.gameserver.tickable.Tickable;
import com.paragon464.gameserver.util.NumberUtils;

public class FlockleaderGeerin extends NPCAttackLayout {

    @Override
    public void executeAttacks(NPC npc, Mob mainTarget) {
        CombatType combatType = getCombatType(npc, mainTarget);
        npc.getCombatState().setCombatType(combatType);
        npc.setInteractingMob(mainTarget);
        if (!isWithinRadius(npc, mainTarget)) {
            return;
        } else {
            npc.getWalkingQueue().reset();
        }
        if (npc.getCombatState().getAttackTimer() > 0) {
            return;
        }
        if (!canAttack(npc, mainTarget)) {
            return;
        }
        int projectile = 1190;
        Projectiles rangeProj = Projectiles.create(npc.getPosition(), mainTarget.getPosition(), mainTarget, projectile,
            32, 80, 50, 70, 34);
        npc.executeProjectile(rangeProj);
        int anim = npc.getCombatDefinition().getAttackAnim();
        npc.playAnimation(anim, Animation.AnimationPriority.HIGH);
        npc.getCombatState().setAttackTimer(npc.getCombatDefinition().getAttackSpeed());
        final Hits.Hit hit = getDamage(npc, mainTarget);
        handleInitEffects(npc, mainTarget, hit);
        World.getWorld().submit(new Tickable(1) {
            @Override
            public void execute() {
                this.stop();
                mainTarget.inflictDamage(hit, false);
                handleEndEffects(npc, mainTarget, hit);
            }
        });
    }

    @Override
    public CombatType getCombatType(NPC npc, Mob target) {
        return CombatType.RANGED;
    }

    @Override
    public boolean isWithinRadius(NPC npc, Mob target) {
        return !TileControl.locationOccupied(npc, target) && npc.getPosition().isWithinRadius(target, 8);
    }

    @Override
    public Hits.Hit getDamage(NPC npc, Mob target) {
        int maxHit = npc.getCombatDefinition().getMaxHit();
        final Hits.Hit hit = new Hits.Hit(npc, NumberUtils.random(maxHit));
        boolean accurate = Formulas.isAccurate(npc, target, CombatType.RANGED, false);
        if (!accurate) {
            hit.setDamage(0);
        }
        if (target.isPlayer()) {
            Player player = (Player) target;
            if (player.getPrayers().isPrayingRange()) {
                hit.setDamage(0);
            }
        }
        return hit;
    }

    @Override
    public void processFollow(NPC npc, Mob target) {
        if (isWithinRadius(npc, target))
            return;
        NPCFollowing.executePathFinding(npc, target, true);
    }

    @Override
    public void loadAttack(NPC npc) {
        npc.getCombatState().setCombatType(CombatType.RANGED);
    }
}
