package com.builder.disconnectstickmod.blocks;

import com.builder.disconnectstickmod.DisconnectStickMod;
import com.builder.disconnectstickmod.tileentities.TileEntityChimera;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import static com.builder.disconnectstickmod.CommonProxy.renderId;

/**
 * キメラブロック
 * 竹Modの縮小フィールド的な
 */
public class BlockChimera extends Block implements ITileEntityProvider {
    public BlockChimera() {
        super(Material.rock);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    /**
     * そのブロックのTileEntityのインスタンスを返す<br>
     * ブロックが設置されたときに呼ばれるらしい
     *
     * @param worldIn わからん
     * @param meta    しらん
     * @return 対応するTileEntityのインスタンス
     */
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityChimera();
    }

    /**
     * ブロックが右クリックされたときの動作
     *
     * @return 腕を振るかどうか？？？？
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ) {
        // クライアントサイドでは処理しない
        if (!world.isRemote) return true;

        // デバッグ用
        DisconnectStickMod.LOG.info("block clicked\n world: {},\nx: {}, y: {}, z: {}, \nplayer: {},\nside: {},\nsubX: {}, subY: {}, subZ: {}", world, x, y, z, player, side, subX, subY, subZ);

        Block block = getBlockFromPlayerHand(player);
        if (block == null) return false;

        DisconnectStickMod.LOG.info("block in hand: {}", block);

        return true;
    }

    /**
     * このブロックのために呼ばれるrender関数のタイプ
     *
     * @return FMLから貰ったこのブロックのrenderId
     */
    public int getRenderType() {
        return renderId;
    }

    /**
     * 通常のブロックとしてレンダリングされるかどうからしい
     * 何を返しても変わった感じはしない
     * @return 常にfalse
     */
//    public boolean renderAsNormalBlock() {
//        return false;
//    }

    /**
     * 不透過で1mの立方体であるかどうか
     * 隣接するブロック間の面を描画するかや、松明や赤石ワイヤを設置できるかどうかの判定に利用されるらしい
     * @return 常にfalse
     */
    public boolean isOpaqueCube(){
        return false;
    }

    // 長いので切り出す
    // 純粋関数なのでstaticにしといたほうがよさげ？
    // BlockChimeraの持ち物にすべきかは知らん

    /**
     * プレイヤーがメインハンドに持っているブロック
     *
     * @param player 取得するプレイヤー
     * @return Block、なければnull
     */
    private static Block getBlockFromPlayerHand(EntityPlayer player) {
        // プレイヤーが手に持っているアイテム
        ItemStack itemStackInHand = player.getHeldItem();
        // 手に何も持っていないときなどにnullになる
        // 意味がないことを示すためにfalseを返して腕を振らなくする
        if (itemStackInHand == null) return null;

        // ItemStackからItemを取得
        Item itemInHand = itemStackInHand.getItem();
        // nullになることがあるっぽい 同様に腕を振らない
        if (itemInHand == null) return null;

        // ItemからBlockを取得
        Block blockInHand = Block.getBlockFromItem(itemInHand);
        // 対応するBlockがないときにnullやBlocks.airが返されるみたい
        if (blockInHand == null || blockInHand.equals(Blocks.air)) return null; // equals？それとも等価演算子？

        return blockInHand;
    }
}
