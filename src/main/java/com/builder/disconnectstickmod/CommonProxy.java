package com.builder.disconnectstickmod;

import com.builder.disconnectstickmod.energy.EnergyInfoProviderRegistry;
import com.builder.disconnectstickmod.items.ItemDisconnectStick;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

    public static ItemDisconnectStick itemDisconnectStick;

    // preInit "Run before anything else. Read your config, create blocks, items,
    // etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        DisconnectStickModConfig.synchronizeConfiguration(event.getSuggestedConfigurationFile());

        // エネルギープロバイダレジストリを初期化
        EnergyInfoProviderRegistry.init();

        DisconnectStickMod.LOG.info(DisconnectStickModConfig.greeting);
        DisconnectStickMod.LOG.info("I am MyMod at version " + Tags.VERSION);

        itemDisconnectStick = new ItemDisconnectStick();
        itemDisconnectStick.setUnlocalizedName("disconnect_stick");
        itemDisconnectStick.setTextureName("disconnectstickmod:disconnect_stick");
        GameRegistry.registerItem(itemDisconnectStick, "disconnect_stick");
    }

    // load "Do your mod setup. Build whatever data structures you care about.
    // Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
    }

    // postInit "Handle interaction with other mods, complete your setup based on
    // this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
    }

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
    }
}
