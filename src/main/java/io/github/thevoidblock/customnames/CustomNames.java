package io.github.thevoidblock.customnames;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomNames implements ClientModInitializer {

    public static final String MOD_ID = "customnames";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    @Override
    public void onInitializeClient() {

        AutoConfig.register(CustomNamesConfig.class, JanksonConfigSerializer::new);
        ChatModifier.register();

        LOGGER.info("{} initialized!", MOD_ID);
    }

    private static Text getAppliedPlayerName(CustomNamesConfig.PlayerEntry playerEntry, MutableText name) {
        return getAppliedName(playerEntry.toEntry(), name);
    }

    private static Text getAppliedName(CustomNamesConfig.Entry entry, MutableText name) {

        if(entry.nameOverwriteEnabled) name = Text.literal(entry.nameOverwrite).setStyle(name.getStyle());
        if(entry.nameColorEnabled) name = name.withColor(entry.nameColor);
        if(entry.prefixEnabled) {
            MutableText prefix = Text.literal(entry.prefix);
            if(entry.prefixColorEnabled) prefix = prefix.withColor(entry.prefixColor);
            name = prefix.append(name);
        }
        if(entry.suffixEnabled) {
            MutableText suffix = Text.literal(entry.suffix);
            if(entry.suffixColorEnabled) suffix = suffix.withColor(entry.suffixColor);
            name = name.append(suffix);
        }

        return name;
    }

    // checks whether a name will need to be modified
    public static boolean checkNameModification(CustomNamesConfig config, final Text name) {
        var modificationWrapper = new Object() {boolean modification;};
        config.playerEntries
                .stream()
                .filter(playerEntry -> playerEntry.enabled && name.getString().equals(playerEntry.playerName))
                .findAny()
                .ifPresentOrElse(
                        playerEntry -> modificationWrapper.modification = true,
                        () -> modificationWrapper.modification = config.globalConfig.nameOverwriteEnabled || config.globalConfig.suffixEnabled || config.globalConfig.prefixEnabled
                );
        return modificationWrapper.modification;
    }

    public static Text getAppliedName(CustomNamesConfig config, Text name) {

        var nameWrapper = new Object(){Text wrappedName = name;};

        config.playerEntries
                .stream()
                .filter(playerEntry -> playerEntry.enabled && nameWrapper.wrappedName.getString().equals(playerEntry.playerName))
                .findAny()
                .ifPresentOrElse(
                        playerEntry -> nameWrapper.wrappedName = CustomNames.getAppliedPlayerName(playerEntry, (MutableText) nameWrapper.wrappedName),
                        () -> nameWrapper.wrappedName = CustomNames.getAppliedName(config.globalConfig, (MutableText) nameWrapper.wrappedName)
                );

        return nameWrapper.wrappedName;
    }
}
