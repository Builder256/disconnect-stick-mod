package com.builder.disconnectstickmod.tileentities;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;

import java.util.Arrays;

public class TileEntityChimera extends TileEntity {

    /** 各領域を描画するかどうか */
    private byte visibilityMask = -1;
    /** 各領域のテクスチャのブロック */
    private Block[] renderBlocks = new Block[8];

    public TileEntityChimera() {
//        visibilityMask = -127; // テスト用: 00001000
        Arrays.fill(renderBlocks, Blocks.stone);
    }

    public byte getVisibilityMask() {
        return visibilityMask;
    }

    public Block[] getRenderBlocks() {
        return renderBlocks;
    }

    public void setVisibilityMask(byte visibilityMask){
        this.visibilityMask = visibilityMask;
    }

    public void setRenderBlocks(Block[] renderBlocks) {
        this.renderBlocks = renderBlocks;
    }
}
