package com.builder.disconnectstickmod.energy;

/**
 * エネルギー情報を保持するクラス。
 * 電力規格に依存しない共通の戻り値型として使用。
 */
public class EnergyInfo {
    /** その装置に現在保存されている電力量 */
    public final int stored;
    /** その装置が保存することができる最大電力量 */
    public final int max;
    /** その装置が使用する電力規格名（例: "RF"） */
    public final String unit;

    public EnergyInfo(int stored, int max, String unit) {
        this.stored = stored;
        this.max = max;
        this.unit = unit;
    }
}
