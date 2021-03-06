package me.andre111.items;

import me.andre111.items.item.LuaSpell;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.JsePlatform;

public class LuaController {
	public Globals globals;
	
	public LuaController() {
		// create an environment to run in
		globals = JsePlatform.standardGlobals();
	}
	
	public void loadScript(String script) {
		try {
			// Use the convenience function on the globals to load a chunk.
			LuaValue chunk = globals.loadfile(script);
			
			// Use any of the "call()" or "invoke()" functions directly on the chunk.
			chunk.call( LuaValue.valueOf(script) );
		} catch (LuaError error) {
			System.out.println(error.getLocalizedMessage());
		}
	}
	
	public boolean castFunction(LuaSpell spell, String name, String player, String targetPlayer, Block block, Location loc) {
		try {
			if(globals.get(name).isfunction()) {
				LuaValue[] args = new LuaValue[4];
				args[0] = LuaValue.valueOf(player);
				args[1] = LuaValue.valueOf(targetPlayer);
				args[2] = LuaValue.userdataOf(block);
				args[3] = LuaValue.userdataOf(loc);
				
				globals.get("utils").set("currentSpell", LuaValue.userdataOf(spell));
				Varargs vars = globals.get(name).invoke(LuaValue.varargsOf(args));
				globals.get("utils").set("currentSpell", LuaValue.NIL);
				
				if(vars.narg()>0) {
					LuaValue returnVal = vars.arg(1);
							
					if(returnVal.isboolean())
						return returnVal.toboolean();
				}
			}
		} catch (LuaError error) {
			System.out.println(error.getLocalizedMessage());
		}
		
		return false;
	}
}
