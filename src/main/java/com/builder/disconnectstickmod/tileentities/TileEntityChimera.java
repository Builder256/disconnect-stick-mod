package com.builder.disconnectstickmod.tileentities;

import com.builder.disconnectstickmod.DisconnectStickMod;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import java.util.Arrays;

public class TileEntityChimera extends TileEntity {
    /** セルの数 */
    private final int RANGE = 8;
    /** 各セルのテクスチャのブロック ないときはnull */
    private Block[] cellBlocks = new Block[this.RANGE];
    /** 各セルのテクスチャのメタデータ ないときはcellBlockもないだろ？ */
    private int[] cellMetadatas = new int[this.RANGE];

    /** NBTタグのキー */
    private static final String KEY_CELLBLOCK_NAMES = "CellBlockIds";
    /** NBTタグのキー */
    private static final String KEY_CELLBLOCK_METADATAS = "CellMetadatas";


    public TileEntityChimera() {
    }

    public void initializeCells() {
        this.cellBlocks = new Block[this.RANGE];
        this.cellMetadatas = new int[this.RANGE];
    }

    /**
     * セルブロックを設置する
     * @param cellIndex 設置するセブブロックの位置 0-7
     * @param block セルに設定するブロック
     * @param metadata セルに設定するブロックのメタデータ
     * @return ブロックを設置したかどうか
     */
    public boolean placeCellBlock(int cellIndex, Block block, int metadata) {
        // 範囲外のindexを弾く
        if (cellIndex < 0 || cellIndex >= this.RANGE) return false;
        // 既にある場合に弾く
        final boolean isUpdateRequired = this.cellBlocks[cellIndex] != block || this.cellMetadatas[cellIndex] != metadata;
        if (!isUpdateRequired) return false;

        this.cellBlocks[cellIndex] = block;
        this.cellMetadatas[cellIndex] = metadata;

        // サーバーにセーブデータに保存すべき内容ありと伝える
        this.markDirty();
        // クライアントに再レンダリングしろと伝える
        // this.getDescriptionPacketを実行させる
        // ワールドでクリックできてんのにworldObjがnullになることなんかあるの？
        if (this.worldObj != null) this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        return true;
    }

    public Block[] getCellBlocks() {
        return this.cellBlocks;
    }

    public int[] getCellMetadatas() {
        return this.cellMetadatas;
    }

    @Override
    public boolean canUpdate() {
        // TODO: 開発完了したらfalseにしろ
        return false;
    }

    // デバッグ用
    @Override
    public void updateEntity(){
        super.updateEntity();
        final boolean isClientSide = this.worldObj.isRemote;
        DisconnectStickMod.LOG.info((isClientSide ? "client side " : "server side ") + "updateEntity: " + Arrays.toString(this.cellBlocks));
        DisconnectStickMod.LOG.info((isClientSide ? "client side " : "server side ") + "updateEntity: " + Block.getIdFromBlock(cellBlocks[0]));
    }

    // 渡されたNBTから状態を読んでTileEntityに書き込む
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

//        final int[] blockIds = compound.getIntArray(KEY_CELLBLOCK_NAMES);
        final NBTTagList blockNameTagList = compound.getTagList(KEY_CELLBLOCK_NAMES, 8);
        final int[] metadatas = compound.getIntArray(KEY_CELLBLOCK_METADATAS);

        // 外部ツールや他のMod、あるいはバグにより、NBTがぶっ壊れている場合を考慮する
        // NBTTagCompound#getIntArrayは、指定されたキーがない場合にlength==0の配列を返す
        if (blockNameTagList.tagCount() != this.RANGE || metadatas.length != this.RANGE) {
            this.initializeCells();
            return;
        };

        for (int i = 0; i < this.RANGE; i++) {
            String blockName = blockNameTagList.getStringTagAt(i);
            this.cellBlocks[i] = Block.getBlockFromName(blockName);
        }
        this.cellMetadatas = metadatas;
    }

    // TileEntityの状態をNBTに書き込む
    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

//        String[] blockNames = new String[this.RANGE];
//        for (int i = 0; i < blockNames.length ; i++) {
//            Block block = this.cellBlocks[i];
//            blockNames[i] = block != null ? Block.blockRegistry.getNameForObject(block) : "";
//        }

        final String[] blockNames =  Arrays.stream(this.cellBlocks).map(block -> block != null ? Block.blockRegistry.getNameForObject(block) : "").toArray(String[]::new);
        final NBTTagList blockNameTagList = new NBTTagList();
        for (String blockName : blockNames) {
            blockNameTagList.appendTag(new NBTTagString(blockName));
        }

        compound.setTag(KEY_CELLBLOCK_NAMES, blockNameTagList);
        compound.setIntArray(KEY_CELLBLOCK_METADATAS, this.cellMetadatas);
    }

    // TileEntityの状態をNBTに詰め込んでクライアントに発送する
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
    }

    // サーバーからgetDescriptionPacketで状態が送られてきたときに、クライアントに反映する
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }
}
