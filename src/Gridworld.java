
import java.awt.Color;
import java.util.ArrayList;

import info.gridworld.actor.*;
import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

class GridManager {
	
	public TuringMachine machine;
	public ActorWorld world;
	
	GridManager(TuringMachine machine)
	{
		this.machine = machine;
		
		BoundedGrid<Actor> grid = new BoundedGrid<Actor>(2, machine.size);
		this.world = new ActorWorld(grid);
		this.world.add(new Lector(this));
	}
	
	
	void configActor(Actor actor, int value)
	{
		if(actor != null) {
			Color color;
			switch(value)
			{
			case 0: color = Color.BLACK; break;
			case 1: color = Color.WHITE; break;
			case TuringMachine.valueA: color = Color.YELLOW; break;
			case TuringMachine.valueB: color = Color.BLUE; break;
			case TuringMachine.valueC: color = Color.CYAN; break;
			case TuringMachine.valueX: color = Color.RED; break;
			case TuringMachine.valueY: color = Color.GREEN; break;
			default: color = Color.GRAY; break;
			}
			actor.setColor(color);
		}
	}
	
	
	public void updateTape()
	{
		int [] tape = this.machine.getTape();
		for(int i = 0; i < this.machine.size; ++i)
		{
			Location loc = new Location(0, i);
			Actor r = null;
			
			if(tape[i] != TuringMachine.valueBLANK) {
				r = new Rock();
				this.world.add(loc, r);
				configActor(r, tape[i]);

			}else
				this.world.remove(loc);
		}
	}
	
	
	public void show()
	{
		this.world.show();
	}
}


class Lector extends Bug
{
	GridManager manager;
	
	Lector(GridManager manager)
	{
		this.manager = manager;
	}

	public void act()
	{
		this.manager.machine.step(1);
		this.moveTo(new Location(1, this.manager.machine.getPosition()));
		this.manager.updateTape();
	}
}

