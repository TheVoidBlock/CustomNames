package com.thevoidblock.customnames;

import com.terraformersmc.modmenu.util.mod.Mod;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomNames implements ModInitializer {

    public static final String MOD_ID = "customnames";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);

    }

    private static Text getAppliedPlayerName(ModConfig.PlayerEntry config, MutableText name) {

        if(config.nameOverwriteEnabled) name = Text.literal(config.nameOverwrite).setStyle(name.getStyle());
        if(config.nameColorEnabled) name = name.withColor(config.nameColor);
        if(config.prefixEnabled) {
            MutableText prefix = Text.literal(config.prefix);
            if(config.prefixColorEnabled) prefix = prefix.withColor(config.prefixColor);
            name = prefix.append(name);
        }
        if(config.suffixEnabled) {
            MutableText suffix = Text.literal(config.suffix);
            if(config.suffixColorEnabled) suffix = suffix.withColor(config.suffixColor);
            name = name.append(suffix);
        }

        return name;
    }

    private static Text getAppliedGlobalName(ModConfig.Entry config, MutableText name) {

        if(config.nameOverwriteEnabled) name = Text.literal(config.nameOverwrite).setStyle(name.getStyle());
        if(config.nameColorEnabled) name = name.withColor(config.nameColor);
        if(config.prefixEnabled) {
            MutableText prefix = Text.literal(config.prefix);
            if(config.prefixColorEnabled) prefix = prefix.withColor(config.prefixColor);
            name = prefix.append(name);
        }
        if(config.suffixEnabled) {
            MutableText suffix = Text.literal(config.suffix);
            if(config.suffixColorEnabled) suffix = suffix.withColor(config.suffixColor);
            name = name.append(suffix);
        }

        return name;
    }

    public static Text getAppliedName(ModConfig config, MutableText name) {

        var nameWrapper = new Object(){Text wrappedName = name;};

        config.playerEntries
                .stream()
                .filter(playerEntry -> playerEntry.enabled && nameWrapper.wrappedName.getString().equals(playerEntry.playerName))
                .findAny()
                .ifPresentOrElse(
                        playerEntry -> nameWrapper.wrappedName = CustomNames.getAppliedPlayerName(playerEntry, (MutableText) nameWrapper.wrappedName),
                        () -> nameWrapper.wrappedName = CustomNames.getAppliedGlobalName(config.globalConfig, (MutableText) nameWrapper.wrappedName)
                );

        return nameWrapper.wrappedName;
    }
}
