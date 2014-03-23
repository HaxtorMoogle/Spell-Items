package me.andre111.items.item.spell;

import me.andre111.items.item.ItemSpell;
import me.andre111.items.item.SpellVariable;
import me.andre111.items.world.WorldTornado;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ItemWorldTornado extends ItemSpell {
	private int time = 10*20;
	private double moveSpeed = 0.05;
	private int changeChance = 1;
	
	private int blockChance = 70;
	private int radius = 3;
	private boolean hurt = false;
	
	@Override
	public void setCastVar(int id, double var) {
		if(id==0) time = (int) Math.round(var);
		else if(id==1) moveSpeed = var;
		else if(id==2) changeChance = (int) Math.round(var);
		else if(id==3) blockChance = (int) Math.round(var);
		else if(id==4) radius = (int) Math.abs(Math.round(var));
		else if(id==5) hurt = var==1;
	}
	
	@Override
	public void setCastVar(int id, SpellVariable var) {
		if(id==0) time = var.getAsInt();
		else if(id==1) moveSpeed = var.getAsDouble();
		else if(id==2) changeChance = var.getAsInt();
		else if(id==3) blockChance = var.getAsInt();
		else if(id==4) radius = (int) Math.abs(var.getAsInt());
		else if(id==5) hurt = var.getAsIntBoolean();
	}
	
	@Override
	public boolean cast(Player player, Location loc, Player target, Block block) {
		return castIntern(loc);
	}
	
	private boolean castIntern(Location loc) {
		WorldTornado effect = new WorldTornado(moveSpeed, changeChance, blockChance, radius, hurt);
		effect.start(loc.getWorld(), loc, time);
		
		return true;
	}
}
