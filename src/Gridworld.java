
import java.awt.Color;

import info.gridworld.actor.*;
import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Location;

class GridManager {
	
	public TuringMachine machine;
	public ActorWorld world;
	public Reader reader;
	public long stoptime;
	public long timestamp;
	boolean waiting;
	public int step;
	
	GridManager(TuringMachine machine, long stopTime)
	{		
		BoundedGrid<Actor> grid = new BoundedGrid<Actor>(2, machine.size);
		this.machine = machine;
		this.world = new ActorWorld(grid);
		this.reader = new Reader(this);
		this.world.add(this.reader);
		if(stopTime < 0)
			this.stoptime = Long.MAX_VALUE;
		else
			this.stoptime = stopTime;
		
		this.waiting = false;
		this.step = 0;
		
		updateItems();
	}
	
	
	Actor createFor(int value)
	{
		switch(value)
		{
		case 0: return new Value0();
		case 1: return new Value1();
		case TuringMachine.valueA: return new ValueA();
		case TuringMachine.valueB: return new ValueB();
		case TuringMachine.valueC: return new ValueC();
		case TuringMachine.valueD: return new ValueD();
		case TuringMachine.valueX: return new ValueX();
		case TuringMachine.valueY: return new ValueY();
		default: return new ValueN();
		}
	}
	
	
	public void updateItems()
	{
		// UPDATE READER
		this.reader.moveTo(new Location(1, this.machine.getPosition()));
		
		
		// UPDATE TAPE
		int [] tape = this.machine.getTape();
		for(int i = 0; i < this.machine.size; ++i)
		{
			Location loc = new Location(0, i);
			Actor r = null;
			
			if(tape[i] != TuringMachine.valueBLANK) {
				r = createFor(tape[i]);
				this.world.add(loc, r);

			}else
				this.world.remove(loc);
		}
		world.setMessage("Step: "+this.step+"\nState: "+this.machine.getState());
		
		if(this.machine.inHaltState) {
			this.waiting = true;
			this.timestamp = System.currentTimeMillis();
		}
	}
	
	
	public void show()
	{
		this.world.show();
	}
}


class Reader extends Bug
{
	GridManager manager;
	
	Reader(GridManager manager)
	{
		setColor(Color.WHITE);
		this.manager = manager;
	}

	public void act()
	{
		if((System.currentTimeMillis()-manager.timestamp) >= manager.stoptime)
			manager.waiting = false;

		if(!manager.waiting) {
			this.manager.machine.step(1);
			++this.manager.step;
			this.manager.updateItems();
		}
	}
}


class Value0 extends Rock {
	Value0() {
		this.setColor(Color.WHITE);
	}
}
class Value1 extends Rock {
	Value1() {
		this.setColor(Color.WHITE);
	}
}
class ValueA extends Rock {
	ValueA() {
		this.setColor(Color.RED);
	}
}
class ValueB extends Rock {
	ValueB() {
		setColor(Color.CYAN);
	}
}
class ValueC extends Rock {
	ValueC() {
		setColor(Color.MAGENTA);
	}
}
class ValueD extends Rock {
	ValueD() {
		setColor(Color.ORANGE);
	}
}
class ValueX extends Rock {
	ValueX() {
		setColor(Color.GRAY);
	}
}
class ValueY extends Rock {
	ValueY() {
		setColor(Color.LIGHT_GRAY);
	}
}
class ValueN extends Rock {
	ValueN() {
		setColor(Color.white);
	}
}
