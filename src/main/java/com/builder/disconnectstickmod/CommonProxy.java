package com.builder.disconnectstickmod;

import com.builder.disconnectstickmod.blocks.BlockChimera;
import com.builder.disconnectstickmod.client.renderer.RenderChimera;
import com.builder.disconnectstickmod.energy.EnergyInfoProviderRegistry;
import com.builder.disconnectstickmod.items.ItemDisconnectStick;

import com.builder.disconnectstickmod.tileentities.TileEntityChimera;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy {

    public static ItemDisconnectStick itemDisconnectStick;
    public static BlockChimera blockChimera;

    public static int renderId;

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

        blockChimera = new BlockChimera();
        blockChimera.setBlockName("chimera_block");
        blockChimera.setBlockTextureName("disconnectstickmod:chimera_block");
        GameRegistry.registerBlock(blockChimera, "chimera_block");
        GameRegistry.registerTileEntity(TileEntityChimera.class, "TileEntityChimera");
    }

    // load "Do your mod setup. Build whatever data structures you care about.
    // Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            renderId = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(new RenderChimera());
        }
    }

    // postInit "Handle interaction with other mods, complete your setup based on
    // this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
    }

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
    }
}
