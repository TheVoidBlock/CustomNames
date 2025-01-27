package com.thevoidblock.customnames.mixin;

import com.thevoidblock.customnames.CustomNames;
import com.thevoidblock.customnames.CustomNamesConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PlayerEntity.class)
public class PlayerEntityRendererMixin {

    @ModifyArgs(
            method = "Lnet/minecraft/entity/player/PlayerEntity;getDisplayName()Lnet/minecraft/text/Text;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/scoreboard/Team;decorateName(Lnet/minecraft/scoreboard/AbstractTeam;Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;")
    )
    protected void renderLabelIfPresent(Args args) {

        CustomNamesConfig config = AutoConfig.getConfigHolder(CustomNamesConfig.class).getConfig();

        if(config.enabled) {
            Text name = args.get(1);
            if(CustomNames.checkNameModification(config, name))
                args.set(1, CustomNames.getAppliedName(config, name));
        }

    }

}
