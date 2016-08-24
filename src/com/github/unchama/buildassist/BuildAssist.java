package com.github.unchama.buildassist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class BuildAssist  extends JavaPlugin {
	//このクラスのインスタンス
	public static BuildAssist plugin;
	//デバッグ用変数
	public static final boolean DEBUG = true;
	//起動するタスクリスト
	private List<BukkitTask> tasklist = new ArrayList<BukkitTask>();
	//Playerdataに依存するデータリスト
	public static final HashMap<UUID,PlayerData> playermap = new HashMap<UUID,PlayerData>();

	//lvの閾値
	public static final List<Integer> levellist = new ArrayList<Integer>(Arrays.asList(
			0,15,50,100,175,//5
			300,450,650,900,1200,//10
			1600,2100,2700,3400,4200,//15
			5100,6100,7500,9000,10500,//20
			12000,14000,16000,18000,20000,//25
			25000,30000,35000,40000,50000//30
			));

	//設置ブロックの対象リスト
	public static final List<Material> materiallist = new ArrayList<Material>(Arrays.asList(
			Material.ACACIA_STAIRS,Material.ACACIA_FENCE,Material.ACACIA_FENCE_GATE,
			Material.BIRCH_WOOD_STAIRS,Material.BIRCH_FENCE,Material.BIRCH_FENCE_GATE,
			Material.BONE_BLOCK,Material.BOOKSHELF,
			Material.BRICK,Material.BRICK_STAIRS,
			Material.CACTUS,Material.CHEST,
			Material.CLAY_BRICK,
			Material.DARK_OAK_STAIRS,Material.DARK_OAK_FENCE,Material.DARK_OAK_FENCE_GATE,
			Material.END_BRICKS,
			Material.FURNACE,Material.GLOWSTONE,Material.HARD_CLAY,
			Material.JACK_O_LANTERN,Material.JUKEBOX,Material.JUNGLE_FENCE,Material.JUNGLE_FENCE_GATE,
			Material.JUNGLE_WOOD_STAIRS,Material.LADDER,Material.LEAVES,Material.LEAVES_2,
			Material.LOG,Material.LOG_2,Material.NETHER_BRICK,Material.NETHER_BRICK_STAIRS,
			Material.NETHER_WART_BLOCK,Material.RED_NETHER_BRICK,
			Material.OBSIDIAN,Material.PACKED_ICE,Material.PRISMARINE,
			Material.PUMPKIN,Material.PURPUR_BLOCK,Material.PURPUR_SLAB,
			Material.PURPUR_STAIRS,Material.PURPUR_PILLAR,
			Material.QUARTZ_BLOCK,Material.QUARTZ_STAIRS,Material.QUARTZ,
			Material.SANDSTONE,Material.SANDSTONE_STAIRS,Material.SEA_LANTERN,
			Material.SLIME_BLOCK,Material.SMOOTH_BRICK,Material.SMOOTH_STAIRS,
			Material.SNOW_BLOCK,Material.SPRUCE_FENCE,Material.SPRUCE_FENCE_GATE,
			Material.SPRUCE_WOOD_STAIRS,Material.FENCE,Material.FENCE_GATE,
			Material.STAINED_CLAY,Material.STAINED_GLASS,Material.STAINED_GLASS_PANE,
			Material.STEP,Material.STONE,Material.STONE_SLAB2,Material.THIN_GLASS,
			Material.TORCH,Material.WOOD,
			Material.WOOD_STAIRS,Material.WOOD_STEP,
			Material.WOOL,Material.CARPET,Material.WORKBENCH


			));

	//プラグインを起動したときに処理するメソッド
	@Override
	public void onEnable(){
		plugin = this;
		//リスナの登録
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);


		//オンラインの全てのプレイヤーを処理
		for(Player p : getServer().getOnlinePlayers()){
			//UUIDを取得
			UUID uuid = p.getUniqueId();
			//プレイヤーデータを生成
			PlayerData playerdata = new PlayerData(p);
			//統計量を取得
			int builds = BuildBlock.calcBuildBlock(p);
			playerdata.levelupdata(p,builds);
			//プレイヤーマップにプレイヤーを追加
			playermap.put(uuid,playerdata);
		}

		getLogger().info("BuildAssist is Enabled!");

		//１分おき
		if(DEBUG){
			tasklist.add(new MinuteTaskRunnable().runTaskTimer(this,0,300));
		}else{
			tasklist.add(new MinuteTaskRunnable().runTaskTimer(this,0,1200));
		}
	}
	@Override
	public void onDisable(){
		//全てのタスクをキャンセル
		for(BukkitTask task:tasklist){
			task.cancel();
		}
	}
}
