/*
 * TuringMachine
 *
 * Copyright (c) 2012 Manuel Mart�nez-Almeida
 * Copyright (c) 2012 Jose Maria Pinilla
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
			
			if(tape[i] != TuringMachine.valueBLANK)
				this.world.add(loc, createFor(tape[i]));
			else
				this.world.remove(loc);
		}
		
		// UPDATE MESSAGE
		world.setMessage("Steps: "+this.step+"\nCurrent state: "+this.machine.getState());
		
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
