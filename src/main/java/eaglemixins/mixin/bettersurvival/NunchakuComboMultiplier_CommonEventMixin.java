package eaglemixins.mixin.bettersurvival;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mujmajnkraft.bettersurvival.eventhandlers.CommonEventHandler;
import eaglemixins.config.ForgeConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Replaces Better Survival's nunchaku combo damage multiplier with the flat
 * "Nunchaku Combo Damage Multiplier" config value on the non-RLCombat code path.
 *
 * <p>Better Survival's {@code onDamage} does {@code event.setAmount(amount * (comboPower + 1.0F))}.
 * Returning {@code multiplier - 1} from the {@code getComboPower()} call makes that
 * {@code amount * multiplier}. This branch is gated by {@code !BetterSurvival.isRLCombatLoaded},
 * so it is inert while RLCombat is installed but keeps the override correct without it.
 */
@Mixin(CommonEventHandler.class)
public class NunchakuComboMultiplier_CommonEventMixin {

    @ModifyExpressionValue(
            method = "onDamage",
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