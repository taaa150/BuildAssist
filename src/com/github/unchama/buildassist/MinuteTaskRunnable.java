package com.github.unchama.buildassist;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MinuteTaskRunnable extends BukkitRunnable{
	private BuildAssist plugin = BuildAssist.plugin;
	private HashMap<UUID, PlayerData> playermap = BuildAssist.playermap;


	@Override
	public void run() {
		playermap = BuildAssist.playermap;
		plugin = BuildAssist.plugin;
		//playermapが空の時return
		if(playermap.isEmpty()){
			return;
		}
		//プレイヤーマップに記録されているすべてのplayerdataについての処理
		for(PlayerData playerdata : playermap.values()){
			//プレイヤーがオフラインの時処理を終了、次のプレイヤーへ
			if(playerdata.isOffline()){
				continue;
			}
			//プレイﾔｰが必ずオンラインと分かっている処理
			//プレイヤーを取得
			Player player = plugin.getServer().getPlayer(playerdata.uuid);
			//統計量計算
			int mines = BuildBlock.calcBuildBlock(player);
			//Levelを設定
			playerdata.levelupdata(player,mines);
		}
	}

}
