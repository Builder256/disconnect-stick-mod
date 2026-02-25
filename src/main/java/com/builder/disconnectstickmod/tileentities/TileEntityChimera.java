package com.builder.disconnectstickmod.tileentities;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class TileEntityChimera extends TileEntity {
    /** セルの数 */
    private final int RANGE = 8;
    /** 各セルのテクスチャのブロック */
    private final Block[] cellBlocks = new Block[RANGE];

    public TileEntityChimera() {
    }

    public boolean placeCellBlock(int cellIndex, Block block){
        if(cellIndex < 0 || cellIndex > RANGE) return false;
        cellBlocks[cellIndex] = block;

        // サーバーにセーブデータに保存すべき内容ありと伝える
        this.markDirty();
        // クライアントに再レンダリングしろと伝える
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        return true;
    }

    public Block[] getCellBlocks() {
        return cellBlocks;
    }
}
