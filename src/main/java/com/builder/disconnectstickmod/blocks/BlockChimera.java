package com.builder.disconnectstickmod.blocks;

import com.builder.disconnectstickmod.DisconnectStickMod;
import com.builder.disconnectstickmod.tileentities.TileEntityChimera;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * キメラブロック
 * 竹Modの縮小フィールド的な
 */
public class BlockChimera extends Block implements ITileEntityProvider {
    public BlockChimera(){
        super(Material.rock);
    }

    /**
     * ITileEntityProviderは実装しないといけないらしい
     * 用途は不明
     * @param worldIn わからん
     * @param meta しらん
     * @return 対応するTileEntityのインスタンス
     */
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityChimera();
    }

    // これがないとITileEntityProviderを実装していないとエラーになる

    /**
     * ブロックが右クリックされたときの動作（ITileEntityProvider#onBlockActivatedの実装）
     * 中身はないので空っぽ
     * @return わからん
     */
    public boolean onBlockActivated() {
        return true;
    }

    /**
     * ブロックが右クリックされたときの動作（Block#onBlockActivatedのオーバーライド）
     * @param world
     * @param x
     * @param y
     * @param z
     * @param player
     * @param side
     * @param subX
     * @param subY
     * @param subZ
     * @return わからん
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ){
        if(world.isRemote) return true;

        DisconnectStickMod.LOG.info("block clicked!");
        return true;
    }
}
