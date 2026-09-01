package eaglemixins.mixin.bettersurvival;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mujmajnkraft.bettersurvival.integration.RLCombatCompatEventHandler;
import eaglemixins.config.ForgeConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Replaces Better Survival's nunchaku combo damage multiplier with the flat
 * "Nunchaku Combo Damage Multiplier" config value on the RLCombat code path.
 *
 * <p>Better Survival's {@code onDamageModifyPost} does
 * {@code damageModifier += baseDamage * combo.getComboPower()}, which RLCombat turns into
 * {@code baseDamage * (1 + comboPower)}. Returning {@code multiplier - 1} from the
 * {@code getComboPower()} call makes the final contribution {@code baseDamage * multiplier}.
 */
@Mixin(RLCombatCompatEventHandler.class)
public class NunchakuComboMultiplier_RLCombatMixin {

    @ModifyExpressionValue(
            method = "onDamageModifyPost",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mujmajnkraft/bettersurvival/capabilities/nunchakucombo/INunchakuCombo;getComboPower()F"
            ),
            remap = false
    )
    private float eagleMixins_nunchakuComboMultiplier(float original) {
        return ForgeConfigHandler.weapondamage.nunchakuComboMultiplier - 1.0F;
    }
}