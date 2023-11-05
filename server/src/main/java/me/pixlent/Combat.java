package me.pixlent;

import net.kyori.adventure.text.Component;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.item.attribute.ItemAttribute;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;

public class Combat {

    public static void handle(EventNode<Event> eventNode) {
        eventNode.addListener(EntityAttackEvent.class, event -> {
            if (!(event.getEntity() instanceof Player player)) {
                return;
            }
            if (!(event.getTarget() instanceof LivingEntity target)) {
                return;
            }

            double attackSpeed = player.getAttribute(Attribute.ATTACK_SPEED).getValue();
            for (ItemAttribute itemAttribute : player.getItemInMainHand().meta().getAttributes()) {
                if (itemAttribute.attribute() == Attribute.ATTACK_SPEED) {
                    attackSpeed = itemAttribute.amount();
                    break;
                }
            }

            int t = 40;
            double T = 20 / attackSpeed;
            double damageMultiplier = Math.max(0.2, Math.min(1, 0.2 + Math.pow(((t + 0.5) / T), 2) * 0.8));

            double damageAmount = player.getAttribute(Attribute.ATTACK_DAMAGE).getValue() * damageMultiplier;
            for (ItemAttribute itemAttribute : player.getItemInMainHand().meta().getAttributes()) {
                if (itemAttribute.attribute() == Attribute.ATTACK_DAMAGE) {
                    damageAmount = itemAttribute.amount() * damageMultiplier;
                    break;
                }
            }

            boolean isCrit = false;
            if (player.getVelocity().y() < -1.568 && !player.isSprinting()) {
                //crit?
                damageAmount = damageAmount * 1.5;
                player.sendMessage("isCrit " + player.getVelocity());
                isCrit = true;
            }

            if (!(player.getHealth() - damageAmount <= 0)) {
                target.damage(DamageType.fromPlayer(player), (float) damageAmount);
                TextDisplay damageIndicator = new TextDisplay()
                        .text(damageAmount + "\uD83D\uDDE1")
                        .billboardConstraints(AbstractDisplayMeta.BillboardConstraints.VERTICAL)
                        .gravity(false);

                damageIndicator.build(target.getInstance(), target.getPosition().add(0, 1, 0));
                damageIndicator.destroy(1);

                target.setCustomName(Component.text(target.getHealth() + "/" + target.getAttribute(Attribute.MAX_HEALTH).getValue()));

                //player.sendMessage(player.getAttribute(Attribute.ATTACK_DAMAGE).toString() + " / " + player.getAttribute(Attribute.ATTACK_SPEED).toString());
                //player.sendMessage(damageMultiplier + " / " + damageAmount + " / " + attackSpeed);

                if (isCrit) {
                    target.getViewers().forEach(player1 -> player1.sendPacket(ParticleCreator.createParticlePacket(Particle.CRIT, target.getPosition().x(), target.getPosition().y(), target.getPosition().z(), 0, (float) target.getEyeHeight(), 0, 100)));
                }

                target.takeKnockback((float) 0.4, Math.sin(player.getPosition().yaw() * (Math.PI/180)), -(Math.cos(player.getPosition().yaw() * (Math.PI / 180))));
            } else {
                target.kill();
            }
        });
    }
}