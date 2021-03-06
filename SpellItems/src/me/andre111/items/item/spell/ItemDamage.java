package me.andre111.items.item.spell;

import me.andre111.items.SpellItems;
import me.andre111.items.item.ItemSpell;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

public class ItemDamage extends ItemSpell {
	/*private String playername = "";
	private int damage = 4;*/
	
	@Override
	public Varargs invoke(Varargs args) {
		if(args.narg()>=3) {
			LuaValue playerN = args.arg(1);
			LuaValue targetN = args.arg(2);
			LuaValue damageN = args.arg(3);
			
			if(playerN.isstring() && targetN.isstring() && damageN.isnumber()) {
				Player player = Bukkit.getPlayerExact(playerN.toString());
				Player target = Bukkit.getPlayerExact(targetN.toString());
				double damage = damageN.todouble();
				
				if(player!=null && target!=null) {
					if(castIntern(target, player, damage))
						return RETURN_TRUE;
				}
			}
		} else {
			SpellItems.log("Missing Argument for "+getClass().getCanonicalName());
		}
		
		return RETURN_FALSE;
	}
	
	private boolean castIntern(Player player, Player source, double damage) {
		if(damage>0) {
			player.damage(damage, source);
		} else {
			double newHealth = player.getHealth() - damage;
			if(newHealth>player.getMaxHealth()) newHealth = player.getMaxHealth();
			
			player.setHealth(newHealth);
		}
		
		return true;
	}
}