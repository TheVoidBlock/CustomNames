package com.thevoidblock.customnames.mixin;

import com.thevoidblock.customnames.CustomNames;
import com.thevoidblock.customnames.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    @ModifyArgs(
            method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", ordinal = 1)
    )
    protected void renderLabelIfPresent(Args args) {

        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        if(config.enabled) {
            Text name = args.get(1);
            args.set(1, CustomNames.getAppliedName(config, (MutableText) name));
        }

    }

}
