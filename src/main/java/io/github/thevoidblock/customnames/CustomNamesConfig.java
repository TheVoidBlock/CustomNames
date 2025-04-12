package io.github.thevoidblock.customnames;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

import static io.github.thevoidblock.customnames.CustomNames.MOD_ID;

@Config(name = MOD_ID)
public class CustomNamesConfig implements ConfigData {

    public boolean enabled = true;
    public boolean chatModification = true;

    @ConfigEntry.Gui.TransitiveObject
    public Entry globalConfig = new Entry();

    public List<PlayerEntry> playerEntries = new ArrayList<>();


    public static class Entry implements ConfigData {

        public boolean nameOverwriteEnabled = false;
        public String nameOverwrite = "";

        public boolean nameColorEnabled = false;
        @ConfigEntry.ColorPicker
        public int nameColor = 16777215;


        public boolean prefixEnabled = false;
        public String prefix = "";

        public boolean prefixColorEnabled = false;
        @ConfigEntry.ColorPicker
        public int prefixColor = 16777215;


        public boolean suffixEnabled = false;
        public String suffix = "";

        public boolean suffixColorEnabled = false;
        @ConfigEntry.ColorPicker
        public int suffixColor = 16777215;

    }

    public static class PlayerEntry /*autoconfig doesn't register inherited fields :( If anyone knows how to fix this please let me know!*/ {

        public boolean enabled = true;
        public String playerName = "";


        public boolean nameOverwriteEnabled = false;
        public String nameOverwrite = "";

        public boolean nameColorEnabled = false;
        @ConfigEntry.ColorPicker
        public int nameColor = 16777215;


        public boolean prefixEnabled = false;
        public String prefix = "";

        public boolean prefixColorEnabled = false;
        @ConfigEntry.ColorPicker
        public int prefixColor = 16777215;


        public boolean suffixEnabled = false;
        public String suffix = "";

        public boolean suffixColorEnabled = false;
        @ConfigEntry.ColorPicker
        public int suffixColor = 16777215;

        public Entry toEntry() {
            Entry entry = new Entry();
            entry.nameOverwriteEnabled = this.nameOverwriteEnabled;
            entry.nameOverwrite = this.nameOverwrite;
            entry.nameColorEnabled = this.nameColorEnabled;
            entry.nameColor = this.nameColor;
            entry.prefixEnabled = this.prefixEnabled;
            entry.prefix = this.prefix;
            entry.prefixColorEnabled = this.prefixColorEnabled;
            entry.prefixColor = this.prefixColor;
            entry.suffixEnabled = this.suffixEnabled;
            entry.suffix = this.suffix;
            entry.suffixColorEnabled = this.suffixColorEnabled;
            entry.suffixColor = this.suffixColor;
            return entry;
        }
    }

}
