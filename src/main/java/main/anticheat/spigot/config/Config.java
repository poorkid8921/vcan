package main.anticheat.spigot.config;

import main.anticheat.api.check.ICheckData;
import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.config.type.CommentedConfiguration;
import main.anticheat.spigot.util.CacheUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public final class Config {
    public static final Map<String, ICheckData> CHECK_DATA;
    public static final Map<String, Boolean> ENABLED_CHECKS;
    public static final Map<String, Integer> MAX_VIOLATIONS;
    public static final Map<String, Integer> ALERT_INTERVAL;
    public static final Map<String, Integer> MINIMUM_VL_TO_NOTIFY;
    public static final Map<String, List<String>> PUNISHMENT_COMMANDS;
    public static final Map<String, Boolean> PUNISHABLE;
    public static final Map<String, Boolean> BROADCAST_PUNISHMENT;
    public static final Map<String, Integer> MAXIMUM_PING;
    public static final Map<String, Double> MINIMUM_TPS;
    public static final Map<String, Boolean> SETBACKS;
    public static final Map<String, Integer> MAX_BUFFERS;
    public static final Map<String, Double> BUFFER_DECAYS;
    public static final Map<String, Double> BUFFER_MULTIPLES;
    public static final Map<String, Boolean> HOTBAR_SHUFFLE;
    public static final Map<String, Integer> HOTBAR_SHUFFLE_MINIMUM;
    public static final Map<String, Integer> HOTBAR_SHUFFLE_EVERY;
    public static final Map<String, Boolean> RANDOM_ROTATION;
    public static final Map<String, Integer> RANDOM_ROTATION_MINIMUM;
    public static final Map<String, Integer> RANDOM_ROTATION_EVERY;
    public static final Map<String, Integer> DISCORD_ALERT_INTERVAL;
    public static final Map<String, Integer> MINIMUM_VIOLATIONS_TO_SETBACK;
    public static String PREFIX;
    public static String NO_PERMISSION;
    public static String ALERTS_FORMAT;
    public static String ALERTS_ENABLED;
    public static String ALERTS_DISABLED;
    public static String CANT_EXECUTE_FROM_CONSOLE;
    public static String PUNISHMENT_MESSAGE;
    public static String BAN_COMMAND_SYNTAX;
    public static String RELOAD_SUCCESS;
    public static String KB_COMMAND_SYNTAX;
    public static String INVALID_TARGET;
    public static String KB_TEST_SUCCESS;
    public static String JDAY_COMMAND_SYNTAX;
    public static String JDAY_NO_PENDING_BANS;
    public static String JDAY_ADDED_TO_LIST;
    public static String RESET_COMMAND;
    public static String PROFILE_COMMAND_SYNTAX;
    public static String VERBOSE_ENABLED;
    public static String VERBOSE_DISABLED;
    public static String VERBOSE_FORMAT;
    public static String VIOLATION_RESET_MESSAGE;
    public static String ALERTS_CONSOLE_FORMAT;
    public static String LOG_FILE_ALERT_MESSAGE;
    public static String LOG_FILE_PUNISHMENT_MESSAGE;
    public static String ALERTS_SEVERITY_COLOR_1;
    public static String ALERTS_SEVERITY_COLOR_2;
    public static String ALERTS_SEVERITY_COLOR_3;
    public static String ALERTS_SEVERITY_COLOR_4;
    public static String ALERTS_SEVERITY_COLOR_5;
    public static String CPS_COMMAND;
    public static String NO_NEXT_PAGE;
    public static String NO_PREVIOUS_PAGE;
    public static String ENABLED_ALL_CHECKS;
    public static String DISABLED_ALL_CHECKS;
    public static String ENABLED_ALL_PUNISHMENTS;
    public static String DISABLED_ALL_PUNISHMENTS;
    public static String ENABLED_CHECK;
    public static String DISABLED_CHECK;
    public static String ENABLED_PUNISHMENT;
    public static String DISABLED_PUNISHMENT;
    public static String ENABLED_HOTBAR_SHUFFLE;
    public static String DISABLED_HOTBAR_SHUFFLE;
    public static String ENABLED_RANDOM_ROTATION;
    public static String DISABLED_RANDOM_ROTATION;
    public static String ENABLED_BROADCAST_PUNISHMENT;
    public static String DISABLED_BROADCAST_PUNISHMENT;
    public static String MUST_BE_AN_INTEGER;
    public static String MUST_BE_POSITIVE;
    public static String SET_MAX_BUFFER;
    public static String SET_HOTBAR_SHUFFLE_MINIMUM_VIOLATIONS;
    public static String SET_HOTBAR_SHUFFLE_INTERVAL;
    public static String SET_RANDOM_ROTATION_MINIMUM_VIOLATIONS;
    public static String SET_RANDOM_ROTATION_INTERVAL;
    public static String SET_ALERT_INTERVAL;
    public static String MUST_BE_NUMBER;
    public static String SET_BUFFER_MULTIPLE;
    public static String SET_BUFFER_DECAY;
    public static String SET_MAX_VIOLATIONS;
    public static String SET_MINIMUM_VIOLATIONS_TO_ALERT;
    public static String SET_MAXIMUM_PING;
    public static String SET_MINIMUM_TPS;
    public static String REMOVED_PUNISHMENT_COMMAND;
    public static String INVALID_INDEX_NUMBER;
    public static String STOPPED_EDITING_PUNISHMENT_COMMANDS;
    public static String ADDED_PUNISHMENT_COMMAND;
    public static String ENTER_PUNISHMENT_COMMAND;
    public static String REMOVE_PUNISHMENT_COMMAND;
    public static String DISABLE_CHECK_COMMAND_SYNTAX;
    public static String INVALID_CHECK;
    public static String CHECK_REMOVED;
    public static String VIOLATIONS_COMMAND_SYNTAX;
    public static String NO_LOGS;
    public static String CPS_COMMAND_SYNTAX;
    public static String CONNECTION_COMMAND_SYNTAX;
    public static String LOGS_COMMAND_SYNTAX;
    public static String NO_LOGS_FILE;
    public static String LOGS_COMMAND_NO_LOGS;
    public static String NO_PAGE;
    public static String LOGS_COMMAND_COLOR;
    public static String LOGS_COMMAND_HEADER_FOOTER;
    public static String UPDATE_AVAILABLE;
    public static String LATEST_VERSION;
    public static String SERVER_NAME;
    public static String FROZEN;
    public static String LOGGED_OUT_WHILE_FROZEN;
    public static String FROZE;
    public static String UNFROZE;
    public static String FREEZE_COMMAND_SYNTAX;
    public static String FLYING_NOT_ENABLED;
    public static String TOO_MANY_PACKETS;
    public static String INVALID_MOVE_PACKET;
    public static String SHUFFLE_COMMAND_SYNTAX;
    public static String SHUFFLED_HOTBAR;
    public static String RANDOMLY_ROTATED;
    public static String ROTATE_COMMAND_SYNTAX;
    public static String GHOST_BLOCK_MESSAGE;
    public static String GUI_TITLE;
    public static String GHOST_BLOCK_STAFF_MESSAGE;
    public static String TOP_COMMAND_UNRESOLVED;
    public static String EXPERIMENTAL_SYMBOL;
    public static String RESET_COMMAND_SYNTAX;
    public static String VIOLATIONS_RESET;
    public static String UNKNOWN_COMMAND;
    public static String ALERTS_SEVERITY_COLOR_6;
    public static String ALERTS_SEVERITY_COLOR_7;
    public static String ALERTS_SEVERITY_COLOR_8;
    public static String ALERTS_SEVERITY_COLOR_9;
    public static String ALERTS_SEVERITY_COLOR_10;
    public static String STAFF_FROZE_BROADCAST;
    public static String STAFF_UNFROZE_BROADCAST;
    public static String GHOST_BLOCK_CONSOLE_MESSAGE;
    public static String JDAY_LIST_HEADER_FOOTER;
    public static String JDAY_LIST_FORMAT;
    public static String REMOVED_FROM_JDAY;
    public static String JDAY_REMOVE_COMMAND_SYNTAX;
    public static String PUNISH_LOGS_HEADER_FOOTER;
    public static String PUNISH_LOGS_SYNTAX;
    public static String TRANSACTION_KICK_MESSAGE;
    public static String KEEPALIVE_KICK_MESSAGE;
    public static String TRANSACTION_STAFF_ALERT_MESSAGE;
    public static String KEEPALIVE_STAFF_ALERT_MESSAGE;
    public static String TRANSACTION_KICK_CONSOLE_MESSAGE;
    public static String KEEPALIVE_KICK_CONSOLE_MESSAGE;
    public static String MAX_PING_KICK_MESSAGE;
    public static String MAX_PING_KICK_STAFF_ALERT_MESSAGE;
    public static String MAX_PING_KICK_CONSOLE_MESSAGE;
    public static String SENT_TEST_ALERT;
    public static String UNLOADED_CHUNK_SETBACK_MESSAGE;
    public static String DESYNC_KICK_MESSAGE;
    public static String DESYNC_STAFF_KICK_MESSAGE;
    public static String DESYNC_CONSOLE_MESSAGE;
    public static long JOIN_CHECK_WAIT_TIME;
    public static long TIMER_D_MAX_BALANCE;
    public static long JDAY_COOLDOWN;
    public static long TRANSACTION_MAX_DELAY;
    public static long KEEPALIVE_MAX_DELAY;
    public static boolean JOIN_MESSAGE_ENABLED;
    public static boolean TOGGLE_ALERTS_ON_JOIN;
    public static boolean VIOLATION_RESET_MESSAGE_ENABLED;
    public static boolean VIOLATION_RESET;
    public static boolean ALERTS_TO_CONSOLE;
    public static boolean LOG_FILE_ENABLED;
    public static boolean PUNISHMENT_FILE_ENABLED;
    public static boolean PER_CHECK_SEVERITY;
    public static boolean ENTITY_COLLISION_FIX;
    public static boolean CINEMATIC;
    public static boolean DEBUG;
    public static boolean CHECK_FOR_UPDATES;
    public static boolean PAPERSPIGOT;
    public static boolean GHOST_BLOCK_FIX;
    public static boolean GHOST_BLOCK_STAFF_MESSAGE_ENABLED;
    public static boolean ASYNC_ALERTS;
    public static boolean ENABLE_API;
    public static boolean GHOST_BLOCK_SETBACK;
    public static boolean UPDATE_CHECKER;
    public static boolean KICK_ALERT_MESSAGES;
    public static boolean GHOST_BLOCK_MESSAGE_ENABLED;
    public static boolean PUNISHMENT_STATISTIC_BROADCAST;
    public static boolean GHOSTHAND_A_CANCEL;
    public static boolean IGNORE_VIVECRAFT;
    public static boolean BOAT_FLY_A_KICKOUT;
    public static boolean BOAT_FLY_B_KICKOUT;
    public static boolean ENTITY_FLIGHT_A_KICKOUT;
    public static boolean ENTITY_SPEED_A_KICKOUT;
    public static boolean NO_SADDLE_A_KICKOUT;
    public static boolean LENIENT_SCAFFOLDING;
    public static boolean BAD_PACKETS_V_KICKOUT;
    public static boolean SERVER_LAG_SPIKE;
    public static boolean FLIGHT_A_IGNORE_GHOST_BLOCKS;
    public static boolean FLIGHT_C_IGNORE_GHOST_BLOCKS;
    public static boolean FLIGHT_D_IGNORE_GHOST_BLOCKS;
    public static boolean GHOST_BLOCK_FIX_UPDATE_BLOCKS;
    public static boolean IGNORED_1_17;
    public static boolean FLIGHT_E_IGNORE_GHOST_BLOCKS;
    public static boolean GHOST_BLOCK_PRINT_TO_CONSOLE;
    public static boolean TRANSACTION_KICK_ENABLED;
    public static boolean KEEPALIVE_KICK_ENABLED;
    public static boolean GHOST_WATER_FIX;
    public static boolean MAX_PING_KICK_ENABLED;
    public static boolean AIRPLACE_A_CANCEL;
    public static boolean IGNORE_ALERTS_OVER_MAX_VIOLATIONS;
    public static boolean PLUGIN_MESSAGING;
    public static boolean SETBACK_LOWER_POSITION;
    public static boolean VELOCITY_WORLD_BORDER;
    public static boolean UNLOADED_CHUNK_SETBACK;
    public static boolean DESYNC_KICK_ENABLED;
    public static boolean ENTITY_CRAM_FIX_ENABLED;
    public static List<String> ALERTS_CLICK_COMMANDS;
    public static List<String> ALERTS_HOVER_MESSAGES;
    public static List<String> JDAY_COMMANDS;
    public static List<String> VERBOSE_CLICK_COMMANDS;
    public static List<String> JDAY_BROADCAST;
    public static List<String> JOIN_MESSAGE;
    public static List<String> PUNISHMENT_BROADCAST;
    public static List<String> HELP_COMMAND;
    public static List<String> BAN_COMMANDS;
    public static List<String> VERBOSE_HOVER_MESSAGES;
    public static List<String> BAN_COMMAND_BROADCAST;
    public static List<String> JDAY_STARTED_BROADCAST;
    public static List<String> PROFILE_COMMAND;
    public static List<String> ALERTS_CUSTOM_COMMANDS;
    public static List<String> CONNECTION_COMMAND;
    public static List<String> CLICKALERT_COMMAND_COMMANDS;
    public static List<String> TOP_COMMAND;
    public static List<String> PUNISHMENT_STATISTIC_BROADCAST_MESSAGE;
    public static List<String> LOGGED_OUT_FROZEN_COMMANDS;
    public static List<String> JDAY_END_BROADCAST;
    public static int AUTOCLICKER_A_MAX_CPS;
    public static int FASTPLACE_A_MAX_BLOCKS;
    public static int ALERTS_SEVERITY_2;
    public static int PUNISHMENT_STATISTIC_BROADCAST_INTERVAL;
    public static int ALERTS_SEVERITY_3;
    public static int ALERTS_SEVERITY_4;
    public static int ALERTS_SEVERITY_5;
    public static int PING_SPOOF_C_MAX_PING;
    public static int SCAFFOLD_K_MAX_BLOCKS;
    public static int MAX_VELOCITY_TICKS;
    public static int MAX_LOGS_FILE_SIZE;
    public static int IMPROBABLE_A_MAX_VIOLATIONS;
    public static int IMPROBABLE_B_MAX_VIOLATIONS;
    public static int IMPROBABLE_C_MAX_VIOLATIONS;
    public static int IMPROBABLE_D_MAX_VIOLATIONS;
    public static int PUNISHMENT_DELAY;
    public static int TIMER_C_MAX_PACKETS;
    public static int MAX_ALERT_VIOLATION;
    public static int FLIGHT_COOLDOWN;
    public static int FASTBREAK_A_MIN_DIFFERENCE;
    public static int MIN_TICKS_EXISTED;
    public static int DESYNC_MAX_TICKS;
    public static int GHOST_BLOCK_MIN_TICKS;
    public static int ALERTS_SEVERITY_6;
    public static int ALERTS_SEVERITY_7;
    public static int ALERTS_SEVERITY_8;
    public static int ALERTS_SEVERITY_9;
    public static int ALERTS_SEVERITY_10;
    public static int IMPROBABLE_E_MAX_VIOLATIONS;
    public static int IMPROBABLE_F_MAX_SCAFFOLD_VIOLATIONS;
    public static int MAX_UNLOADED_CHUNK_TICKS;
    public static int MAX_PING;
    public static int MAX_PING_KICK_TICKS;
    public static int IGNORE_ALERTS_OVER_MAX_VIOLATIONS_AMOUNT;
    public static int FASTPLACE_B_MAX_BLOCKS;
    public static int ENTITY_CRAM_FIX_AMOUNT;
    public static int ENTITY_CRAM_FIX_EXEMPT_TICKS;
    public static double BOAT_FLY_A_MAX_SPEED;
    public static double ELYTRA_A_MAX_SPEED;
    public static double REACH_A_MAX_REACH;
    public static double HITBOX_A_MAX_ANGLE;
    public static double HITBOX_B_MAX_ANGLE;
    public static double ENTITY_SPEED_A_MAX_SPEED;
    public static double TIMER_A_MAX_SPEED;
    public static double REACH_B_MAX_REACH;
    public static double INTERACT_A_MAX_DISTANCE;
    public static double INTERACT_B_MAX_DISTANCE;
    public static double TIMER_B_MIN_SPEED;
    public static double SPEED_B_MIN_DIFFERENCE;
    public static double SPEED_C_MIN_DIFFERENCE;
    public static double SPEED_D_MIN_DIFFERENCE;
    public static double ELYTRA_L_MIN_DIFFERENCE;
    public static double ELYTRA_M_MIN_DIFFERENCE;
    public static double GHOST_BLOCK_MAX_BUFFER;
    public static double GHOST_BLOCK_BUFFER_DECAY;
    public static double AUTOCLICKER_T_MIN_KURTOSIS;
    public static double GHOST_BLOCK_MIN_TPS;
    private static CommentedConfiguration config;
    private static File file;

    static {
        CHECK_DATA = new HashMap<String, ICheckData>();
        ENABLED_CHECKS = new HashMap<String, Boolean>();
        MAX_VIOLATIONS = new HashMap<String, Integer>();
        ALERT_INTERVAL = new HashMap<String, Integer>();
        MINIMUM_VL_TO_NOTIFY = new HashMap<String, Integer>();
        PUNISHMENT_COMMANDS = new HashMap<String, List<String>>();
        PUNISHABLE = new HashMap<String, Boolean>();
        BROADCAST_PUNISHMENT = new HashMap<String, Boolean>();
        MAXIMUM_PING = new HashMap<String, Integer>();
        MINIMUM_TPS = new HashMap<String, Double>();
        SETBACKS = new HashMap<String, Boolean>();
        MAX_BUFFERS = new HashMap<String, Integer>();
        BUFFER_DECAYS = new HashMap<String, Double>();
        BUFFER_MULTIPLES = new HashMap<String, Double>();
        HOTBAR_SHUFFLE = new HashMap<String, Boolean>();
        HOTBAR_SHUFFLE_MINIMUM = new HashMap<String, Integer>();
        HOTBAR_SHUFFLE_EVERY = new HashMap<String, Integer>();
        RANDOM_ROTATION = new HashMap<String, Boolean>();
        RANDOM_ROTATION_MINIMUM = new HashMap<String, Integer>();
        RANDOM_ROTATION_EVERY = new HashMap<String, Integer>();
        DISCORD_ALERT_INTERVAL = new HashMap<String, Integer>();
        MINIMUM_VIOLATIONS_TO_SETBACK = new HashMap<String, Integer>();
    }

    private Config() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void save() {
        try {
            Config.config.save(Config.file);
        } catch (final Exception exception) {
            final File newFile = new File(Anticheat.INSTANCE.getPlugin().getDataFolder(), "config.broken." + new Date().getTime());
            Anticheat.INSTANCE.getPlugin().getLogger().log(Level.SEVERE, "Could not save: config.yml");
            Anticheat.INSTANCE.getPlugin().getLogger().log(Level.SEVERE, "Regenerating file and renaming the current file to: " + newFile.getName());
            Anticheat.INSTANCE.getPlugin().getLogger().log(Level.SEVERE, "You can try fixing the file with a yaml parser online!");
            Config.file.renameTo(newFile);
            initialize();
        }
    }

    public static void reload() {
        try {
            Config.config.load(Config.file);
        } catch (final Exception exception) {
            final File newFile = new File(Anticheat.INSTANCE.getPlugin().getDataFolder(), "config.broken." + new Date().getTime());
            Anticheat.INSTANCE.getPlugin().getLogger().log(Level.SEVERE, "Could not save: config.yml");
            Anticheat.INSTANCE.getPlugin().getLogger().log(Level.SEVERE, "Regenerating file and renaming the current file to: " + newFile.getName());
            Anticheat.INSTANCE.getPlugin().getLogger().log(Level.SEVERE, "You can try fixing the file with a yaml parser online!");
            Config.file.renameTo(newFile);
            initialize();
        }
    }

    public static void initialize() {
        if (!Anticheat.INSTANCE.getPlugin().getDataFolder().exists()) {
            Anticheat.INSTANCE.getPlugin().getDataFolder().mkdir();
        }
        Config.file = new File(Anticheat.INSTANCE.getPlugin().getDataFolder(), "config.yml");
        if (!Config.file.exists()) {
            Config.file.getParentFile().mkdirs();
            copy(Anticheat.INSTANCE.getPlugin().getResource("config.yml"), Config.file);
        }
        Config.config = CommentedConfiguration.loadConfiguration(Config.file);
        try {
            Config.config.syncWithConfig(Config.file, Anticheat.INSTANCE.getPlugin().getResource("config.yml"), "NONE");
        } catch (final Exception ex) {
        }
    }

    private static void copy(final InputStream in, final File file) {
        try {
            final OutputStream out = new FileOutputStream(file);
            final byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void updateConfig() {
        try {
            Config.PREFIX = getString("prefix");
            Config.ALERTS_FORMAT = getString("alerts.format");
            Config.NO_PERMISSION = getString("messages.no-permission");
            Config.UNLOADED_CHUNK_SETBACK = getBoolean("unloaded-chunks.setback-enabled");
            Config.UNLOADED_CHUNK_SETBACK_MESSAGE = getString("unloaded-chunks.message");
            Config.MAX_UNLOADED_CHUNK_TICKS = getInt("unloaded-chunks.max-ticks");
            Config.DESYNC_KICK_ENABLED = getBoolean("connection.desync.kick-enabled");
            Config.DESYNC_MAX_TICKS = getInt("connection.desync.max-ticks");
            Config.DESYNC_KICK_MESSAGE = getString("connection.desync.kick-message");
            Config.DESYNC_STAFF_KICK_MESSAGE = getString("connection.desync.staff-alert-message");
            Config.DESYNC_CONSOLE_MESSAGE = getString("connection.desync.console-message");
            Config.ALERTS_ENABLED = getString("alerts.toggled-on-message");
            Config.ALERTS_DISABLED = getString("alerts.toggled-off-message");
            Config.ALERTS_HOVER_MESSAGES = getStringList("alerts.hover-message");
            Config.ALERTS_CLICK_COMMANDS = getStringList("alerts.click-commands");
            Config.CANT_EXECUTE_FROM_CONSOLE = getString("messages.cant-execute-from-console");
            Config.JDAY_COMMANDS = getStringList("judgement-days.commands");
            Config.JDAY_BROADCAST = getStringList("judgement-days.broadcast");
            Config.JDAY_END_BROADCAST = getStringList("judgement-days.ended-broadcast");
            Config.JOIN_MESSAGE_ENABLED = getBoolean("join-message.enabled");
            Config.JOIN_MESSAGE = getStringList("join-message.message");
            Config.TOGGLE_ALERTS_ON_JOIN = getBoolean("settings.toggle-alerts-on-join");
            Config.PUNISHMENT_MESSAGE = getString("punishments.message");
            Config.PUNISHMENT_BROADCAST = getStringList("punishments.broadcast");
            Config.HELP_COMMAND = getStringList("commands.help");
            Config.JDAY_LIST_HEADER_FOOTER = getString("commands.jday.list-header-footer");
            Config.PUNISH_LOGS_HEADER_FOOTER = getString("commands.punishlogs.header-footer");
            Config.JDAY_LIST_FORMAT = getString("commands.jday.list-format");
            Config.GHOST_WATER_FIX = getBoolean("ghost-blocks-fix.ghost-water-fix");
            Config.SETBACK_LOWER_POSITION = getBoolean("settings.dont-setback-lower-positions");
            Config.TRANSACTION_KICK_MESSAGE = getString("connection.transaction.kick-message");
            Config.KEEPALIVE_KICK_MESSAGE = getString("connection.keepalive.kick-message");
            Config.MAX_PING_KICK_MESSAGE = getString("connection.max-ping.kick-message");
            Config.TRANSACTION_KICK_ENABLED = getBoolean("connection.transaction.kick-enabled");
            Config.KEEPALIVE_KICK_ENABLED = getBoolean("connection.keepalive.kick-enabled");
            Config.MAX_PING_KICK_ENABLED = getBoolean("connection.max-ping.kick-enabled");
            Config.MAX_PING = getInt("connection.max-ping.max-ping");
            Config.VELOCITY_WORLD_BORDER = getBoolean("settings.velocity-world-border");
            Config.PLUGIN_MESSAGING = getBoolean("settings.plugin-messaging");
            Config.MAX_PING_KICK_TICKS = getInt("connection.max-ping.max-ticks");
            Config.TRANSACTION_STAFF_ALERT_MESSAGE = getString("connection.transaction.staff-alert-message");
            Config.KEEPALIVE_STAFF_ALERT_MESSAGE = getString("connection.keepalive.staff-alert-message");
            Config.MAX_PING_KICK_STAFF_ALERT_MESSAGE = getString("connection.max-ping.staff-alert-message");
            Config.TRANSACTION_KICK_CONSOLE_MESSAGE = getString("connection.transaction.console-message");
            Config.MAX_PING_KICK_CONSOLE_MESSAGE = getString("connection.max-ping.console-message");
            Config.KEEPALIVE_KICK_CONSOLE_MESSAGE = getString("connection.keepalive.console-message");
            Config.TRANSACTION_MAX_DELAY = getLong("connection.transaction.max-delay");
            Config.KEEPALIVE_MAX_DELAY = getLong("connection.keepalive.max-delay");
            Config.IGNORE_ALERTS_OVER_MAX_VIOLATIONS = getBoolean("settings.ignore-alerts-over-max-violations");
            Config.IGNORE_ALERTS_OVER_MAX_VIOLATIONS_AMOUNT = getInt("settings.ignore-alerts-over-max-violations-amount");
            Config.PUNISH_LOGS_SYNTAX = getString("messages.punishlogs-syntax");
            Config.JDAY_REMOVE_COMMAND_SYNTAX = getString("messages.jday-remove-syntax");
            Config.BAN_COMMAND_SYNTAX = getString("messages.ban-command-syntax");
            Config.BAN_COMMANDS = getStringList("commands.ban.commands");
            Config.BAN_COMMAND_BROADCAST = getStringList("commands.ban.broadcast");
            Config.RELOAD_SUCCESS = getString("messages.reload-success");
            Config.KB_COMMAND_SYNTAX = getString("messages.kb-command-syntax");
            Config.INVALID_TARGET = getString("messages.invalid-target");
            Config.KB_TEST_SUCCESS = getString("messages.kb-test-success");
            Config.JDAY_COMMAND_SYNTAX = getString("messages.jday-command-syntax");
            Config.JDAY_NO_PENDING_BANS = getString("messages.jday-no-pending-bans");
            Config.JDAY_STARTED_BROADCAST = getStringList("judgement-days.started-broadcast");
            Config.JDAY_ADDED_TO_LIST = getString("messages.jday-added-to-list");
            Config.RESET_COMMAND = getString("messages.reset-command");
            Config.SERVER_LAG_SPIKE = getBoolean("settings.server-lag-spike");
            Config.PROFILE_COMMAND_SYNTAX = getString("messages.profile-command-syntax");
            Config.PROFILE_COMMAND = getStringList("commands.profile");
            Config.REMOVED_FROM_JDAY = getString("messages.removed-from-jday");
            Config.VERBOSE_ENABLED = getString("verbose.toggled-on-message");
            Config.VERBOSE_DISABLED = getString("verbose.toggled-off-message");
            Config.VERBOSE_HOVER_MESSAGES = getStringList("verbose.hover-message");
            Config.VERBOSE_CLICK_COMMANDS = getStringList("verbose.click-commands");
            Config.VERBOSE_FORMAT = getString("verbose.format");
            Config.VIOLATION_RESET = getBoolean("violation-reset.enabled");
            Config.VIOLATION_RESET_MESSAGE_ENABLED = getBoolean("violation-reset.message-enabled");
            Config.VIOLATION_RESET_MESSAGE = getString("violation-reset.message");
            Config.ALERTS_TO_CONSOLE = getBoolean("alerts.print-to-console");
            Config.ALERTS_CONSOLE_FORMAT = getString("alerts.console-format");
            Config.LOG_FILE_ENABLED = getBoolean("log-file.enabled");
            Config.LOG_FILE_ALERT_MESSAGE = getString("log-file.alert-message");
            Config.LOG_FILE_PUNISHMENT_MESSAGE = getString("log-file.punishment-message");
            Config.PUNISHMENT_FILE_ENABLED = getBoolean("punishment-file.enabled");
            Config.AUTOCLICKER_A_MAX_CPS = getInt("checks.combat.autoclicker.a.max-cps");
            Config.AUTOCLICKER_T_MIN_KURTOSIS = getInt("checks.combat.autoclicker.t.min-kurtosis");
            Config.CLICKALERT_COMMAND_COMMANDS = getStringList("alerts.click-alert-command-commands");
            Config.FASTPLACE_A_MAX_BLOCKS = getInt("checks.player.fastplace.a.max-blocks");
            Config.FASTPLACE_B_MAX_BLOCKS = getInt("checks.player.fastplace.b.max-blocks");
            Config.AIRPLACE_A_CANCEL = getBoolean("checks.player.airplace.a.cancel");
            Config.IGNORE_VIVECRAFT = getBoolean("settings.ignore-vivecraft");
            Config.BOAT_FLY_A_KICKOUT = getBoolean("checks.movement.boatfly.a.kick-out");
            Config.BOAT_FLY_B_KICKOUT = getBoolean("checks.movement.boatfly.b.kick-out");
            Config.ENTITY_SPEED_A_KICKOUT = getBoolean("checks.movement.entityspeed.a.kick-out");
            Config.ENTITY_FLIGHT_A_KICKOUT = getBoolean("checks.movement.entityflight.a.kick-out");
            Config.SPEED_B_MIN_DIFFERENCE = getDouble("checks.movement.speed.b.min-difference");
            Config.SPEED_C_MIN_DIFFERENCE = getDouble("checks.movement.speed.c.min-difference");
            Config.SPEED_D_MIN_DIFFERENCE = getDouble("checks.movement.speed.d.min-difference");
            Config.FLIGHT_A_IGNORE_GHOST_BLOCKS = getBoolean("checks.movement.flight.a.ignore-ghost-blocks");
            Config.FLIGHT_C_IGNORE_GHOST_BLOCKS = getBoolean("checks.movement.flight.c.ignore-ghost-blocks");
            Config.FLIGHT_D_IGNORE_GHOST_BLOCKS = getBoolean("checks.movement.flight.d.ignore-ghost-blocks");
            Config.FLIGHT_E_IGNORE_GHOST_BLOCKS = getBoolean("checks.movement.flight.e.ignore-ghost-blocks");
            Config.ELYTRA_L_MIN_DIFFERENCE = getDouble("checks.movement.elytra.l.min-difference");
            Config.ELYTRA_M_MIN_DIFFERENCE = getDouble("checks.movement.elytra.m.min-difference");
            Config.NO_SADDLE_A_KICKOUT = getBoolean("checks.movement.nosaddle.a.kick-out");
            Config.NO_SADDLE_A_KICKOUT = getBoolean("checks.player.badpackets.v.kick-out");
            Config.BOAT_FLY_A_MAX_SPEED = getDouble("checks.movement.boatfly.a.max-speed");
            Config.ELYTRA_A_MAX_SPEED = getDouble("checks.movement.elytra.a.max-speed");
            Config.REACH_A_MAX_REACH = getDouble("checks.combat.reach.a.max-reach");
            Config.REACH_B_MAX_REACH = getDouble("checks.combat.reach.b.max-reach");
            Config.SCAFFOLD_K_MAX_BLOCKS = getInt("checks.player.scaffold.k.max-blocks");
            Config.HITBOX_A_MAX_ANGLE = getDouble("checks.combat.hitbox.a.max-angle");
            Config.HITBOX_B_MAX_ANGLE = getDouble("checks.combat.hitbox.b.max-angle");
            Config.TIMER_B_MIN_SPEED = getDouble("checks.player.timer.b.min-speed");
            Config.INTERACT_A_MAX_DISTANCE = getDouble("checks.player.interact.a.max-distance");
            Config.INTERACT_B_MAX_DISTANCE = getDouble("checks.player.interact.b.max-distance");
            Config.ENTITY_SPEED_A_MAX_SPEED = getDouble("checks.movement.entityspeed.a.max-speed");
            Config.IMPROBABLE_A_MAX_VIOLATIONS = getInt("checks.player.improbable.a.max-combat-violations");
            Config.IMPROBABLE_B_MAX_VIOLATIONS = getInt("checks.player.improbable.b.max-movement-violations");
            Config.IMPROBABLE_C_MAX_VIOLATIONS = getInt("checks.player.improbable.c.max-player-violations");
            Config.IMPROBABLE_D_MAX_VIOLATIONS = getInt("checks.player.improbable.d.max-autoclicker-violations");
            Config.IMPROBABLE_E_MAX_VIOLATIONS = getInt("checks.player.improbable.e.max-total-violations");
            Config.IMPROBABLE_F_MAX_SCAFFOLD_VIOLATIONS = getInt("checks.player.improbable.f.max-scaffold-violations");
            Config.GHOSTHAND_A_CANCEL = getBoolean("checks.player.ghosthand.a.cancel");
            Config.FASTBREAK_A_MIN_DIFFERENCE = getInt("checks.player.fastbreak.a.min-difference");
            Config.ALERTS_CUSTOM_COMMANDS = getStringList("alerts.custom-commands");
            Config.TIMER_A_MAX_SPEED = getDouble("checks.player.timer.a.max-speed");
            Config.TIMER_C_MAX_PACKETS = getInt("checks.player.timer.c.max-packets");
            Config.TIMER_D_MAX_BALANCE = getLong("checks.player.timer.d.max-balance");
            Config.IGNORED_1_17 = getBoolean("settings.ignore-1-17");
            Config.ALERTS_SEVERITY_2 = getInt("alerts.severity.violations.2");
            Config.ALERTS_SEVERITY_3 = getInt("alerts.severity.violations.3");
            Config.ALERTS_SEVERITY_4 = getInt("alerts.severity.violations.4");
            Config.ALERTS_SEVERITY_5 = getInt("alerts.severity.violations.5");
            Config.ALERTS_SEVERITY_6 = getInt("alerts.severity.violations.6");
            Config.ALERTS_SEVERITY_7 = getInt("alerts.severity.violations.7");
            Config.ALERTS_SEVERITY_8 = getInt("alerts.severity.violations.8");
            Config.ALERTS_SEVERITY_9 = getInt("alerts.severity.violations.9");
            Config.ALERTS_SEVERITY_10 = getInt("alerts.severity.violations.10");
            Config.ALERTS_SEVERITY_COLOR_1 = getString("alerts.severity.colors.1");
            Config.ALERTS_SEVERITY_COLOR_2 = getString("alerts.severity.colors.2");
            Config.ALERTS_SEVERITY_COLOR_3 = getString("alerts.severity.colors.3");
            Config.ALERTS_SEVERITY_COLOR_4 = getString("alerts.severity.colors.4");
            Config.ALERTS_SEVERITY_COLOR_5 = getString("alerts.severity.colors.5");
            Config.ALERTS_SEVERITY_COLOR_6 = getString("alerts.severity.colors.6");
            Config.ALERTS_SEVERITY_COLOR_7 = getString("alerts.severity.colors.7");
            Config.ALERTS_SEVERITY_COLOR_8 = getString("alerts.severity.colors.8");
            Config.ALERTS_SEVERITY_COLOR_9 = getString("alerts.severity.colors.9");
            Config.ALERTS_SEVERITY_COLOR_10 = getString("alerts.severity.colors.10");
            Config.PER_CHECK_SEVERITY = getBoolean("settings.per-check-severities");
            Config.PING_SPOOF_C_MAX_PING = getInt("checks.player.pingspoof.c.max-ping");
            Config.ENTITY_COLLISION_FIX = getBoolean("settings.entity-collision-fix");
            Config.CINEMATIC = getBoolean("settings.cinematic");
            Config.CPS_COMMAND = getString("commands.cps");
            Config.DEBUG = getBoolean("settings.debug");
            Config.SENT_TEST_ALERT = getString("messages.sent-test-alert");
            Config.NO_NEXT_PAGE = getString("messages.no-next-page");
            Config.NO_PREVIOUS_PAGE = getString("messages.no-previous-page");
            Config.ENABLED_ALL_CHECKS = getString("messages.enabled-all-checks");
            Config.DISABLED_ALL_CHECKS = getString("messages.disabled-all-checks");
            Config.ENABLED_ALL_PUNISHMENTS = getString("messages.enabled-all-punishments");
            Config.DISABLED_ALL_PUNISHMENTS = getString("messages.disabled-all-punishments");
            Config.ENABLED_CHECK = getString("messages.enabled-check");
            Config.DISABLED_CHECK = getString("messages.disabled-check");
            Config.ENABLED_PUNISHMENT = getString("messages.enabled-punishment");
            Config.DISABLED_PUNISHMENT = getString("messages.disabled-punishment");
            Config.ENABLED_HOTBAR_SHUFFLE = getString("messages.enabled-hotbar-shuffle");
            Config.DISABLED_HOTBAR_SHUFFLE = getString("messages.disabled-hotbar-shuffle");
            Config.ENABLED_RANDOM_ROTATION = getString("messages.enabled-random-rotation");
            Config.DISABLED_RANDOM_ROTATION = getString("messages.disabled-random-rotation");
            Config.ENABLED_BROADCAST_PUNISHMENT = getString("messages.enabled-broadcast-punishment");
            Config.DISABLED_BROADCAST_PUNISHMENT = getString("messages.disabled-broadcast-punishment");
            Config.MUST_BE_AN_INTEGER = getString("messages.must-be-an-integer");
            Config.MUST_BE_POSITIVE = getString("messages.must-be-positive");
            Config.SET_MAX_BUFFER = getString("messages.set-max-buffer");
            Config.SET_HOTBAR_SHUFFLE_MINIMUM_VIOLATIONS = getString("messages.set-hotbar-shuffle-minimum-violations");
            Config.SET_HOTBAR_SHUFFLE_INTERVAL = getString("messages.set-hotbar-interval");
            Config.SET_RANDOM_ROTATION_MINIMUM_VIOLATIONS = getString("messages.set-random-rotation-minimum-violations");
            Config.SET_RANDOM_ROTATION_INTERVAL = getString("messages.set-random-rotation-interval");
            Config.SET_ALERT_INTERVAL = getString("messages.set-alert-interval");
            Config.MUST_BE_NUMBER = getString("messages.must-be-number");
            Config.SET_BUFFER_MULTIPLE = getString("messages.set-buffer-multiple");
            Config.SET_BUFFER_DECAY = getString("messages.set-buffer-decay");
            Config.SET_MAX_VIOLATIONS = getString("messages.set-max-violations");
            Config.SET_MINIMUM_VIOLATIONS_TO_ALERT = getString("messages.set-minimum-violations-to-alert");
            Config.SET_MAXIMUM_PING = getString("messages.set-maximum-ping");
            Config.SET_MINIMUM_TPS = getString("messages.set-minimum-tps");
            Config.REMOVED_PUNISHMENT_COMMAND = getString("messages.removed-punishment-command");
            Config.INVALID_INDEX_NUMBER = getString("messages.invalid-index-number");
            Config.STOPPED_EDITING_PUNISHMENT_COMMANDS = getString("messages.stopped-editing-punishment-commands");
            Config.ADDED_PUNISHMENT_COMMAND = getString("messages.added-punishment-command");
            Config.ENTER_PUNISHMENT_COMMAND = getString("messages.enter-punishment-command");
            Config.REMOVE_PUNISHMENT_COMMAND = getString("messages.remove-punishment-command");
            Config.DISABLE_CHECK_COMMAND_SYNTAX = getString("messages.disable-check-command-syntax");
            Config.INVALID_CHECK = getString("messages.invalid-check");
            Config.CHECK_REMOVED = getString("messages.check-removed");
            Config.VIOLATIONS_COMMAND_SYNTAX = getString("messages.violations-command-syntax");
            Config.NO_LOGS = getString("messages.no-logs");
            Config.CPS_COMMAND_SYNTAX = getString("messages.cps-command-syntax");
            Config.CONNECTION_COMMAND_SYNTAX = getString("messages.connection-command-syntax");
            Config.CONNECTION_COMMAND = getStringList("commands.connection");
            Config.LOGS_COMMAND_SYNTAX = getString("messages.logs-command-syntax");
            Config.NO_LOGS_FILE = getString("messages.no-logs-file");
            Config.LOGS_COMMAND_NO_LOGS = getString("messages.logs-command-no-logs");
            Config.NO_PAGE = getString("messages.no-page");
            Config.LOGS_COMMAND_COLOR = getString("commands.logs.color");
            Config.LOGS_COMMAND_HEADER_FOOTER = getString("commands.logs.header-footer");
            Config.CHECK_FOR_UPDATES = getBoolean("settings.check-for-updates");
            Config.UPDATE_AVAILABLE = getString("messages.update-available");
            Config.LATEST_VERSION = getString("messages.latest-version");
            Config.PAPERSPIGOT = getBoolean("settings.paperspigot");
            Config.MAX_VELOCITY_TICKS = getInt("settings.max-velocity-ticks");
            Config.MAX_LOGS_FILE_SIZE = getInt("settings.max-logs-file-size");
            Config.SERVER_NAME = getString("settings.server-name");
            Config.FROZEN = getString("messages.frozen");
            Config.LOGGED_OUT_WHILE_FROZEN = getString("messages.logged-out-while-frozen");
            Config.FROZE = getString("messages.froze");
            Config.UNFROZE = getString("messages.unfroze");
            Config.FREEZE_COMMAND_SYNTAX = getString("messages.freeze-command-syntax");
            Config.ASYNC_ALERTS = getBoolean("settings.async-alerts");
            Config.ENABLE_API = getBoolean("settings.enable-api");
            Config.UPDATE_CHECKER = getBoolean("settings.update-checker");
            Config.KICK_ALERT_MESSAGES = getBoolean("kick-alert-messages.enabled");
            Config.FLYING_NOT_ENABLED = getString("kick-alert-messages.messages.flying-not-enabled");
            Config.TOO_MANY_PACKETS = getString("kick-alert-messages.messages.too-many-packets");
            Config.INVALID_MOVE_PACKET = getString("kick-alert-messages.messages.invalid-move-packet");
            Config.PUNISHMENT_DELAY = getInt("settings.punishment-delay");
            Config.JOIN_CHECK_WAIT_TIME = getLong("settings.join-check-wait-time");
            Config.MIN_TICKS_EXISTED = getInt("settings.min-ticks-existed");
            Config.MAX_ALERT_VIOLATION = getInt("settings.max-alert-violation");
            Config.SHUFFLE_COMMAND_SYNTAX = getString("messages.shuffle-command-syntax");
            Config.SHUFFLED_HOTBAR = getString("messages.shuffled-hotbar");
            Config.ROTATE_COMMAND_SYNTAX = getString("messages.rotate-command-syntax");
            Config.RANDOMLY_ROTATED = getString("messages.randomly-rotated");
            Config.TOP_COMMAND = getStringList("commands.top");
            Config.GHOST_BLOCK_FIX = getBoolean("ghost-blocks-fix.enabled");
            Config.GHOST_BLOCK_MIN_TPS = getDouble("ghost-blocks-fix.minimum-tps");
            Config.GHOST_BLOCK_MAX_BUFFER = getDouble("ghost-blocks-fix.buffer.max");
            Config.GHOST_BLOCK_BUFFER_DECAY = getDouble("ghost-blocks-fix.buffer.decay");
            Config.GHOST_BLOCK_MESSAGE = getString("ghost-blocks-fix.message");
            Config.GHOST_BLOCK_MIN_TICKS = getInt("ghost-blocks-fix.ticks");
            Config.GHOST_BLOCK_FIX_UPDATE_BLOCKS = getBoolean("ghost-blocks-fix.update-blocks");
            Config.GUI_TITLE = getString("gui.title");
            Config.FLIGHT_COOLDOWN = getInt("settings.flight-cooldown");
            Config.GHOST_BLOCK_MESSAGE_ENABLED = getBoolean("ghost-blocks-fix.message-enabled");
            Config.GHOST_BLOCK_STAFF_MESSAGE_ENABLED = getBoolean("ghost-blocks-fix.staff-message-enabled");
            Config.GHOST_BLOCK_STAFF_MESSAGE = getString("ghost-blocks-fix.staff-message");
            Config.GHOST_BLOCK_SETBACK = getBoolean("ghost-blocks-fix.setback");
            Config.GHOST_BLOCK_PRINT_TO_CONSOLE = getBoolean("ghost-blocks-fix.print-to-console");
            Config.TOP_COMMAND_UNRESOLVED = getString("settings.top-command-unresolved");
            Config.JDAY_COOLDOWN = getLong("judgement-days.cooldown");
            Config.PUNISHMENT_STATISTIC_BROADCAST = getBoolean("punishment-statistics-broadcast.enabled");
            Config.PUNISHMENT_STATISTIC_BROADCAST_MESSAGE = getStringList("punishment-statistics-broadcast.broadcast");
            Config.PUNISHMENT_STATISTIC_BROADCAST_INTERVAL = getInt("punishment-statistics-broadcast.interval");
            Config.LOGGED_OUT_FROZEN_COMMANDS = getStringList("freeze.logged-out-while-frozen-commands");
            Config.EXPERIMENTAL_SYMBOL = getString("alerts.experimental-symbol");
            Config.RESET_COMMAND_SYNTAX = getString("messages.reset-command-syntax");
            Config.VIOLATIONS_RESET = getString("messages.violations-reset");
            Config.UNKNOWN_COMMAND = getString("messages.unknown-command");
            Config.STAFF_FROZE_BROADCAST = getString("messages.froze-staff-broadcast");
            Config.STAFF_UNFROZE_BROADCAST = getString("messages.unfroze-staff-broadcast");
            Config.LENIENT_SCAFFOLDING = getBoolean("settings.lenient-scaffolding");
            Config.GHOST_BLOCK_CONSOLE_MESSAGE = getString("ghost-blocks-fix.console-message");
            Config.ENTITY_COLLISION_FIX = getBoolean("settings.entity-collision");
            Config.ENTITY_CRAM_FIX_ENABLED = getBoolean("settings.entity-cram-fix-enabled");
            Config.ENTITY_CRAM_FIX_AMOUNT = getInt("settings.entity-cram-entities-amount");
            Config.ENTITY_CRAM_FIX_EXEMPT_TICKS = getInt("settings.entity-cram-exempt-ticks");
            CacheUtil.updateCheckValues();
        } catch (final Exception e) {
            Bukkit.getLogger().severe("Error while loading Vulcan's configuration file!");
            e.printStackTrace();
        }
    }

    public static boolean getBoolean(final String string) {
        return Config.config.getBoolean(string);
    }

    public static String getString(final String string) {
        return Config.config.getString(string);
    }

    public static int getInt(final String string) {
        return Config.config.getInt(string);
    }

    public static double getDouble(final String string) {
        return Config.config.getDouble(string);
    }

    public static long getLong(final String string) {
        return Config.config.getLong(string);
    }

    public static List<String> getStringList(final String string) {
        return Config.config.getStringList(string);
    }

    public static boolean isString(final String string) {
        return Config.config.isString(string);
    }

    public static Color getColor(final String string) {
        return Config.config.getColor(string);
    }

    public static boolean isColor(final String string) {
        return Config.config.isColor(string);
    }

    public static void setValue(final String path, final Object object) {
        Anticheat.INSTANCE.getPlugin().getConfig().set(path, object);
        Anticheat.INSTANCE.getPlugin().saveConfig();
        Anticheat.INSTANCE.getPlugin().reloadConfig();
    }
}
