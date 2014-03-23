package me.andre111.items.item.spell;

import me.andre111.items.ItemHandler;
import me.andre111.items.item.ItemSpell;
import me.andre111.items.item.SpellVariable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ItemLay extends ItemSpell {
	private int radius;
	private String message = "";
	
	@Override
	public void setCastVar(int id, double var) {
		if(id==0) radius = (int) Math.round(var);
	}
	
	@Override
	public void setCastVar(int id, String var) {
		if(id==1) message = var;
	}
	
	@Override
	public void setCastVar(int id, SpellVariable var) {
		if(id==0) radius = var.getAsInt();
		else if(id==1) message = var.getAsString();
	}
	
	@Override
	public boolean cast(Player player, Location loc, Player target, Block block) {
		if(player!=null) {
			return castAt(player, loc);
		}
		
		return false;
	}
	
	private boolean castAt(Player player, Location loc) {
		if(ItemHandler.countItems(player, 383, 0)>=1) {
			ItemHandler.removeItems(player, 383, 0, 1);
			
			World w = loc.getWorld();
			int x = loc.getBlockX();
			int y = loc.getBlockY();
			int z = loc.getBlockZ();
			for(int xx=-radius; xx<=radius; xx++) {
				for(int yy=-radius; yy<=radius; yy++) {
					for(int zz=-radius; zz<=radius; zz++) {
						Block block = w.getBlockAt(x+xx, y+yy, z+zz);
						Material bid = block.getType();
						if(bid==Material.STONE || bid==Material.COBBLESTONE || bid==Material.SMOOTH_BRICK) {
							block.setType(Material.MONSTER_EGGS);
						}
					}
				}
			}
			
			if(!message.equals(""))
				Bukkit.getServer().broadcastMessage(message);
			
			return true;
		} else {
			//player.sendMessage(ConfigManager.getLanguage().getString("string_need_egg","You need an Egg to Infect!"));
			player.sendMessage("You need an Egg to Infect!");
			
			return false;
		}
	}
}
