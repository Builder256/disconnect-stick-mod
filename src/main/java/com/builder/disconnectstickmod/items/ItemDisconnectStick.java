package com.builder.disconnectstickmod.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.builder.disconnectstickmod.DisconnectStickMod;
import com.builder.disconnectstickmod.energy.EnergyInfo;
import com.builder.disconnectstickmod.energy.EnergyInfoProviderRegistry;

public class ItemDisconnectStick extends Item {

    public ItemDisconnectStick() {
        super();
        this.setCreativeTab(CreativeTabs.tabTools);
        this.setMaxStackSize(1);
    }

    /**
     * アイテムを手に持って右クリックをした場合の処理
     *
     * @return 腕を振る場合はtrue, そうでなければfalseらしい
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        // クライアントサイドでは処理しない
        if (world.isRemote) return true;

        TileEntity targetTileEntity = world.getTileEntity(x, y, z);
        // 対象のブロックが TileEntity でない（=ただのブロック？）ときはnullになる
        if (targetTileEntity == null) return true;

        // レジストリからエネルギー情報を取得
        EnergyInfo energyInfo = EnergyInfoProviderRegistry.getEnergyInfo(targetTileEntity, side);
        if (energyInfo == null) {
            DisconnectStickMod.LOG.info("The TileEntity is not an energy device");
            return true;
        }

        DisconnectStickMod.LOG.info("The TileEntity is an energy device (" + energyInfo.unit + ")");
        ForgeDirection direction = ForgeDirection.getOrientation(side);
        player.addChatMessage(
            new ChatComponentTranslation(
                "item.disconnect_stick.info",
                energyInfo.unit,
                energyInfo.stored,
                energyInfo.max,
                energyInfo.unit,
                new ChatComponentTranslation(
                    "disconnectstick.direction." + direction.name()
                        .toLowerCase())));

        return true;
    }
}
