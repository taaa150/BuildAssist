package com.github.unchama.buildassist;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MinuteTaskRunnable extends BukkitRunnable{
	private BuildAssist plugin = BuildAssist.plugin;
	private HashMap<UUID, PlayerData> playermap = BuildAssist.playermap;
	private ExperienceManager expman;

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

			if(playerdata.flyflag = true){
				int flytime = playerdata.flytime ;
				if(flytime == 0){
					player.sendMessage(ChatColor.GREEN + "Fly効果が終了しました");
					playerdata.flyflag = false;
					player.setFlying(false);
				}else {
					if(!expman.hasExp(10)){
						player.sendMessage(ChatColor.RED + "Fly効果の発動に必要な経験値が不足しているため、");
						player.sendMessage(ChatColor.RED + "Fly効果を終了しました");
						playerdata.flytime = 0 ;
						playerdata.flyflag = false;
						player.setFlying(false);
					}else {
						player.setFlying(true);
						player.sendMessage(ChatColor.GREEN + "Fly効果はあと" + flytime + "分です");
						playerdata.flytime --;
						expman.changeExp(-10);
					}
				}
			}
		}
	}

}
