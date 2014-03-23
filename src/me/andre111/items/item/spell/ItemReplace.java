package me.andre111.items.item.spell;

import me.andre111.items.item.ItemSpell;
import me.andre111.items.item.SpellVariable;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ItemReplace extends ItemSpell {
	private int range = 3;
	private int originalID = 1;
	private int originalDamage = 0;
	private int replaceID = 1;
	private int replaceDamage = 0;
	
	@Override
	public void setCastVar(int id, double var) {
		if(id==0) range = (int) Math.round(var);
	}
	
	@Override
	public void setCastVar(int id, String var) {
		if(id==1) {
			String[] split = var.split(":");
			
			originalID = Integer.parseInt(split[0]);
			originalDamage = Integer.parseInt(split[1]);
		}
		else if(id==2) {
			String[] split = var.split(":");
			
			replaceID = Integer.parseInt(split[0]);
			replaceDamage = Integer.parseInt(split[1]);
		}
	}
	
	@Override
	public void setCastVar(int id, SpellVariable var) {
		if(id==0) range = var.getAsInt();
		else if(id==1) {
			String[] split = var.getAsString().split(":");
			
			originalID = Integer.parseInt(split[0]);
			originalDamage = Integer.parseInt(split[1]);
		}
		else if(id==2) {
			String[] split = var.getAsString().split(":");
			
			replaceID = Integer.parseInt(split[0]);
			replaceDamage = Integer.parseInt(split[1]);
		}
	}
	
	@Override
	public boolean cast(Player player, Location loc, Player target, Block block) {
		return replaceNear(loc);
	}
	
	// TODO: This will need to up updated SOON Item Numbers going away completely!
	@SuppressWarnings("deprecation")
	private boolean replaceNear(Location loc) {
		boolean replaced = false;
		
		for(int xx=-range; xx<=range; xx++) {
			for(int yy=-range; yy<=range; yy++) {
				for(int zz=-range; zz<=range; zz++) {
					Block block = loc.getWorld().getBlockAt(loc.getBlockX()+xx, loc.getBlockY()+yy, loc.getBlockZ()+zz);
					
					if(block.getTypeId()==originalID && block.getData()==originalDamage) {
						block.setTypeIdAndData(replaceID, (byte) replaceDamage, false);
					}
				}
			}
		}
		
		return replaced;
	}
}
