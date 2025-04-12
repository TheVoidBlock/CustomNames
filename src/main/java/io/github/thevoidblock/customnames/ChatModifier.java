package io.github.thevoidblock.customnames;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

import static io.github.thevoidblock.customnames.CustomNames.*;

public class ChatModifier {
    public static void register() {

        ClientReceiveMessageEvents.ALLOW_GAME.register(
                (message, overlay) -> {
                    CustomNamesConfig config = AutoConfig.getConfigHolder(CustomNamesConfig.class).getConfig();
                    if(!config.enabled || !config.chatModification) return true;

                    return attemptRawTranslatableChatModification(message, config);
                });

        ClientReceiveMessageEvents.ALLOW_CHAT.register(
                (message, signedMessage, sender, params, receptionTimestamp) -> {
                    CustomNamesConfig config = AutoConfig.getConfigHolder(CustomNamesConfig.class).getConfig();
                    if(!config.enabled || !config.chatModification) return true;

                    if(sender == null) return attemptRawTranslatableChatModification(message, config);

                    Text name = Text.of(sender.getName());

                    if(checkNameModification(config, name))
                        return modifyMessage(
                                (messageText, messageContent) -> {
                                    assert CLIENT.player != null;
                                    addModifiedMessage(getAppliedName(config, name), messageText);
                                    return true;
                                },
                                message
                        );
                    return true;
                });
    }


    @FunctionalInterface
    private interface MessageModifier {
        boolean modify(Text messageText, TranslatableTextContent messageContent);
    }

    private static boolean modifyMessage(MessageModifier modifier, Text message) {
        if(
                message.getContent() instanceof TranslatableTextContent messageContent
                        && messageContent.getKey().equals("chat.type.text")
                        && messageContent.getArgs().length == 2
        ) {
            Text messageText;
            Object messageTextObject = messageContent.getArgs()[1];

            if(messageTextObject instanceof Text) messageText = (Text)messageTextObject;
            else if(messageTextObject instanceof String) messageText = Text.of((String)messageTextObject);
            else return true;

            return !modifier.modify(messageText, messageContent);
        }
        return true;
    }

    private static void addModifiedMessage(final Text player, Text message) {
        ((MutableText)player).append("*");
        CLIENT.inGameHud.getChatHud().addMessage(Text.translatable("chat.type.text", player, ((MutableText)message).append(" ")));
    }

    private static boolean attemptRawTranslatableChatModification(Text message, CustomNamesConfig config) {
        return modifyMessage(
                (messageText, messageContent) -> {
                    Text name = Text.of(messageContent.getArg(0).getString());

                    if (checkNameModification(config, name)) {
                        assert CLIENT.player != null;
                        addModifiedMessage(getAppliedName(config, name), messageText);
                        return true;
                    }
                    return false;
                },
                message
        );
    }
}
