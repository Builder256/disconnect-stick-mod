package com.builder.disconnectstickmod.tileentities;

import com.builder.disconnectstickmod.DisconnectStickMod;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.Arrays;

public class TileEntityChimera extends TileEntity {
    /** セルの数 */
    private final int RANGE = 8;
    /** 各セルのテクスチャのブロック */
    private Block[] cellBlocks = new Block[RANGE];
    private int[] metadatas = new int[RANGE];

    public TileEntityChimera() {
    }

    @Override
    public void updateEntity(){
        super.updateEntity();
        DisconnectStickMod.LOG.info("updateEntity: " + Arrays.toString(this.cellBlocks));
        DisconnectStickMod.LOG.info("updateEntity: " + Block.getIdFromBlock(cellBlocks[0]));
    }

    public boolean placeCellBlock(int cellIndex, Block block, int metadata) {
        // 範囲外のindexを弾く
        if (cellIndex < 0 || cellIndex > RANGE) return false;
        // 既にある場合に弾く
        final boolean isUpdateRequired = cellBlocks[cellIndex] != block;
        if (!isUpdateRequired) return false;

        cellBlocks[cellIndex] = block;
        metadatas[cellIndex] = metadata;

        // サーバーにセーブデータに保存すべき内容ありと伝える
        this.markDirty();
        // クライアントに再レンダリングしろと伝える
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        return true;
    }

    public Block[] getCellBlocks() {
        return cellBlocks;
    }

    public int[] getMetadatas() {
        return metadatas;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        final int[] nbtBlockIds = compound.getIntArray("CellBlockIds");
        final int[] nbtMetadatas = compound.getIntArray("CellMetadatas");

        this.cellBlocks = (Block[]) Arrays.stream(nbtBlockIds).mapToObj(Block::getBlockById).toArray();
        this.metadatas = nbtMetadatas;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        final int[] nbtBlockIds =  Arrays.stream(cellBlocks).mapToInt(Block::getIdFromBlock).toArray();

        compound.setIntArray("CellBlockIds", nbtBlockIds);
        compound.setIntArray("CellMetadatas", metadatas);
    }
}
