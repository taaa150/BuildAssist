package com.github.unchama.buildassist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
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

	private HashMap<String, TabExecutor> commandlist;

	//lvの閾値
	public static final List<Integer> levellist = new ArrayList<Integer>(Arrays.asList(
			0,15,49,106,198,//5
			333,705,1265,2105,3347,//10
			4589,5831,7073,8315,9557,//15
			11047,12835,14980,17554,20642,//20
			24347,28793,34128,40530,48212,//25
			57430,68491,81764,97691,116803,//30
			135915,155027,174139,193251,212363,//35
			235297,262817,295841,335469,383022,//40
			434379,489844,549746,614440,684309,//45
			759767,841261,929274,1024328,1126986,//50

			/*
			新経験値テーブル
			51-60→125,000
			61-70→175,000
			71-80→250,000
			81-90→330,000
			91-100→420,000
			これで51-100の累計が1300万
			(毎日14.5万掘り続けて3か月ペース)
			(毎日21.5万掘り続けて2か月ペース)
			(毎日43.3万掘り続けて1か月ペース)
			 */

			1237856,1362856,1487856,1612856,1737856,//55
			1862856,1987856,2112856,2237856,2362856,//60
			2537856,2712856,2887856,3062856,3237856,//65
			3412856,3587856,3762856,3937856,4112856,//70
			4362856,4612856,4862856,5112856,5362856,//75
			5612856,5862856,6112856,6362856,6612856,//80
			6942856,7272856,7602856,7932856,8262856,//85
			8592856,8922856,9252856,9582856,9912856,//90
			10332856,10752856,11172856,11592856,12012856,//95
			12432856,12852856,13272856,13692856,14112856//100
			));

	//設置ブロックの対象リスト
	public static final List<Material> materiallist = new ArrayList<Material>(Arrays.asList(
			Material.STONE,Material.NETHERRACK,Material.NETHER_BRICK,Material.DIRT
			,Material.GRAVEL,Material.LOG,Material.LOG_2,Material.GRASS
			,Material.COAL_ORE,Material.IRON_ORE,Material.GOLD_ORE,Material.DIAMOND_ORE
			,Material.LAPIS_ORE,Material.EMERALD_ORE,Material.REDSTONE_ORE,Material.SAND
			,Material.SANDSTONE,Material.QUARTZ_ORE,Material.END_BRICKS,Material.ENDER_STONE
			,Material.ICE,Material.PACKED_ICE,Material.OBSIDIAN,Material.MAGMA,Material.SOUL_SAND
			));

	//プラグインを起動したときに処理するメソッド
	@Override
	public void onEnable(){
		plugin = this;

		//コマンドの登録
		commandlist = new HashMap<String, TabExecutor>();
		commandlist.put("fly",new flyCommand(plugin));

		//リスナの登録
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		getServer().getPluginManager().registerEvents(new EntityListener(), this);



		//オンラインの全てのプレイヤーを処理
		for(Player p : getServer().getOnlinePlayers()){
			//UUIDを取得
			UUID uuid = p.getUniqueId();
			//プレイヤーデータを生成
			PlayerData playerdata = new PlayerData(p);
			//統計量を取得
			int mines = BuildBlock.calcBuildBlock(p);
			playerdata.levelupdata(p,mines);
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
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		return commandlist.get(cmd.getName()).onCommand(sender, cmd, label, args);
	}
}
