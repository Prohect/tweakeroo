package fi.dy.masa.tweakeroo;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.tweakeroo.config.Configs;

public class Tweakeroo implements ModInitializer
{
    public static final Logger logger = LogManager.getLogger(Reference.MOD_ID);

    public static int renderCountItems;
    public static int renderCountXPOrbs;
    /**
     * Used to store the jump key, so that it can be told when deciding if auto switch elytra needed, as for onKeyPressed called by GL is a static method, cant access this there directly
     */
    public static net.minecraft.client.option.KeyBinding jumpKey = null;
    /**
     * same reason as above
     */
    public static ClientPlayerEntity player;
    public static ItemStack autoSwitchElytraChestplate = ItemStack.EMPTY;

    @Override
    public void onInitialize()
    {
        InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());
    }

    public static void printDebug(String msg, Object... args)
    {
        if (Configs.Generic.DEBUG_LOGGING.getBooleanValue())
        {
            Tweakeroo.logger.info(msg, args);
        }
    }
}
