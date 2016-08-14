package com.github.unchama.buildassist;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;





public class PlayerData {
	//プレイヤー名
	public String name;
	//UUID
	public UUID uuid;
	//プレイヤーの建築レベル
	public int level;


//プレイヤーデータクラスのコンストラクタ
	public PlayerData(Player player){
		//初期値を設定
		name = Util.getName(player);
		uuid = player.getUniqueId();
	}
	//レベルを更新
	public void levelupdata(Player player,int mines) {
		calcPlayerLevel(player,mines);
		setDisplayName(player);
	}

	//プレイヤーレベルを計算し、更新する。
	private void calcPlayerLevel(Player player,int mines){
		//現在のランクの次を取得
		int i = level + 1;
		//ランクが上がらなくなるまで処理
		while(BuildAssist.levellist.get(i).intValue() <= mines && i <= 101){
			if(!BuildAssist.DEBUG){
				//レベルアップ時のメッセージ
				player.sendMessage(ChatColor.GOLD+"ﾑﾑｯwwwwwwwﾚﾍﾞﾙｱｯﾌﾟwwwwwww【Lv("+(i-1)+")→Lv("+i+")】");
				//レベルアップ時の花火の打ち上げ
				Location loc = player.getLocation();
			}
			i++;
		}
		level = i-1;
	}

	//表示される名前に整地レベルを追加
	public void setDisplayName(Player p) {
		String displayname = Util.getName(p);
		if(p.isOp()){
			//管理人の場合
			displayname = ChatColor.RED + "<管理人>" + name;
		}
		if(level == 101){
			displayname =  ChatColor.GOLD + "[ GOD ]" + displayname + ChatColor.WHITE;
		}else{
			displayname =  "[ Lv" + level + " ]" + displayname + ChatColor.WHITE;
		}

		p.setDisplayName(displayname);
		p.setPlayerListName(displayname);
	}
	//オフラインかどうか
	public boolean isOffline() {
		return BuildAssist.plugin.getServer().getPlayer(name) == null;
	}
}
