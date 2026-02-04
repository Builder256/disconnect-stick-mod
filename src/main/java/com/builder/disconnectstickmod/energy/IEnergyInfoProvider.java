package com.builder.disconnectstickmod.energy;

import net.minecraft.tileentity.TileEntity;

/**
 * 各電力規格ハンドラの共通インターフェース。
 * 将来的にIC2 (EU) 等に対応する際は、このインターフェースを実装する。
 */
public interface IEnergyInfoProvider {
    /**
     * このプロバイダが利用可能か（対応Modがロードされているか）。
     * パフォーマンスのため、結果はキャッシュされるべき。
     */
    boolean isAvailable();

    /**
     * 指定されたTileEntityをこのプロバイダで処理可能か判定。
     * isAvailable() が true の場合のみ呼び出されるべき。
     */
    boolean canHandle(TileEntity tileEntity);

    /**
     * TileEntityからエネルギー情報を取得。
     * canHandle() が true を返した場合のみ呼び出されるべき。
     *
     * @param tileEntity 対象のTileEntity
     * @param side       ForgeDirection.getOrientation(side) の結果
     * @return エネルギー情報、取得できない場合はnull
     */
    EnergyInfo getInfo(TileEntity tileEntity, int side);
}
