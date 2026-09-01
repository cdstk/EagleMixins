package eaglemixins.mixin.bettercombatmod;

import bettercombat.mod.util.Helpers;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import eaglemixins.config.ForgeConfigHandler;
import eaglemixins.config.folders.WeaponDamageConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Per-weapon critical-hit and range damage multipliers, configured under "Weapon Damage Modifiers".
 * Hooks RLCombat's melee pipeline ({@link Helpers#attackTargetEntityItem}). No-ops while both
 * config lists are empty.
 */
@Mixin(Helpers.class)
public class HelpersMixin {

    /**
     * Crit multiplier, "before" mode: fold the multiplier into the vanilla x1.5 crit step so
     * damage = base * multiplier * 1.5 (enchant bonus added afterwards, unscaled).
     * This expression is only evaluated on the crit path, so no extra crit guard is needed.
     */
    @ModifyExpressionValue(
            method = "attackTargetEntityItem",
            at = @At(value = "INVOKE", target = "Lbettercombat/mod/event/RLCombatCriticalHitEvent;getDamageModifier()F"),
            remap = false
    )
    private static float eagleMixins_weaponDamage_critBefore(float original, @Local(name = "weapon") ItemStack weapon) {
        WeaponDamageConfig.CritEntry entry = ForgeConfigHandler.weapondamage.getCritEntry(weapon);
        if (entry != null && !entry.afterCrit) {
            return original * entry.multiplier;
        }
        return original;
    }

    /**
     * Crit multiplier "after" mode plus the range multiplier, both applied to the final damage
     * value that is passed to {@code targetEntity.attackEntityFrom(source, damage)}.
     * "after": damage = (base * 1.5 + enchant bonus) * multiplier, only on an actual crit.
     * range: damage *= (multiplier - base) * amount + base, on every hit.
     */
    @ModifyArg(
            method = "attackTargetEntityItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z"),
            index = 1
    )
    private static float eagleMixins_weaponDamage_afterAndRange(
            float damage,
            @Local(name = "weapon") ItemStack weapon,
            @Local(name = "isCrit") boolean isCrit,
            @Local(argsOnly = true, name = "player") EntityPlayer player,
            @Local(argsOnly = true, name = "targetEntity") Entity targetEntity) {

        float result = damage;

        WeaponDamageConfig.CritEntry crit = ForgeConfigHandler.weapondamage.getCritEntry(weapon);
        if (crit != null && crit.afterCrit && isCrit) {
            result *= crit.multiplier;
        }

        WeaponDamageConfig.RangeEntry range = ForgeConfigHandler.weapondamage.getRangeEntry(weapon);
        if (range != null) {
            result *= range.compute(player.getDistance(targetEntity));
        }

        return result;
    }
}