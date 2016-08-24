package com.github.unchama.buildassist;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class PlayerJoinListener implements Listener {
	HashMap<UUID,PlayerData> playermap = BuildAssist.playermap;

	//プレイヤーがjoinした時に実行
	@EventHandler
	public void onplayerJoinEvent(PlayerJoinEvent event){
		//ジョインしたplayerを取得
		Player player = event.getPlayer();
		//プレイヤーのuuidを取得
		UUID uuid = player.getUniqueId();
		//プレイヤーデータを宣言
		PlayerData playerdata = null;
		//ログインしたプレイヤーのデータが残っていなかった時にPlayerData作成
		if(!playermap.containsKey(uuid)){
			//新しいplayerdataを作成
			playerdata = new PlayerData(player);
			//playermapに追加
			playermap.put(player.getUniqueId(), playerdata);
		}else{
			playerdata = playermap.get(uuid);
		}
		//統計量を取得
		int builds = BuildBlock.calcBuildBlock(player);
		playerdata.levelupdata(player,builds);
	}
}
